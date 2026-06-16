package com.shipyard.scheduling.controller;

import com.shipyard.common.Result;
import com.shipyard.scheduling.dto.SchedulingRequest;
import com.shipyard.scheduling.entity.BuildSchedule;
import com.shipyard.scheduling.service.SchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping("/generate")
    public Result<Map<String, Object>> generateSchedule(@Valid @RequestBody SchedulingRequest request) {
        Map<String, Object> result = schedulingService.generateSchedule(request);
        Boolean success = (Boolean) result.get("success");
        if (success != null && !success) {
            return Result.fail((String) result.get("message"));
        }
        return Result.success((String) result.get("message"), result);
    }

    @GetMapping("/{scheduleNo}")
    public Result<List<BuildSchedule>> getSchedule(@PathVariable String scheduleNo) {
        List<BuildSchedule> schedules = schedulingService.getByScheduleNo(scheduleNo);
        return Result.success(schedules);
    }
}
