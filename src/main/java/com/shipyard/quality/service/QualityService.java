package com.shipyard.quality.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shipyard.quality.dto.WeldingParamReportRequest;
import com.shipyard.quality.entity.*;
import com.shipyard.quality.mapper.*;
import com.shipyard.scheduling.entity.SectionInfo;
import com.shipyard.scheduling.mapper.SectionInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QualityService {

    private final WeldingParamMapper weldingParamMapper;
    private final QualityWarningMapper qualityWarningMapper;
    private final ProcessThresholdMapper processThresholdMapper;
    private final WelderSkillMapper welderSkillMapper;
    private final SectionInfoMapper sectionInfoMapper;

    @Transactional
    public Map<String, Object> reportWeldingParam(WeldingParamReportRequest request) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> warnings = new ArrayList<>();

        SectionInfo section = sectionInfoMapper.selectOne(
                new LambdaQueryWrapper<SectionInfo>()
                        .eq(SectionInfo::getProjectNo, request.getProjectNo())
                        .eq(SectionInfo::getSectionCode, request.getSectionCode())
        );
        if (section == null) {
            result.put("success", false);
            result.put("message", "分段不存在");
            return result;
        }

        Long processId = request.getProcessId();
        if (processId == null) {
            processId = 3L;
        }

        WeldingParam param = new WeldingParam();
        param.setProjectNo(request.getProjectNo());
        param.setSectionId(section.getId());
        param.setProcessId(processId);
        param.setWelderCode(request.getWelderCode());
        param.setTemperature(request.getTemperature());
        param.setCurrentValue(request.getCurrentValue());
        param.setVoltageValue(request.getVoltageValue());
        param.setReportTime(request.getReportTime());
        weldingParamMapper.insert(param);

        List<ProcessThreshold> thresholds = processThresholdMapper.selectList(
                new LambdaQueryWrapper<ProcessThreshold>()
                        .eq(ProcessThreshold::getProcessId, processId)
        );

        String mostSevereLevel = null;
        for (ProcessThreshold threshold : thresholds) {
            BigDecimal actualValue = getParamValue(request, threshold.getParamType());
            if (actualValue == null) {
                continue;
            }

            boolean outOfRange = false;
            if (threshold.getMinValue() != null && actualValue.compareTo(threshold.getMinValue()) < 0) {
                outOfRange = true;
            }
            if (threshold.getMaxValue() != null && actualValue.compareTo(threshold.getMaxValue()) > 0) {
                outOfRange = true;
            }

            if (outOfRange) {
                if (mostSevereLevel == null || isMoreSevere(threshold.getSeverityLevel(), mostSevereLevel)) {
                    mostSevereLevel = threshold.getSeverityLevel();
                }

                String warningNo = "WARN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                WelderSkill inspector = findSuitableInspector(section.getId(), threshold.getParamType());

                int deadlineHours = "SERIOUS".equals(threshold.getSeverityLevel()) ? 4 : 24;
                LocalDateTime deadline = request.getReportTime().plusHours(deadlineHours);

                QualityWarning warning = new QualityWarning();
                warning.setWarningNo(warningNo);
                warning.setProjectNo(request.getProjectNo());
                warning.setSectionId(section.getId());
                warning.setProcessId(processId);
                warning.setWeldingParamId(param.getId());
                warning.setWarningType("PARAM_OUT_OF_RANGE");
                warning.setSeverityLevel(threshold.getSeverityLevel());
                warning.setParamType(threshold.getParamType());
                warning.setActualValue(actualValue);
                warning.setThresholdMin(threshold.getMinValue());
                warning.setThresholdMax(threshold.getMaxValue());
                warning.setStatus("PENDING_INSPECTION");
                warning.setDeadline(deadline);
                warning.setEscalated(0);

                if (inspector != null) {
                    warning.setInspectorCode(inspector.getWelderCode());
                    warning.setInspectorName(inspector.getWelderName());
                }

                qualityWarningMapper.insert(warning);

                Map<String, Object> warnMap = new HashMap<>();
                warnMap.put("warningNo", warningNo);
                warnMap.put("paramType", threshold.getParamType());
                warnMap.put("actualValue", actualValue);
                warnMap.put("thresholdMin", threshold.getMinValue());
                warnMap.put("thresholdMax", threshold.getMaxValue());
                warnMap.put("severityLevel", threshold.getSeverityLevel());
                warnMap.put("inspectorCode", warning.getInspectorCode());
                warnMap.put("inspectorName", warning.getInspectorName());
                warnMap.put("deadline", deadline);
                warnMap.put("status", "PENDING_INSPECTION");
                warnings.add(warnMap);
            }
        }

        result.put("paramId", param.getId());
        result.put("hasWarning", !warnings.isEmpty());
        result.put("warningCount", warnings.size());
        result.put("warnings", warnings);

        if (warnings.isEmpty()) {
            result.put("message", "参数上报成功，参数在工艺阈值范围内");
        } else {
            result.put("message", "参数上报成功，检测到 " + warnings.size() + " 项参数超出阈值，已生成质量预警工单");
        }

        return result;
    }

    private BigDecimal getParamValue(WeldingParamReportRequest request, String paramType) {
        return switch (paramType) {
            case "temperature" -> request.getTemperature();
            case "current" -> request.getCurrentValue();
            case "voltage" -> request.getVoltageValue();
            default -> null;
        };
    }

    private boolean isMoreSevere(String level1, String level2) {
        int rank1 = getSeverityRank(level1);
        int rank2 = getSeverityRank(level2);
        return rank1 > rank2;
    }

    private int getSeverityRank(String level) {
        return switch (level) {
            case "SERIOUS" -> 2;
            case "WARNING" -> 1;
            default -> 0;
        };
    }

    private WelderSkill findSuitableInspector(Long sectionId, String paramType) {
        List<WelderSkill> inspectors = welderSkillMapper.selectList(
                new LambdaQueryWrapper<WelderSkill>()
                        .eq(WelderSkill::getCanInspect, 1)
                        .orderByAsc(WelderSkill::getId)
        );

        if (inspectors.isEmpty()) {
            return null;
        }

        int totalInspectors = inspectors.size();
        int index = (int) (sectionId % totalInspectors);
        return inspectors.get(index);
    }

    public QualityWarning getWarningByNo(String warningNo) {
        return qualityWarningMapper.selectOne(
                new LambdaQueryWrapper<QualityWarning>()
                        .eq(QualityWarning::getWarningNo, warningNo)
        );
    }

    public List<QualityWarning> getWarningsByProject(String projectNo) {
        return qualityWarningMapper.selectList(
                new LambdaQueryWrapper<QualityWarning>()
                        .eq(QualityWarning::getProjectNo, projectNo)
                        .orderByDesc(QualityWarning::getCreateTime)
        );
    }

    public List<QualityWarning> getWarningsBySection(String projectNo, String sectionCode) {
        SectionInfo section = sectionInfoMapper.selectOne(
                new LambdaQueryWrapper<SectionInfo>()
                        .eq(SectionInfo::getProjectNo, projectNo)
                        .eq(SectionInfo::getSectionCode, sectionCode)
        );
        if (section == null) {
            return Collections.emptyList();
        }
        return qualityWarningMapper.selectList(
                new LambdaQueryWrapper<QualityWarning>()
                        .eq(QualityWarning::getProjectNo, projectNo)
                        .eq(QualityWarning::getSectionId, section.getId())
                        .orderByDesc(QualityWarning::getCreateTime)
        );
    }
}
