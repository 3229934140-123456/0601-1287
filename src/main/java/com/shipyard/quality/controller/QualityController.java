package com.shipyard.quality.controller;

import com.shipyard.common.Result;
import com.shipyard.quality.dto.WeldingParamReportRequest;
import com.shipyard.quality.entity.QualityWarning;
import com.shipyard.quality.service.QualityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityService qualityService;

    @PostMapping("/welding/report")
    public Result<Map<String, Object>> reportWeldingParam(@Valid @RequestBody WeldingParamReportRequest request) {
        Map<String, Object> result = qualityService.reportWeldingParam(request);
        Boolean success = (Boolean) result.get("success");
        if (success != null && !success) {
            return Result.fail((String) result.get("message"));
        }
        return Result.success((String) result.get("message"), result);
    }

    @GetMapping("/warning/{warningNo}")
    public Result<QualityWarning> getWarning(@PathVariable String warningNo) {
        QualityWarning warning = qualityService.getWarningByNo(warningNo);
        if (warning == null) {
            return Result.fail("质量预警工单不存在");
        }
        return Result.success(warning);
    }

    @GetMapping("/warnings")
    public Result<List<QualityWarning>> listWarnings(
            @RequestParam String projectNo,
            @RequestParam(required = false) String sectionCode) {
        List<QualityWarning> warnings;
        if (sectionCode != null && !sectionCode.isEmpty()) {
            warnings = qualityService.getWarningsBySection(projectNo, sectionCode);
        } else {
            warnings = qualityService.getWarningsByProject(projectNo);
        }
        return Result.success(warnings);
    }
}
