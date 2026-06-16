package com.shipyard.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("welder_skill")
public class WelderSkill {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String welderCode;

    private String welderName;

    private String skillLevel;

    private Integer canInspect;

    private String processTypes;

    private Integer deleted;

    private LocalDateTime createTime;
}
