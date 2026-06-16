const { v4: uuidv4 } = require('uuid');

const materials = [
  { id: 1, material_code: 'STEEL-001', material_name: '船用钢板DH36', material_type: '钢材', stock_limit: 500, current_stock: 120, require_certificate: true },
  { id: 2, material_code: 'STEEL-002', material_name: '船用钢板EH36', material_type: '钢材', stock_limit: 300, current_stock: 280, require_certificate: true },
  { id: 3, material_code: 'PIPE-001', material_name: '不锈钢管DN100', material_type: '管材', stock_limit: 1000, current_stock: 350, require_certificate: true },
  { id: 4, material_code: 'WELD-001', material_name: '焊丝E71T-1', material_type: '焊材', stock_limit: 2000, current_stock: 800, require_certificate: false },
  { id: 5, material_code: 'COAT-001', material_name: '环氧防腐漆', material_type: '涂料', stock_limit: 500, current_stock: 490, require_certificate: true },
];

const suppliers = [
  { id: 1, supplier_code: 'SUP-001', supplier_name: '上海宝钢船舶材料有限公司', qualification_valid_date: '2027-06-30', qualification_level: 'A级' },
  { id: 2, supplier_code: 'SUP-002', supplier_name: '鞍钢股份有限公司', qualification_valid_date: '2025-03-15', qualification_level: 'A级' },
  { id: 3, supplier_code: 'SUP-003', supplier_name: '天津钢管集团', qualification_valid_date: '2026-12-31', qualification_level: 'B级' },
  { id: 4, supplier_code: 'SUP-004', supplier_name: '大西洋焊材', qualification_valid_date: '2025-01-10', qualification_level: 'B级' },
  { id: 5, supplier_code: 'SUP-005', supplier_name: '中远关西涂料', qualification_valid_date: '2028-01-20', qualification_level: 'A级' },
];

const stockInApplies = [];

const sections = [
  { id: 1, project_no: 'PROJ-2024-001', section_code: 'SEC-101', section_name: '上层建筑分段101', plan_start_date: '2024-01-15', plan_end_date: '2024-06-30', completion_rate: 65.50 },
  { id: 2, project_no: 'PROJ-2024-001', section_code: 'SEC-102', section_name: '货舱分段102', plan_start_date: '2024-02-01', plan_end_date: '2024-07-15', completion_rate: 42.00 },
  { id: 3, project_no: 'PROJ-2024-001', section_code: 'SEC-103', section_name: '机舱分段103', plan_start_date: '2024-03-10', plan_end_date: '2024-08-20', completion_rate: 28.30 },
  { id: 4, project_no: 'PROJ-2024-002', section_code: 'SEC-201', section_name: '首部分段201', plan_start_date: '2024-04-01', plan_end_date: '2024-09-30', completion_rate: 15.00 },
  { id: 5, project_no: 'PROJ-2024-002', section_code: 'SEC-202', section_name: '尾部分段202', plan_start_date: '2024-05-01', plan_end_date: '2024-10-31', completion_rate: 5.00 },
];

const workProcesses = [
  { id: 1, process_code: 'PROC-001', process_name: '下料切割', work_type: '切割', standard_hour: 16, seq_no: 1 },
  { id: 2, process_code: 'PROC-002', process_name: '部件装配', work_type: '装配', standard_hour: 24, seq_no: 2 },
  { id: 3, process_code: 'PROC-003', process_name: '焊接', work_type: '焊接', standard_hour: 40, seq_no: 3 },
  { id: 4, process_code: 'PROC-004', process_name: 'X光探伤', work_type: '检测', standard_hour: 8, seq_no: 4 },
  { id: 5, process_code: 'PROC-005', process_name: '除锈涂装', work_type: '涂装', standard_hour: 12, seq_no: 5 },
];

const workshopResources = [
  { id: 1, resource_code: 'RES-C01', resource_name: '数控切割机1号', resource_type: '设备', workshop: '下料车间', capacity: 1, locked: false },
  { id: 2, resource_code: 'RES-C02', resource_name: '数控切割机2号', resource_type: '设备', workshop: '下料车间', capacity: 1, locked: false },
  { id: 3, resource_code: 'RES-A01', resource_name: '装配工位A1', resource_type: '工位', workshop: '装焊车间', capacity: 1, locked: false },
  { id: 4, resource_code: 'RES-A02', resource_name: '装配工位A2', resource_type: '工位', workshop: '装焊车间', capacity: 1, locked: false },
  { id: 5, resource_code: 'RES-W01', resource_name: '焊接工位W1', resource_type: '工位', workshop: '装焊车间', capacity: 1, locked: false },
  { id: 6, resource_code: 'RES-W02', resource_name: '焊接工位W2', resource_type: '工位', workshop: '装焊车间', capacity: 1, locked: false },
  { id: 7, resource_code: 'RES-X01', resource_name: 'X光探伤室', resource_type: '设备', workshop: '检测中心', capacity: 1, locked: false },
  { id: 8, resource_code: 'RES-P01', resource_name: '涂装房1号', resource_type: '设备', workshop: '涂装车间', capacity: 1, locked: false },
];

const buildSchedules = [];
const scheduleConflicts = [];

const processThresholds = [
  { id: 1, process_id: 3, param_type: 'temperature', min_value: 100, max_value: 250, severity_level: 'WARNING' },
  { id: 2, process_id: 3, param_type: 'current', min_value: 180, max_value: 280, severity_level: 'WARNING' },
  { id: 3, process_id: 3, param_type: 'voltage', min_value: 24, max_value: 32, severity_level: 'WARNING' },
  { id: 4, process_id: 3, param_type: 'temperature', min_value: 80, max_value: 300, severity_level: 'SERIOUS' },
  { id: 5, process_id: 3, param_type: 'current', min_value: 150, max_value: 320, severity_level: 'SERIOUS' },
  { id: 6, process_id: 3, param_type: 'voltage', min_value: 20, max_value: 36, severity_level: 'SERIOUS' },
];

const welderSkills = [
  { id: 1, welder_code: 'W-001', welder_name: '张师傅', skill_level: '高级焊工', can_inspect: true, process_types: ['PROC-003'] },
  { id: 2, welder_code: 'W-002', welder_name: '李师傅', skill_level: '高级焊工', can_inspect: true, process_types: ['PROC-003'] },
  { id: 3, welder_code: 'W-003', welder_name: '王师傅', skill_level: '中级焊工', can_inspect: false, process_types: ['PROC-003'] },
  { id: 4, welder_code: 'W-004', welder_name: '赵师傅', skill_level: '中级焊工', can_inspect: false, process_types: ['PROC-003'] },
  { id: 5, welder_code: 'W-005', welder_name: '钱师傅', skill_level: '焊工技师', can_inspect: true, process_types: ['PROC-003', 'PROC-004'] },
];

const weldingParams = [];
const qualityWarnings = [];

const equipments = [
  { id: 1, equipment_code: 'EQ-001', equipment_name: '数控切割机1号', workshop: '下料车间', status: 'RUNNING', fault_count: 1 },
  { id: 2, equipment_code: 'EQ-002', equipment_name: '数控切割机2号', workshop: '下料车间', status: 'RUNNING', fault_count: 0 },
  { id: 3, equipment_code: 'EQ-003', equipment_name: '焊接机器人1号', workshop: '装焊车间', status: 'RUNNING', fault_count: 2 },
  { id: 4, equipment_code: 'EQ-004', equipment_name: '焊接机器人2号', workshop: '装焊车间', status: 'FAULT', fault_count: 3 },
  { id: 5, equipment_code: 'EQ-005', equipment_name: 'X光探伤机', workshop: '检测中心', status: 'RUNNING', fault_count: 0 },
  { id: 6, equipment_code: 'EQ-006', equipment_name: '喷漆机器人', workshop: '涂装车间', status: 'RUNNING', fault_count: 1 },
];

const inspectionRecords = [
  { id: 1, project_no: 'PROJ-2024-001', section_id: 1, process_id: 1, inspect_time: '2024-06-10T09:30:00', first_pass: true, inspector: '质检甲' },
  { id: 2, project_no: 'PROJ-2024-001', section_id: 1, process_id: 2, inspect_time: '2024-06-12T14:20:00', first_pass: true, inspector: '质检甲' },
  { id: 3, project_no: 'PROJ-2024-001', section_id: 1, process_id: 3, inspect_time: '2024-06-14T10:15:00', first_pass: false, inspector: '质检乙' },
  { id: 4, project_no: 'PROJ-2024-001', section_id: 1, process_id: 3, inspect_time: '2024-06-15T11:00:00', first_pass: true, inspector: '质检乙' },
  { id: 5, project_no: 'PROJ-2024-001', section_id: 2, process_id: 1, inspect_time: '2024-06-11T08:45:00', first_pass: true, inspector: '质检甲' },
  { id: 6, project_no: 'PROJ-2024-001', section_id: 2, process_id: 2, inspect_time: '2024-06-13T15:30:00', first_pass: true, inspector: '质检丙' },
  { id: 7, project_no: 'PROJ-2024-002', section_id: 4, process_id: 1, inspect_time: '2024-06-09T10:00:00', first_pass: false, inspector: '质检乙' },
  { id: 8, project_no: 'PROJ-2024-002', section_id: 4, process_id: 1, inspect_time: '2024-06-10T09:00:00', first_pass: true, inspector: '质检乙' },
];

function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).substr(2, 5);
}

function generateApplyNo() {
  return 'APPLY-' + uuidv4().substring(0, 8).toUpperCase();
}

function generateScheduleNo() {
  return 'SCH-' + uuidv4().substring(0, 8).toUpperCase();
}

function generateWarningNo() {
  return 'WARN-' + uuidv4().substring(0, 8).toUpperCase();
}

function ok(data = null, message = 'success') {
  return { code: 200, message, data };
}

function fail(message = '操作失败', code = 400, data = null) {
  return { code, message, data };
}

const express = require('express');
const app = express();

app.use(express.json());

app.get('/api/health', (req, res) => {
  res.json(ok({
    status: 'UP',
    service: 'shipyard-platform',
    timestamp: new Date().toISOString(),
  }));
});

app.post('/api/material/stock-in/apply', (req, res) => {
  const {
    material_code, material_type, material_name,
    supplier_code, supplier_name, qualification_valid_date, qualification_level,
    apply_quantity, certificate_no, certificate_valid_date,
  } = req.body;

  const fieldErrors = [];
  if (!apply_quantity || apply_quantity <= 0) fieldErrors.push('apply_quantity: 申请数量必须大于0');
  if (!material_code && !material_type) fieldErrors.push('material_code 或 material_type: 至少提供一个来标识物料');
  if (!supplier_code && !supplier_name) fieldErrors.push('supplier_code 或 supplier_name: 至少提供一个来标识供应商');

  if (fieldErrors.length > 0) {
    return res.json(fail(`字段校验失败: ${fieldErrors.join('; ')}`));
  }

  let material;
  if (material_code) {
    material = materials.find(m => m.material_code === material_code);
    if (!material) return res.json(fail(`物料编码不存在: ${material_code}`));
  } else {
    const candidates = materials.filter(m => m.material_type === material_type);
    if (candidates.length === 0) return res.json(fail(`物料类型不存在: ${material_type}`));
    if (material_name) {
      material = candidates.find(m => m.material_name === material_name);
      if (!material) return res.json(fail(`未找到类型为"${material_type}"、名称为"${material_name}"的物料`));
    } else {
      material = candidates[0];
    }
  }

  let supplier;
  if (supplier_code) {
    supplier = suppliers.find(s => s.supplier_code === supplier_code);
    if (!supplier) return res.json(fail(`供应商编码不存在: ${supplier_code}`));
  } else {
    const candidates = suppliers.filter(s => s.supplier_name === supplier_name);
    if (candidates.length === 0) return res.json(fail(`供应商名称不存在: ${supplier_name}`));
    if (qualification_valid_date) {
      supplier = candidates.find(s => s.qualification_valid_date === qualification_valid_date);
      if (!supplier) supplier = candidates[0];
    } else {
      supplier = candidates[0];
    }
  }

  if (qualification_valid_date) {
    supplier.qualification_valid_date = qualification_valid_date;
  }
  if (qualification_level) {
    supplier.qualification_level = qualification_level;
  }

  const rejectReasons = [];
  const today = new Date().toISOString().split('T')[0];

  if (supplier.qualification_valid_date < today) {
    rejectReasons.push(`供应商资质已过期，有效期至: ${supplier.qualification_valid_date}`);
  }

  if (material.require_certificate) {
    if (!certificate_no) {
      rejectReasons.push('该物料需要质保证书，但未提供质保证书编号');
    } else if (certificate_valid_date && certificate_valid_date < today) {
      rejectReasons.push(`质保证书已过期，有效期至: ${certificate_valid_date}`);
    }
  }

  const availableStock = material.stock_limit - material.current_stock;

  if (apply_quantity > availableStock) {
    const excess = apply_quantity - availableStock;
    rejectReasons.push(
      `申请量超出剩余库存: 申请${apply_quantity}, 剩余可入${availableStock}, 当前库存${material.current_stock}, 上限${material.stock_limit}, 超出${excess}`
    );
  }

  const applyNo = generateApplyNo();
  const applyRecord = {
    id: generateId(),
    apply_no: applyNo,
    material_id: material.id,
    material_code: material.material_code,
    material_name: material.material_name,
    material_type: material.material_type,
    supplier_id: supplier.id,
    supplier_code: supplier.supplier_code,
    supplier_name: supplier.supplier_name,
    apply_quantity,
    recommend_quantity: rejectReasons.length > 0 ? 0 : apply_quantity,
    certificate_no: certificate_no || null,
    certificate_valid_date: certificate_valid_date || null,
    status: rejectReasons.length > 0 ? 'REJECTED' : 'APPROVED',
    reject_reason: rejectReasons.length > 0 ? rejectReasons.join('; ') : null,
    current_stock: material.current_stock,
    stock_limit: material.stock_limit,
    available_stock: availableStock,
    create_time: new Date().toISOString(),
  };

  stockInApplies.push(applyRecord);

  if (rejectReasons.length > 0) {
    return res.json(fail(rejectReasons.join('; '), 400, applyRecord));
  }

  material.current_stock += apply_quantity;

  return res.json(ok(applyRecord, '入库申请审核通过'));
});

app.get('/api/material/stock-in/apply/:applyNo', (req, res) => {
  const record = stockInApplies.find(a => a.apply_no === req.params.applyNo);
  if (!record) {
    return res.json(fail('入库申请单不存在'));
  }
  return res.json(ok(record));
});

app.post('/api/scheduling/generate', (req, res) => {
  const { project_no, section_code, plan_start_time, resource_ids, material_arrival_time, process_ids, equipment_occupied } = req.body;

  if (!project_no || !section_code || !plan_start_time || !resource_ids || resource_ids.length === 0) {
    return res.json(fail('项目号、分段编码、计划开始时间和车间资源列表不能为空'));
  }

  const section = sections.find(s => s.project_no === project_no && s.section_code === section_code);
  if (!section) {
    return res.json(fail(`分段不存在: 项目号=${project_no}, 分段=${section_code}`));
  }

  let processes;
  if (process_ids && process_ids.length > 0) {
    processes = workProcesses.filter(p => process_ids.includes(p.id)).sort((a, b) => a.seq_no - b.seq_no);
  } else {
    processes = [...workProcesses].sort((a, b) => a.seq_no - b.seq_no);
  }

  const requestedResources = workshopResources.filter(r => resource_ids.includes(r.id));
  if (requestedResources.length === 0) {
    return res.json(fail('指定的车间资源不存在'));
  }

  const resourceOccupied = {};
  for (const r of requestedResources) {
    resourceOccupied[r.id] = buildSchedules
      .filter(s => s.resource_id === r.id && s.locked)
      .sort((a, b) => new Date(a.plan_start_time) - new Date(b.plan_start_time));
  }

  if (equipment_occupied && Array.isArray(equipment_occupied)) {
    for (const occ of equipment_occupied) {
      const rid = occ.resource_id;
      if (resourceOccupied[rid]) {
        resourceOccupied[rid].push({
          resource_id: rid,
          plan_start_time: occ.start_time,
          plan_end_time: occ.end_time,
          source: 'external',
          occupied_reason: occ.reason || '外部占用',
        });
        resourceOccupied[rid].sort((a, b) => new Date(a.plan_start_time) - new Date(b.plan_start_time));
      }
    }
  }

  const scheduleNo = generateScheduleNo();
  const conflicts = [];
  const scheduleList = [];
  let hasConflict = false;

  let currentTime = new Date(plan_start_time);

  if (material_arrival_time) {
    const matTime = new Date(material_arrival_time);
    if (matTime > currentTime) {
      hasConflict = true;
      const delayHours = Math.round((matTime - currentTime) / 3600000);
      conflicts.push({
        conflict_type: 'MATERIAL_DELAY',
        conflict_desc: '物料到货时间晚于计划开始时间，需等待物料到货',
        suggest_adjust_cycle: delayHours,
        material_arrival_time: material_arrival_time,
        original_start_time: plan_start_time,
      });
      currentTime = matTime;
    }
  }

  function findAvailableSlot(start, end, occupied) {
    let currentStart = new Date(start);
    const duration = end - start;
    for (const occ of occupied) {
      const occStart = new Date(occ.plan_start_time);
      const occEnd = new Date(occ.plan_end_time);
      if (currentStart < occEnd && new Date(currentStart.getTime() + duration) > occStart) {
        currentStart = new Date(occEnd);
      }
    }
    return currentStart;
  }

  let resourceIndex = 0;
  for (const proc of processes) {
    const resource = requestedResources[resourceIndex % requestedResources.length];
    let startTime = new Date(currentTime);
    let endTime = new Date(startTime.getTime() + proc.standard_hour * 3600000);

    const occupiedList = resourceOccupied[resource.id];
    const adjustedStart = findAvailableSlot(startTime, endTime, occupiedList);

    if (adjustedStart.getTime() !== startTime.getTime()) {
      hasConflict = true;
      const adjustHours = Math.round((adjustedStart - startTime) / 3600000);
      conflicts.push({
        conflict_type: 'RESOURCE_CONFLICT',
        resource_code: resource.resource_code,
        resource_name: resource.resource_name,
        conflict_desc: `资源 ${resource.resource_name} 在该时间段已被占用`,
        suggest_adjust_cycle: adjustHours,
        original_start_time: startTime.toISOString(),
        adjusted_start_time: adjustedStart.toISOString(),
      });
      startTime = adjustedStart;
      endTime = new Date(startTime.getTime() + proc.standard_hour * 3600000);
    }

    const schRecord = {
      id: generateId(),
      schedule_no: scheduleNo,
      project_no,
      section_id: section.id,
      section_code: section.section_code,
      process_id: proc.id,
      process_code: proc.process_code,
      process_name: proc.process_name,
      resource_id: resource.id,
      resource_code: resource.resource_code,
      resource_name: resource.resource_name,
      resource_type: resource.resource_type,
      workshop: resource.workshop,
      plan_start_time: startTime.toISOString(),
      plan_end_time: endTime.toISOString(),
      locked: true,
      status: 'SCHEDULED',
    };
    buildSchedules.push(schRecord);
    resourceOccupied[resource.id].push(schRecord);
    resourceOccupied[resource.id].sort((a, b) => new Date(a.plan_start_time) - new Date(b.plan_start_time));

    resource.locked = true;

    scheduleList.push({
      schedule_id: schRecord.id,
      seq_no: proc.seq_no,
      process_code: proc.process_code,
      process_name: proc.process_name,
      work_type: proc.work_type,
      standard_hour: proc.standard_hour,
      resource_code: resource.resource_code,
      resource_name: resource.resource_name,
      resource_type: resource.resource_type,
      workshop: resource.workshop,
      plan_start_time: startTime.toISOString(),
      plan_end_time: endTime.toISOString(),
      locked: true,
    });

    currentTime = endTime;
    resourceIndex++;
  }

  const lockedResources = requestedResources
    .filter(r => r.locked)
    .map(r => {
      const entry = {
        resource_id: r.id,
        resource_code: r.resource_code,
        resource_name: r.resource_name,
        resource_type: r.resource_type,
        workshop: r.workshop,
      };
      const externalOcc = (equipment_occupied || []).find(o => o.resource_id === r.id);
      if (externalOcc) {
        entry.occupied = true;
        entry.occupied_time_range = `${externalOcc.start_time} ~ ${externalOcc.end_time}`;
        entry.occupied_reason = externalOcc.reason || '外部占用';
      }
      return entry;
    });

  const message = hasConflict
    ? '排程完成，存在资源冲突，已自动调整并锁定资源'
    : '排程完成，无资源冲突';

  return res.json(ok({
    schedule_no: scheduleNo,
    project_no,
    section_code,
    section_name: section.section_name,
    has_conflict: hasConflict,
    total_processes: processes.length,
    schedule_list: scheduleList,
    conflict_list: conflicts,
    locked_resources: lockedResources,
  }, message));
});

app.get('/api/scheduling/:scheduleNo', (req, res) => {
  const schedules = buildSchedules.filter(s => s.schedule_no === req.params.scheduleNo);
  if (schedules.length === 0) {
    return res.json(fail('排程记录不存在'));
  }
  return res.json(ok(schedules.map(s => ({
    id: s.id,
    schedule_no: s.schedule_no,
    process_code: s.process_code,
    process_name: s.process_name,
    resource_code: s.resource_code,
    resource_name: s.resource_name,
    plan_start_time: s.plan_start_time,
    plan_end_time: s.plan_end_time,
    locked: s.locked,
    status: s.status,
  }))));
});

app.post('/api/quality/welding/report', (req, res) => {
  const { project_no, section_code, process_id, welder_code, temperature, current_value, voltage_value, report_time } = req.body;

  if (!project_no || !section_code || !welder_code || !report_time) {
    return res.json(fail('项目号、分段编码、焊工编码和上报时间不能为空'));
  }

  const section = sections.find(s => s.project_no === project_no && s.section_code === section_code);
  if (!section) {
    return res.json(fail(`分段不存在: 项目号=${project_no}, 分段=${section_code}`));
  }

  const procId = process_id || 3;

  const paramRecord = {
    id: generateId(),
    project_no,
    section_id: section.id,
    section_code: section.section_code,
    process_id: procId,
    welder_code,
    temperature: temperature != null ? Number(temperature) : null,
    current_value: current_value != null ? Number(current_value) : null,
    voltage_value: voltage_value != null ? Number(voltage_value) : null,
    report_time,
    create_time: new Date().toISOString(),
  };
  weldingParams.push(paramRecord);

  const thresholds = processThresholds.filter(t => t.process_id === procId);

  const paramValueMap = {
    temperature: paramRecord.temperature,
    current: paramRecord.current_value,
    voltage: paramRecord.voltage_value,
  };

  const warnings = [];

  for (const threshold of thresholds) {
    const actualValue = paramValueMap[threshold.param_type];
    if (actualValue == null) continue;

    let outOfRange = false;
    if (threshold.min_value != null && actualValue < threshold.min_value) outOfRange = true;
    if (threshold.max_value != null && actualValue > threshold.max_value) outOfRange = true;

    if (!outOfRange) continue;

    const warningNo = generateWarningNo();
    const inspectors = welderSkills.filter(w => w.can_inspect);
    const inspector = inspectors.length > 0 ? inspectors[section.id % inspectors.length] : null;

    const deadlineHours = threshold.severity_level === 'SERIOUS' ? 4 : 24;
    const deadline = new Date(new Date(report_time).getTime() + deadlineHours * 3600000);

    const warningRecord = {
      id: generateId(),
      warning_no: warningNo,
      project_no,
      section_id: section.id,
      section_code: section.section_code,
      process_id: procId,
      welding_param_id: paramRecord.id,
      warning_type: 'PARAM_OUT_OF_RANGE',
      severity_level: threshold.severity_level,
      param_type: threshold.param_type,
      actual_value: actualValue,
      threshold_min: threshold.min_value,
      threshold_max: threshold.max_value,
      status: 'PENDING_INSPECTION',
      inspector_code: inspector ? inspector.welder_code : null,
      inspector_name: inspector ? inspector.welder_name : null,
      deadline: deadline.toISOString(),
      escalated: false,
      create_time: new Date().toISOString(),
    };
    qualityWarnings.push(warningRecord);

    warnings.push({
      warning_no: warningNo,
      param_type: threshold.param_type,
      actual_value: actualValue,
      threshold_min: threshold.min_value,
      threshold_max: threshold.max_value,
      severity_level: threshold.severity_level,
      inspector_code: warningRecord.inspector_code,
      inspector_name: warningRecord.inspector_name,
      deadline: deadline.toISOString(),
      status: 'PENDING_INSPECTION',
    });
  }

  const message = warnings.length > 0
    ? `参数上报成功，检测到 ${warnings.length} 项参数超出阈值，已生成质量预警工单`
    : '参数上报成功，参数在工艺阈值范围内';

  return res.json(ok({
    param_id: paramRecord.id,
    has_warning: warnings.length > 0,
    warning_count: warnings.length,
    warnings,
  }, message));
});

app.get('/api/quality/warning/:warningNo', (req, res) => {
  const warning = qualityWarnings.find(w => w.warning_no === req.params.warningNo);
  if (!warning) {
    return res.json(fail('质量预警工单不存在'));
  }
  return res.json(ok(warning));
});

app.get('/api/quality/warnings', (req, res) => {
  const { project_no, section_code } = req.query;
  if (!project_no) {
    return res.json(fail('项目号不能为空'));
  }

  let filtered = qualityWarnings.filter(w => w.project_no === project_no);

  if (section_code) {
    const section = sections.find(s => s.project_no === project_no && s.section_code === section_code);
    if (!section) {
      return res.json(ok([]));
    }
    filtered = filtered.filter(w => w.section_id === section.id);
  }

  filtered.sort((a, b) => b.create_time.localeCompare(a.create_time));
  return res.json(ok(filtered));
});

app.get('/api/report/daily', (req, res) => {
  const { project_no, report_date } = req.query;
  if (!project_no || !report_date) {
    return res.json(fail('项目号和报表日期不能为空'));
  }

  const projectSections = sections.filter(s => s.project_no === project_no);
  const projectExists = sections.some(s => s.project_no === project_no);

  const dayStart = new Date(report_date + 'T00:00:00');
  const dayEnd = new Date(report_date + 'T23:59:59');

  const dayInspections = inspectionRecords.filter(r =>
    r.project_no === project_no &&
    new Date(r.inspect_time) >= dayStart &&
    new Date(r.inspect_time) <= dayEnd
  );
  const hasDayData = dayInspections.length > 0;

  if (!projectExists || !hasDayData) {
    const emptyReason = !projectExists
      ? `项目号 ${project_no} 不存在`
      : `项目 ${project_no} 在 ${report_date} 当天无任何数据`;

    return res.json(ok({
      project_no,
      report_date,
      has_data: false,
      empty_reason: emptyReason,
      section_count: 0,
      avg_completion_rate: 0,
      overall_first_pass_rate: 0,
      total_inspections: 0,
      first_pass_count: 0,
      equipment_count: 0,
      fault_equipment_count: 0,
      total_fault_count: 0,
      equipment_fault_rate: 0,
      section_stats: [],
      equipment_stats: [],
    }));
  }

  const sectionStats = [];
  let totalCompletion = 0;

  for (const sec of projectSections) {
    const secInspections = dayInspections.filter(r => r.section_id === sec.id);
    const totalInspect = secInspections.length;
    const firstPassCount = secInspections.filter(r => r.first_pass).length;
    const firstPassRate = totalInspect > 0 ? Math.round((firstPassCount / totalInspect) * 10000) / 100 : 0;

    sectionStats.push({
      section_code: sec.section_code,
      section_name: sec.section_name,
      completion_rate: sec.completion_rate,
      plan_start_date: sec.plan_start_date,
      plan_end_date: sec.plan_end_date,
      total_inspections: totalInspect,
      first_pass_count: firstPassCount,
      first_pass_rate: firstPassRate,
    });

    totalCompletion += sec.completion_rate;
  }

  const avgCompletionRate = projectSections.length > 0
    ? Math.round((totalCompletion / projectSections.length) * 100) / 100
    : 0;

  const totalInspectAll = dayInspections.length;
  const firstPassAll = dayInspections.filter(r => r.first_pass).length;
  const overallFirstPassRate = totalInspectAll > 0
    ? Math.round((firstPassAll / totalInspectAll) * 10000) / 100
    : 0;

  const totalEquipment = equipments.length;
  const faultEquipmentCount = equipments.filter(e => e.status === 'FAULT').length;
  const totalFaultCount = equipments.reduce((sum, e) => sum + e.fault_count, 0);
  const equipmentFaultRate = totalEquipment > 0
    ? Math.round((faultEquipmentCount / totalEquipment) * 10000) / 100
    : 0;

  const equipmentStats = equipments.map(e => ({
    equipment_code: e.equipment_code,
    equipment_name: e.equipment_name,
    workshop: e.workshop,
    status: e.status,
    fault_count: e.fault_count,
  }));

  return res.json(ok({
    project_no,
    report_date,
    has_data: true,
    section_count: projectSections.length,
    avg_completion_rate: avgCompletionRate,
    overall_first_pass_rate: overallFirstPassRate,
    total_inspections: totalInspectAll,
    first_pass_count: firstPassAll,
    equipment_count: totalEquipment,
    fault_equipment_count: faultEquipmentCount,
    total_fault_count: totalFaultCount,
    equipment_fault_rate: equipmentFaultRate,
    section_stats: sectionStats,
    equipment_stats: equipmentStats,
  }));
});

app.get('/api/report/sections/completion', (req, res) => {
  const { project_no } = req.query;
  if (!project_no) {
    return res.json(fail('项目号不能为空'));
  }

  const projectSections = sections.filter(s => s.project_no === project_no);
  if (projectSections.length === 0) {
    return res.json(ok([]));
  }

  const result = projectSections.map(sec => ({
    section_code: sec.section_code,
    section_name: sec.section_name,
    completion_rate: sec.completion_rate,
    plan_start_date: sec.plan_start_date,
    plan_end_date: sec.plan_end_date,
  }));

  return res.json(ok(result));
});

const PORT = 8080;
app.listen(PORT, () => {
  console.log(`=== 船舶制造项目全生命周期协同管理平台 启动 ===`);
  console.log(`服务地址: http://localhost:${PORT}`);
  console.log(`健康检查: http://localhost:${PORT}/api/health`);
  console.log(`===============================================`);
});
