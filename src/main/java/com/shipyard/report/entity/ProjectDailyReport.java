package com.shipyard.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("project_daily_report")
public class ProjectDailyReport {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectNo;

    private LocalDate reportDate;

    private Integer sectionCount;

    private BigDecimal avgCompletionRate;

    private BigDecimal firstPassRate;

    private BigDecimal equipmentFaultRate;

    private Integer deleted;

    private LocalDateTime createTime;
}
