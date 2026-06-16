package com.shipyard.scheduling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("build_schedule")
public class BuildSchedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String scheduleNo;

    private String projectNo;

    private Long sectionId;

    private Long processId;

    private Long resourceId;

    private LocalDateTime planStartTime;

    private LocalDateTime planEndTime;

    private Integer locked;

    private String status;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
