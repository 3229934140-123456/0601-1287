package com.shipyard.scheduling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("work_process")
public class WorkProcess {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String processCode;

    private String processName;

    private String workType;

    private Integer standardHour;

    private Integer seqNo;

    private Integer deleted;

    private LocalDateTime createTime;
}
