package com.shipyard.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("equipment")
public class Equipment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String equipmentCode;

    private String equipmentName;

    private String workshop;

    private String status;

    private Integer faultCount;

    private Integer deleted;

    private LocalDateTime createTime;
}
