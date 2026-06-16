package com.shipyard.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inspection_record")
public class InspectionRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectNo;

    private Long sectionId;

    private Long processId;

    private LocalDateTime inspectTime;

    private Integer firstPass;

    private String inspector;

    private Integer deleted;

    private LocalDateTime createTime;
}
