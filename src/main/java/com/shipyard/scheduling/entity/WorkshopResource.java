package com.shipyard.scheduling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("workshop_resource")
public class WorkshopResource {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String workshop;

    private Integer capacity;

    private Integer deleted;

    private LocalDateTime createTime;
}
