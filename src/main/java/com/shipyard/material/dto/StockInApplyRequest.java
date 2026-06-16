package com.shipyard.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StockInApplyRequest {

    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode;

    @NotNull(message = "申请数量不能为空")
    private Integer applyQuantity;

    private String certificateNo;

    private LocalDate certificateValidDate;
}
