package com.shipyard.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("process_threshold")
public class ProcessThreshold {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long processId;

    private String paramType;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private String severityLevel;

    private Integer deleted;

    private LocalDateTime createTime;
}
