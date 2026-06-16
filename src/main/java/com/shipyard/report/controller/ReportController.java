package com.shipyard.report.controller;

import com.shipyard.common.Result;
import com.shipyard.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public Result<Map<String, Object>> getDailyReport(
            @RequestParam String projectNo,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportDate) {
        Map<String, Object> report = reportService.getProjectDailyReport(projectNo, reportDate);
        return Result.success(report);
    }

    @GetMapping("/sections/completion")
    public Result<List<Map<String, Object>>> getSectionCompletion(@RequestParam String projectNo) {
        List<Map<String, Object>> list = reportService.getSectionCompletionList(projectNo);
        return Result.success(list);
    }
}
