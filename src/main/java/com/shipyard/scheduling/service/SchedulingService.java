package com.shipyard.scheduling.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shipyard.scheduling.dto.SchedulingRequest;
import com.shipyard.scheduling.entity.*;
import com.shipyard.scheduling.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final SectionInfoMapper sectionInfoMapper;
    private final WorkProcessMapper workProcessMapper;
    private final WorkshopResourceMapper workshopResourceMapper;
    private final BuildScheduleMapper buildScheduleMapper;
    private final ScheduleConflictMapper scheduleConflictMapper;

    @Transactional
    public Map<String, Object> generateSchedule(SchedulingRequest request) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> scheduleList = new ArrayList<>();
        List<Map<String, Object>> conflictList = new ArrayList<>();

        SectionInfo section = sectionInfoMapper.selectOne(
                new LambdaQueryWrapper<SectionInfo>()
                        .eq(SectionInfo::getProjectNo, request.getProjectNo())
                        .eq(SectionInfo::getSectionCode, request.getSectionCode())
        );
        if (section == null) {
            result.put("success", false);
            result.put("message", "分段不存在: " + request.getSectionCode());
            return result;
        }

        List<WorkProcess> processes;
        if (request.getProcessIds() != null && !request.getProcessIds().isEmpty()) {
            processes = workProcessMapper.selectBatchIds(request.getProcessIds())
                    .stream()
                    .sorted(Comparator.comparingInt(WorkProcess::getSeqNo))
                    .collect(Collectors.toList());
        } else {
            processes = workProcessMapper.selectList(
                    new LambdaQueryWrapper<WorkProcess>()
                            .orderByAsc(WorkProcess::getSeqNo)
            );
        }

        List<WorkshopResource> resources = workshopResourceMapper.selectBatchIds(request.getResourceIds());
        if (resources.isEmpty()) {
            result.put("success", false);
            result.put("message", "指定的车间资源不存在");
            return result;
        }

        Map<Long, List<BuildSchedule>> resourceOccupiedMap = new HashMap<>();
        for (WorkshopResource res : resources) {
            List<BuildSchedule> occupied = buildScheduleMapper.selectList(
                    new LambdaQueryWrapper<BuildSchedule>()
                            .eq(BuildSchedule::getResourceId, res.getId())
                            .eq(BuildSchedule::getLocked, 1)
                            .ge(BuildSchedule::getPlanEndTime, request.getPlanStartTime())
                            .orderByAsc(BuildSchedule::getPlanStartTime)
            );
            resourceOccupiedMap.put(res.getId(), occupied);
        }

        String scheduleNo = "SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime currentTime = request.getPlanStartTime();

        if (request.getMaterialArrivalTime() != null
                && request.getMaterialArrivalTime().isAfter(currentTime)) {
            currentTime = request.getMaterialArrivalTime();
            ScheduleConflict conflict = new ScheduleConflict();
            conflict.setScheduleNo(scheduleNo);
            conflict.setConflictType("MATERIAL_DELAY");
            conflict.setConflictDesc("物料到货时间晚于计划开始时间，需等待物料到货");
            long delayHours = ChronoUnit.HOURS.between(request.getPlanStartTime(), request.getMaterialArrivalTime());
            conflict.setSuggestAdjustCycle((int) delayHours);
            scheduleConflictMapper.insert(conflict);

            Map<String, Object> conflictMap = new HashMap<>();
            conflictMap.put("conflictType", "MATERIAL_DELAY");
            conflictMap.put("conflictDesc", "物料到货时间晚于计划开始时间，需等待物料到货");
            conflictMap.put("suggestAdjustCycle", (int) delayHours);
            conflictMap.put("materialArrivalTime", request.getMaterialArrivalTime());
            conflictList.add(conflictMap);
        }

        int resourceIndex = 0;
        boolean hasConflict = false;

        for (WorkProcess process : processes) {
            WorkshopResource resource = resources.get(resourceIndex % resources.size());
            LocalDateTime startTime = currentTime;
            LocalDateTime endTime = startTime.plusHours(process.getStandardHour());

            List<BuildSchedule> occupiedList = resourceOccupiedMap.get(resource.getId());
            LocalDateTime adjustedStart = findAvailableSlot(startTime, endTime, occupiedList);

            if (!adjustedStart.equals(startTime)) {
                hasConflict = true;
                long adjustHours = ChronoUnit.HOURS.between(startTime, adjustedStart);

                ScheduleConflict conflict = new ScheduleConflict();
                conflict.setScheduleNo(scheduleNo);
                conflict.setConflictType("RESOURCE_CONFLICT");
                conflict.setResourceId(resource.getId());
                conflict.setConflictDesc("资源 " + resource.getResourceName() + " 在该时间段已被占用");
                conflict.setSuggestAdjustCycle((int) adjustHours);
                scheduleConflictMapper.insert(conflict);

                Map<String, Object> conflictMap = new HashMap<>();
                conflictMap.put("conflictType", "RESOURCE_CONFLICT");
                conflictMap.put("resourceCode", resource.getResourceCode());
                conflictMap.put("resourceName", resource.getResourceName());
                conflictMap.put("conflictDesc", "资源 " + resource.getResourceName() + " 在该时间段已被占用");
                conflictMap.put("suggestAdjustCycle", (int) adjustHours);
                conflictMap.put("originalStartTime", startTime);
                conflictMap.put("adjustedStartTime", adjustedStart);
                conflictList.add(conflictMap);

                startTime = adjustedStart;
                endTime = startTime.plusHours(process.getStandardHour());
            }

            BuildSchedule schedule = new BuildSchedule();
            schedule.setScheduleNo(scheduleNo);
            schedule.setProjectNo(request.getProjectNo());
            schedule.setSectionId(section.getId());
            schedule.setProcessId(process.getId());
            schedule.setResourceId(resource.getId());
            schedule.setPlanStartTime(startTime);
            schedule.setPlanEndTime(endTime);
            schedule.setLocked(1);
            schedule.setStatus("SCHEDULED");
            buildScheduleMapper.insert(schedule);

            occupiedList.add(schedule);
            occupiedList.sort(Comparator.comparing(BuildSchedule::getPlanStartTime));

            Map<String, Object> schMap = new HashMap<>();
            schMap.put("scheduleId", schedule.getId());
            schMap.put("processCode", process.getProcessCode());
            schMap.put("processName", process.getProcessName());
            schMap.put("workType", process.getWorkType());
            schMap.put("resourceCode", resource.getResourceCode());
            schMap.put("resourceName", resource.getResourceName());
            schMap.put("resourceType", resource.getResourceType());
            schMap.put("workshop", resource.getWorkshop());
            schMap.put("planStartTime", startTime);
            schMap.put("planEndTime", endTime);
            schMap.put("standardHour", process.getStandardHour());
            schMap.put("locked", true);
            schMap.put("seqNo", process.getSeqNo());
            scheduleList.add(schMap);

            currentTime = endTime;
            resourceIndex++;
        }

        result.put("scheduleNo", scheduleNo);
        result.put("projectNo", request.getProjectNo());
        result.put("sectionCode", request.getSectionCode());
        result.put("sectionName", section.getSectionName());
        result.put("hasConflict", hasConflict);
        result.put("totalProcesses", processes.size());
        result.put("scheduleList", scheduleList);
        result.put("conflictList", conflictList);

        if (hasConflict) {
            result.put("message", "排程完成，存在资源冲突，已自动调整并锁定资源");
        } else {
            result.put("message", "排程完成，无资源冲突");
        }

        return result;
    }

    private LocalDateTime findAvailableSlot(LocalDateTime startTime, LocalDateTime endTime,
                                            List<BuildSchedule> occupiedList) {
        if (occupiedList == null || occupiedList.isEmpty()) {
            return startTime;
        }

        LocalDateTime currentStart = startTime;
        LocalDateTime currentEnd = endTime;
        long duration = ChronoUnit.HOURS.between(startTime, endTime);

        for (BuildSchedule occupied : occupiedList) {
            LocalDateTime occStart = occupied.getPlanStartTime();
            LocalDateTime occEnd = occupied.getPlanEndTime();

            if (currentStart.isBefore(occEnd) && currentEnd.isAfter(occStart)) {
                currentStart = occEnd;
                currentEnd = currentStart.plusHours(duration);
            }
        }

        return currentStart;
    }

    public List<BuildSchedule> getByScheduleNo(String scheduleNo) {
        return buildScheduleMapper.selectList(
                new LambdaQueryWrapper<BuildSchedule>()
                        .eq(BuildSchedule::getScheduleNo, scheduleNo)
                        .orderByAsc(BuildSchedule::getPlanStartTime)
        );
    }
}
