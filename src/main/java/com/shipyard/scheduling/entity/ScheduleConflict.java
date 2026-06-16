package com.shipyard.scheduling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("schedule_conflict")
public class ScheduleConflict {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String scheduleNo;

    private String conflictType;

    private Long resourceId;

    private String conflictDesc;

    private Integer suggestAdjustCycle;

    private Integer deleted;

    private LocalDateTime createTime;
}
