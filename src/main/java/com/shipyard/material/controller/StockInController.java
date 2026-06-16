package com.shipyard.material.controller;

import com.shipyard.common.Result;
import com.shipyard.material.dto.StockInApplyRequest;
import com.shipyard.material.entity.StockInApply;
import com.shipyard.material.service.StockInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/material/stock-in")
@RequiredArgsConstructor
public class StockInController {

    private final StockInService stockInService;

    @PostMapping("/apply")
    public Result<Map<String, Object>> submitApply(@Valid @RequestBody StockInApplyRequest request) {
        Map<String, Object> result = stockInService.submitApply(request);
        String status = (String) result.get("status");
        if ("REJECTED".equals(status)) {
            return Result.fail(400, (String) result.get("rejectReason"));
        }
        return Result.success(result);
    }

    @GetMapping("/apply/{applyNo}")
    public Result<StockInApply> getApply(@PathVariable String applyNo) {
        StockInApply apply = stockInService.getByApplyNo(applyNo);
        if (apply == null) {
            return Result.fail("入库申请单不存在");
        }
        return Result.success(apply);
    }
}
