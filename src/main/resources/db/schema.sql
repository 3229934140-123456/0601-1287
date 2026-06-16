-- 物料表
CREATE TABLE IF NOT EXISTS material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(64) NOT NULL UNIQUE,
    material_name VARCHAR(128) NOT NULL,
    material_type VARCHAR(32) NOT NULL,
    stock_limit INT NOT NULL DEFAULT 0,
    current_stock INT NOT NULL DEFAULT 0,
    require_certificate TINYINT NOT NULL DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 供应商表
CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_code VARCHAR(64) NOT NULL UNIQUE,
    supplier_name VARCHAR(128) NOT NULL,
    qualification_valid_date DATE NOT NULL,
    qualification_level VARCHAR(32),
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 入库申请表
CREATE TABLE IF NOT EXISTS stock_in_apply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    apply_no VARCHAR(64) NOT NULL UNIQUE,
    material_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    apply_quantity INT NOT NULL,
    recommend_quantity INT,
    certificate_no VARCHAR(128),
    certificate_valid_date DATE,
    status VARCHAR(32) NOT NULL,
    reject_reason VARCHAR(512),
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分段表
CREATE TABLE IF NOT EXISTS section_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_no VARCHAR(64) NOT NULL,
    section_code VARCHAR(64) NOT NULL,
    section_name VARCHAR(128) NOT NULL,
    plan_start_date DATE,
    plan_end_date DATE,
    completion_rate DECIMAL(5,2) DEFAULT 0,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_project_section (project_no, section_code)
);

-- 工序表
CREATE TABLE IF NOT EXISTS work_process (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    process_code VARCHAR(64) NOT NULL UNIQUE,
    process_name VARCHAR(128) NOT NULL,
    work_type VARCHAR(32) NOT NULL,
    standard_hour INT NOT NULL,
    seq_no INT NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 车间资源表
CREATE TABLE IF NOT EXISTS workshop_resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_code VARCHAR(64) NOT NULL UNIQUE,
    resource_name VARCHAR(128) NOT NULL,
    resource_type VARCHAR(32) NOT NULL,
    workshop VARCHAR(64) NOT NULL,
    capacity INT DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 排程结果表
CREATE TABLE IF NOT EXISTS build_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_no VARCHAR(64) NOT NULL UNIQUE,
    project_no VARCHAR(64) NOT NULL,
    section_id BIGINT NOT NULL,
    process_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    plan_start_time TIMESTAMP NOT NULL,
    plan_end_time TIMESTAMP NOT NULL,
    locked TINYINT NOT NULL DEFAULT 0,
    status VARCHAR(32) NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 排程冲突表
CREATE TABLE IF NOT EXISTS schedule_conflict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_no VARCHAR(64) NOT NULL,
    conflict_type VARCHAR(32) NOT NULL,
    resource_id BIGINT,
    conflict_desc VARCHAR(512),
    suggest_adjust_cycle INT,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 工艺阈值表
CREATE TABLE IF NOT EXISTS process_threshold (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    process_id BIGINT NOT NULL,
    param_type VARCHAR(32) NOT NULL,
    min_value DECIMAL(10,2),
    max_value DECIMAL(10,2),
    severity_level VARCHAR(32) NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 焊接参数表
CREATE TABLE IF NOT EXISTS welding_param (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_no VARCHAR(64) NOT NULL,
    section_id BIGINT NOT NULL,
    process_id BIGINT NOT NULL,
    welder_code VARCHAR(64) NOT NULL,
    temperature DECIMAL(10,2),
    current_value DECIMAL(10,2),
    voltage_value DECIMAL(10,2),
    report_time TIMESTAMP NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 质量预警工单表
CREATE TABLE IF NOT EXISTS quality_warning (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warning_no VARCHAR(64) NOT NULL UNIQUE,
    project_no VARCHAR(64) NOT NULL,
    section_id BIGINT NOT NULL,
    process_id BIGINT NOT NULL,
    welding_param_id BIGINT,
    warning_type VARCHAR(32) NOT NULL,
    severity_level VARCHAR(32) NOT NULL,
    param_type VARCHAR(64),
    actual_value DECIMAL(10,2),
    threshold_min DECIMAL(10,2),
    threshold_max DECIMAL(10,2),
    status VARCHAR(32) NOT NULL,
    inspector_code VARCHAR(64),
    inspector_name VARCHAR(128),
    deadline TIMESTAMP,
    escalated TINYINT NOT NULL DEFAULT 0,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 焊工技能表
CREATE TABLE IF NOT EXISTS welder_skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    welder_code VARCHAR(64) NOT NULL,
    welder_name VARCHAR(128) NOT NULL,
    skill_level VARCHAR(32) NOT NULL,
    can_inspect TINYINT NOT NULL DEFAULT 0,
    process_types VARCHAR(256),
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 设备表
CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_code VARCHAR(64) NOT NULL UNIQUE,
    equipment_name VARCHAR(128) NOT NULL,
    workshop VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    fault_count INT DEFAULT 0,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 交验记录表
CREATE TABLE IF NOT EXISTS inspection_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_no VARCHAR(64) NOT NULL,
    section_id BIGINT NOT NULL,
    process_id BIGINT NOT NULL,
    inspect_time TIMESTAMP NOT NULL,
    first_pass TINYINT NOT NULL,
    inspector VARCHAR(64),
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 项目日报表
CREATE TABLE IF NOT EXISTS project_daily_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_no VARCHAR(64) NOT NULL,
    report_date DATE NOT NULL,
    section_count INT DEFAULT 0,
    avg_completion_rate DECIMAL(5,2) DEFAULT 0,
    first_pass_rate DECIMAL(5,2) DEFAULT 0,
    equipment_fault_rate DECIMAL(5,2) DEFAULT 0,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_project_date (project_no, report_date)
);
