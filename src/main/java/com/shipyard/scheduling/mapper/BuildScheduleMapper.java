package com.shipyard.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shipyard.scheduling.entity.BuildSchedule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuildScheduleMapper extends BaseMapper<BuildSchedule> {
}
