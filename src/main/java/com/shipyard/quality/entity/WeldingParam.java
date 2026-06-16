package com.shipyard.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("welding_param")
public class WeldingParam {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectNo;

    private Long sectionId;

    private Long processId;

    private String welderCode;

    private BigDecimal temperature;

    private BigDecimal currentValue;

    private BigDecimal voltageValue;

    private LocalDateTime reportTime;

    private Integer deleted;

    private LocalDateTime createTime;
}
