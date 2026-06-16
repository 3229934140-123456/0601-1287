package com.shipyard.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shipyard.scheduling.entity.ScheduleConflict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleConflictMapper extends BaseMapper<ScheduleConflict> {
}
