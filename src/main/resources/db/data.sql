-- 物料数据
INSERT INTO material (material_code, material_name, material_type, stock_limit, current_stock, require_certificate)
VALUES ('STEEL-001', '船用钢板DH36', '钢材', 500, 120, 1),
       ('STEEL-002', '船用钢板EH36', '钢材', 300, 280, 1),
       ('PIPE-001', '不锈钢管DN100', '管材', 1000, 350, 1),
       ('WELD-001', '焊丝E71T-1', '焊材', 2000, 800, 0),
       ('COAT-001', '环氧防腐漆', '涂料', 500, 490, 1);

-- 供应商数据
INSERT INTO supplier (supplier_code, supplier_name, qualification_valid_date, qualification_level)
VALUES ('SUP-001', '上海宝钢船舶材料有限公司', '2027-06-30', 'A级'),
       ('SUP-002', '鞍钢股份有限公司', '2025-03-15', 'A级'),
       ('SUP-003', '天津钢管集团', '2026-12-31', 'B级'),
       ('SUP-004', '大西洋焊材', '2025-01-10', 'B级'),
       ('SUP-005', '中远关西涂料', '2028-01-20', 'A级');

-- 分段数据
INSERT INTO section_info (project_no, section_code, section_name, plan_start_date, plan_end_date, completion_rate)
VALUES ('PROJ-2024-001', 'SEC-101', '上层建筑分段101', '2024-01-15', '2024-06-30', 65.5),
       ('PROJ-2024-001', 'SEC-102', '货舱分段102', '2024-02-01', '2024-07-15', 42.0),
       ('PROJ-2024-001', 'SEC-103', '机舱分段103', '2024-03-10', '2024-08-20', 28.3),
       ('PROJ-2024-002', 'SEC-201', '首部分段201', '2024-04-01', '2024-09-30', 15.0),
       ('PROJ-2024-002', 'SEC-202', '尾部分段202', '2024-05-01', '2024-10-31', 5.0);

-- 工序数据
INSERT INTO work_process (process_code, process_name, work_type, standard_hour, seq_no)
VALUES ('PROC-001', '下料切割', '切割', 16, 1),
       ('PROC-002', '部件装配', '装配', 24, 2),
       ('PROC-003', '焊接', '焊接', 40, 3),
       ('PROC-004', 'X光探伤', '检测', 8, 4),
       ('PROC-005', '除锈涂装', '涂装', 12, 5);

-- 车间资源
INSERT INTO workshop_resource (resource_code, resource_name, resource_type, workshop, capacity)
VALUES ('RES-C01', '数控切割机1号', '设备', '下料车间', 1),
       ('RES-C02', '数控切割机2号', '设备', '下料车间', 1),
       ('RES-A01', '装配工位A1', '工位', '装焊车间', 1),
       ('RES-A02', '装配工位A2', '工位', '装焊车间', 1),
       ('RES-W01', '焊接工位W1', '工位', '装焊车间', 1),
       ('RES-W02', '焊接工位W2', '工位', '装焊车间', 1),
       ('RES-X01', 'X光探伤室', '设备', '检测中心', 1),
       ('RES-P01', '涂装房1号', '设备', '涂装车间', 1);

-- 工艺阈值
INSERT INTO process_threshold (process_id, param_type, min_value, max_value, severity_level)
VALUES (3, 'temperature', 100.00, 250.00, 'WARNING'),
       (3, 'current', 180.00, 280.00, 'WARNING'),
       (3, 'voltage', 24.00, 32.00, 'WARNING'),
       (3, 'temperature', 80.00, 300.00, 'SERIOUS'),
       (3, 'current', 150.00, 320.00, 'SERIOUS'),
       (3, 'voltage', 20.00, 36.00, 'SERIOUS');

-- 焊工技能
INSERT INTO welder_skill (welder_code, welder_name, skill_level, can_inspect, process_types)
VALUES ('W-001', '张师傅', '高级焊工', 1, 'PROC-003'),
       ('W-002', '李师傅', '高级焊工', 1, 'PROC-003'),
       ('W-003', '王师傅', '中级焊工', 0, 'PROC-003'),
       ('W-004', '赵师傅', '中级焊工', 0, 'PROC-003'),
       ('W-005', '钱师傅', '焊工技师', 1, 'PROC-003;PROC-004');

-- 设备数据
INSERT INTO equipment (equipment_code, equipment_name, workshop, status, fault_count)
VALUES ('EQ-001', '数控切割机1号', '下料车间', 'RUNNING', 1),
       ('EQ-002', '数控切割机2号', '下料车间', 'RUNNING', 0),
       ('EQ-003', '焊接机器人1号', '装焊车间', 'RUNNING', 2),
       ('EQ-004', '焊接机器人2号', '装焊车间', 'FAULT', 3),
       ('EQ-005', 'X光探伤机', '检测中心', 'RUNNING', 0),
       ('EQ-006', '喷漆机器人', '涂装车间', 'RUNNING', 1);

-- 交验记录
INSERT INTO inspection_record (project_no, section_id, process_id, inspect_time, first_pass, inspector)
VALUES ('PROJ-2024-001', 1, 1, '2024-06-10 09:30:00', 1, '质检甲'),
       ('PROJ-2024-001', 1, 2, '2024-06-12 14:20:00', 1, '质检甲'),
       ('PROJ-2024-001', 1, 3, '2024-06-14 10:15:00', 0, '质检乙'),
       ('PROJ-2024-001', 1, 3, '2024-06-15 11:00:00', 1, '质检乙'),
       ('PROJ-2024-001', 2, 1, '2024-06-11 08:45:00', 1, '质检甲'),
       ('PROJ-2024-001', 2, 2, '2024-06-13 15:30:00', 1, '质检丙'),
       ('PROJ-2024-002', 4, 1, '2024-06-09 10:00:00', 0, '质检乙'),
       ('PROJ-2024-002', 4, 1, '2024-06-10 09:00:00', 1, '质检乙');
