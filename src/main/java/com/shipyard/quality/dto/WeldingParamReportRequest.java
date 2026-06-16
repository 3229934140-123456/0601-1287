package com.shipyard.quality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WeldingParamReportRequest {

    @NotBlank(message = "项目号不能为空")
    private String projectNo;

    @NotBlank(message = "分段编码不能为空")
    private String sectionCode;

    private Long processId;

    @NotBlank(message = "焊工编码不能为空")
    private String welderCode;

    private BigDecimal temperature;

    private BigDecimal currentValue;

    private BigDecimal voltageValue;

    @NotNull(message = "上报时间不能为空")
    private LocalDateTime reportTime;
}
