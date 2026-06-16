package com.shipyard.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("quality_warning")
public class QualityWarning {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String warningNo;

    private String projectNo;

    private Long sectionId;

    private Long processId;

    private Long weldingParamId;

    private String warningType;

    private String severityLevel;

    private String paramType;

    private BigDecimal actualValue;

    private BigDecimal thresholdMin;

    private BigDecimal thresholdMax;

    private String status;

    private String inspectorCode;

    private String inspectorName;

    private LocalDateTime deadline;

    private Integer escalated;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
