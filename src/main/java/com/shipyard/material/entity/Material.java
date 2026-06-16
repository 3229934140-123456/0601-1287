package com.shipyard.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material")
public class Material {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String materialCode;

    private String materialName;

    private String materialType;

    private Integer stockLimit;

    private Integer currentStock;

    private Integer requireCertificate;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
