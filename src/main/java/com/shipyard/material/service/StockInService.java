package com.shipyard.material.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shipyard.material.dto.StockInApplyRequest;
import com.shipyard.material.entity.Material;
import com.shipyard.material.entity.StockInApply;
import com.shipyard.material.entity.Supplier;
import com.shipyard.material.mapper.MaterialMapper;
import com.shipyard.material.mapper.StockInApplyMapper;
import com.shipyard.material.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockInService {

    private final MaterialMapper materialMapper;
    private final SupplierMapper supplierMapper;
    private final StockInApplyMapper stockInApplyMapper;

    @Transactional
    public Map<String, Object> submitApply(StockInApplyRequest request) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder rejectReasons = new StringBuilder();
        boolean rejected = false;

        Material material = materialMapper.selectOne(
                new LambdaQueryWrapper<Material>()
                        .eq(Material::getMaterialCode, request.getMaterialCode())
        );
        if (material == null) {
            result.put("status", "REJECTED");
            result.put("rejectReason", "物料编码不存在: " + request.getMaterialCode());
            return result;
        }

        Supplier supplier = supplierMapper.selectOne(
                new LambdaQueryWrapper<Supplier>()
                        .eq(Supplier::getSupplierCode, request.getSupplierCode())
        );
        if (supplier == null) {
            result.put("status", "REJECTED");
            result.put("rejectReason", "供应商编码不存在: " + request.getSupplierCode());
            return result;
        }

        if (supplier.getQualificationValidDate().isBefore(LocalDate.now())) {
            rejected = true;
            rejectReasons.append("供应商资质已过期，有效期至: ").append(supplier.getQualificationValidDate());
        }

        if (material.getRequireCertificate() == 1) {
            if (request.getCertificateNo() == null || request.getCertificateNo().isEmpty()) {
                if (rejected) rejectReasons.append("; ");
                rejected = true;
                rejectReasons.append("该物料需要质保证书，但未提供质保证书编号");
            } else if (request.getCertificateValidDate() != null
                    && request.getCertificateValidDate().isBefore(LocalDate.now())) {
                if (rejected) rejectReasons.append("; ");
                rejected = true;
                rejectReasons.append("质保证书已过期，有效期至: ").append(request.getCertificateValidDate());
            }
        }

        int availableStock = material.getStockLimit() - material.getCurrentStock();
        int recommendQuantity = Math.min(request.getApplyQuantity(), availableStock);

        if (recommendQuantity <= 0) {
            if (rejected) rejectReasons.append("; ");
            rejected = true;
            rejectReasons.append("库存已达上限，当前库存: ").append(material.getCurrentStock())
                    .append("，上限: ").append(material.getStockLimit());
        }

        StockInApply apply = new StockInApply();
        apply.setApplyNo("APPLY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        apply.setMaterialId(material.getId());
        apply.setSupplierId(supplier.getId());
        apply.setApplyQuantity(request.getApplyQuantity());
        apply.setCertificateNo(request.getCertificateNo());
        apply.setCertificateValidDate(request.getCertificateValidDate());

        if (rejected) {
            apply.setStatus("REJECTED");
            apply.setRejectReason(rejectReasons.toString());
            apply.setRecommendQuantity(0);
            stockInApplyMapper.insert(apply);

            result.put("applyNo", apply.getApplyNo());
            result.put("status", "REJECTED");
            result.put("rejectReason", rejectReasons.toString());
            result.put("applyQuantity", request.getApplyQuantity());
            result.put("recommendQuantity", 0);
        } else {
            apply.setStatus("APPROVED");
            apply.setRecommendQuantity(recommendQuantity);
            stockInApplyMapper.insert(apply);

            result.put("applyNo", apply.getApplyNo());
            result.put("status", "APPROVED");
            result.put("applyQuantity", request.getApplyQuantity());
            result.put("recommendQuantity", recommendQuantity);
            result.put("currentStock", material.getCurrentStock());
            result.put("stockLimit", material.getStockLimit());
            result.put("materialName", material.getMaterialName());
            result.put("supplierName", supplier.getSupplierName());
        }

        return result;
    }

    public StockInApply getByApplyNo(String applyNo) {
        return stockInApplyMapper.selectOne(
                new LambdaQueryWrapper<StockInApply>()
                        .eq(StockInApply::getApplyNo, applyNo)
        );
    }
}
