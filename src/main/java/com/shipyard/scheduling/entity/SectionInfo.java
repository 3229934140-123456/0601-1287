package com.shipyard.scheduling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("section_info")
public class SectionInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectNo;

    private String sectionCode;

    private String sectionName;

    private LocalDate planStartDate;

    private LocalDate planEndDate;

    private java.math.BigDecimal completionRate;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
