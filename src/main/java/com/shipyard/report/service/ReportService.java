package com.shipyard.report.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shipyard.report.entity.*;
import com.shipyard.report.mapper.*;
import com.shipyard.scheduling.entity.SectionInfo;
import com.shipyard.scheduling.mapper.SectionInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SectionInfoMapper sectionInfoMapper;
    private final InspectionRecordMapper inspectionRecordMapper;
    private final EquipmentMapper equipmentMapper;
    private final ProjectDailyReportMapper dailyReportMapper;

    public Map<String, Object> getProjectDailyReport(String projectNo, LocalDate reportDate) {
        Map<String, Object> result = new HashMap<>();

        List<SectionInfo> sections = sectionInfoMapper.selectList(
                new LambdaQueryWrapper<SectionInfo>()
                        .eq(SectionInfo::getProjectNo, projectNo)
                        .orderByAsc(SectionInfo::getSectionCode)
        );

        List<Map<String, Object>> sectionStats = new ArrayList<>();
        BigDecimal totalCompletionRate = BigDecimal.ZERO;

        if (!sections.isEmpty()) {
            for (SectionInfo section : sections) {
                Map<String, Object> secStat = new HashMap<>();
                secStat.put("sectionCode", section.getSectionCode());
                secStat.put("sectionName", section.getSectionName());
                secStat.put("completionRate", section.getCompletionRate());
                secStat.put("planStartDate", section.getPlanStartDate());
                secStat.put("planEndDate", section.getPlanEndDate());

                LocalDateTime dayStart = reportDate.atStartOfDay();
                LocalDateTime dayEnd = reportDate.atTime(LocalTime.MAX);

                Long totalInspect = inspectionRecordMapper.selectCount(
                        new LambdaQueryWrapper<InspectionRecord>()
                                .eq(InspectionRecord::getProjectNo, projectNo)
                                .eq(InspectionRecord::getSectionId, section.getId())
                                .ge(InspectionRecord::getInspectTime, dayStart)
                                .le(InspectionRecord::getInspectTime, dayEnd)
                );

                Long firstPassCount = 0L;
                if (totalInspect > 0) {
                    firstPassCount = inspectionRecordMapper.selectCount(
                            new LambdaQueryWrapper<InspectionRecord>()
                                    .eq(InspectionRecord::getProjectNo, projectNo)
                                    .eq(InspectionRecord::getSectionId, section.getId())
                                    .eq(InspectionRecord::getFirstPass, 1)
                                    .ge(InspectionRecord::getInspectTime, dayStart)
                                    .le(InspectionRecord::getInspectTime, dayEnd)
                    );
                }

                BigDecimal firstPassRate = totalInspect > 0
                        ? BigDecimal.valueOf(firstPassCount)
                                .divide(BigDecimal.valueOf(totalInspect), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                secStat.put("totalInspections", totalInspect);
                secStat.put("firstPassCount", firstPassCount);
                secStat.put("firstPassRate", firstPassRate);

                sectionStats.add(secStat);

                if (section.getCompletionRate() != null) {
                    totalCompletionRate = totalCompletionRate.add(section.getCompletionRate());
                }
            }
        }

        List<Equipment> allEquipments = equipmentMapper.selectList(
                new LambdaQueryWrapper<Equipment>()
        );

        int totalEquipment = allEquipments.size();
        long faultEquipment = allEquipments.stream()
                .filter(e -> "FAULT".equals(e.getStatus()))
                .count();

        int totalFaultCount = allEquipments.stream()
                .mapToInt(e -> e.getFaultCount() != null ? e.getFaultCount() : 0)
                .sum();

        BigDecimal equipmentFaultRate = totalEquipment > 0
                ? BigDecimal.valueOf(faultEquipment)
                        .divide(BigDecimal.valueOf(totalEquipment), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal avgCompletionRate = !sections.isEmpty()
                ? totalCompletionRate.divide(BigDecimal.valueOf(sections.size()), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        long totalInspectionsAll = 0;
        long firstPassAll = 0;
        if (!sections.isEmpty()) {
            LocalDateTime dayStart = reportDate.atStartOfDay();
            LocalDateTime dayEnd = reportDate.atTime(LocalTime.MAX);

            totalInspectionsAll = inspectionRecordMapper.selectCount(
                    new LambdaQueryWrapper<InspectionRecord>()
                            .eq(InspectionRecord::getProjectNo, projectNo)
                            .ge(InspectionRecord::getInspectTime, dayStart)
                            .le(InspectionRecord::getInspectTime, dayEnd)
            );

            if (totalInspectionsAll > 0) {
                firstPassAll = inspectionRecordMapper.selectCount(
                        new LambdaQueryWrapper<InspectionRecord>()
                                .eq(InspectionRecord::getProjectNo, projectNo)
                                .eq(InspectionRecord::getFirstPass, 1)
                                .ge(InspectionRecord::getInspectTime, dayStart)
                                .le(InspectionRecord::getInspectTime, dayEnd)
                );
            }
        }

        BigDecimal overallFirstPassRate = totalInspectionsAll > 0
                ? BigDecimal.valueOf(firstPassAll)
                        .divide(BigDecimal.valueOf(totalInspectionsAll), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<Map<String, Object>> equipmentStats = new ArrayList<>();
        for (Equipment eq : allEquipments) {
            Map<String, Object> eqStat = new HashMap<>();
            eqStat.put("equipmentCode", eq.getEquipmentCode());
            eqStat.put("equipmentName", eq.getEquipmentName());
            eqStat.put("workshop", eq.getWorkshop());
            eqStat.put("status", eq.getStatus());
            eqStat.put("faultCount", eq.getFaultCount());
            equipmentStats.add(eqStat);
        }

        result.put("projectNo", projectNo);
        result.put("reportDate", reportDate);
        result.put("hasData", !sections.isEmpty() || !allEquipments.isEmpty());
        result.put("sectionCount", sections.size());
        result.put("avgCompletionRate", avgCompletionRate);
        result.put("overallFirstPassRate", overallFirstPassRate);
        result.put("totalInspections", totalInspectionsAll);
        result.put("firstPassCount", firstPassAll);
        result.put("equipmentCount", totalEquipment);
        result.put("faultEquipmentCount", faultEquipment);
        result.put("totalFaultCount", totalFaultCount);
        result.put("equipmentFaultRate", equipmentFaultRate);
        result.put("sectionStats", sectionStats);
        result.put("equipmentStats", equipmentStats);

        if (sections.isEmpty()) {
            result.put("emptyReason", "该项目下暂无分段数据");
        }

        return result;
    }

    public List<Map<String, Object>> getSectionCompletionList(String projectNo) {
        List<SectionInfo> sections = sectionInfoMapper.selectList(
                new LambdaQueryWrapper<SectionInfo>()
                        .eq(SectionInfo::getProjectNo, projectNo)
                        .orderByAsc(SectionInfo::getSectionCode)
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (SectionInfo section : sections) {
            Map<String, Object> map = new HashMap<>();
            map.put("sectionCode", section.getSectionCode());
            map.put("sectionName", section.getSectionName());
            map.put("completionRate", section.getCompletionRate());
            map.put("planStartDate", section.getPlanStartDate());
            map.put("planEndDate", section.getPlanEndDate());
            result.add(map);
        }
        return result;
    }
}
