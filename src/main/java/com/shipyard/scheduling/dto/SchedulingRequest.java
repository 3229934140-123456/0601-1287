package com.shipyard.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SchedulingRequest {

    @NotBlank(message = "项目号不能为空")
    private String projectNo;

    @NotBlank(message = "分段编码不能为空")
    private String sectionCode;

    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime planStartTime;

    @NotEmpty(message = "车间资源列表不能为空")
    private List<Long> resourceIds;

    private LocalDateTime materialArrivalTime;

    private List<Long> processIds;
}
