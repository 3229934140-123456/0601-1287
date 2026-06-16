package com.shipyard.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_in_apply")
public class StockInApply {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applyNo;

    private Long materialId;

    private Long supplierId;

    private Integer applyQuantity;

    private Integer recommendQuantity;

    private String certificateNo;

    private LocalDate certificateValidDate;

    private String status;

    private String rejectReason;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
