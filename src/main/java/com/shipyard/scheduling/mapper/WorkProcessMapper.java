package com.shipyard.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shipyard.scheduling.entity.WorkProcess;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkProcessMapper extends BaseMapper<WorkProcess> {
}
