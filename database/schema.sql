-- 接上表
  CONSTRAINT `fk_payment_record_bill` FOREIGN KEY (`bill_id`) REFERENCES `fee_bill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缴费记录表';

-- 14. 报修类型表
CREATE TABLE `repair_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `type_name` VARCHAR(50) NOT NULL COMMENT '报修类型名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修类型表';

-- 15. 报修工单表
CREATE TABLE `repair_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `order_number` VARCHAR(50) NOT NULL COMMENT '工单编号',
  `room_id` BIGINT NOT NULL COMMENT '房间ID',
  `owner_id` BIGINT NOT NULL COMMENT '业主ID',
  `repair_type_id` BIGINT NOT NULL COMMENT '报修类型ID',
  `title` VARCHAR(100) NOT NULL COMMENT '报修标题',
  `description` TEXT NOT NULL COMMENT '详细描述',
  `images` TEXT DEFAULT NULL COMMENT '图片URL（多个用逗号分隔）',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
  `appointment_date` DATE DEFAULT NULL COMMENT '预约日期',
  `appointment_time` VARCHAR(20) DEFAULT NULL COMMENT '预约时间段',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待处理，1-已分配，2-处理中，3-已完成，4-已取消',
  `priority` TINYINT DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高',
  `assignee_id` BIGINT DEFAULT NULL COMMENT '处理人ID（管理员）',
  `assign_time` DATETIME DEFAULT NULL COMMENT '分配时间',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始处理时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `complete_remark` TEXT DEFAULT NULL COMMENT '完成备注',
  `owner_rating` TINYINT DEFAULT NULL COMMENT '业主评分（1-5星）',
  `owner_feedback` TEXT DEFAULT NULL COMMENT '业主反馈',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_number` (`order_number`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_repair_type_id` (`repair_type_id`),
  KEY `idx_status` (`status`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_appointment_date` (`appointment_date`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_repair_order_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_repair_order_owner` FOREIGN KEY (`owner_id`) REFERENCES `owner_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_repair_order_repair_type` FOREIGN KEY (`repair_type_id`) REFERENCES `repair_type` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_repair_order_assignee` FOREIGN KEY (`assignee_id`) REFERENCES `admin_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修工单表';

-- 16. 公告表
CREATE TABLE `announcement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(100) NOT NULL COMMENT '公告标题',
  `content` TEXT NOT NULL COMMENT '公告内容',
  `publisher` VARCHAR(50) NOT NULL COMMENT '发布单位',
  `community_id` BIGINT DEFAULT NULL COMMENT '发布小区ID（NULL表示所有小区）',
  `publish_time` DATETIME NOT NULL COMMENT '发布时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-已过期',
  `view_count` INT DEFAULT 0 COMMENT '查看次数',
  `publisher_id` BIGINT DEFAULT NULL COMMENT '发布人ID（管理员）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_community_id` (`community_id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_expire_time` (`expire_time`),
  KEY `idx_publisher_id` (`publisher_id`),
  CONSTRAINT `fk_announcement_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- 17. 操作日志表
CREATE TABLE `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作人用户名',
  `operation` VARCHAR(100) NOT NULL COMMENT '操作内容',
  `method` VARCHAR(10) NOT NULL COMMENT '请求方法',
  `url` VARCHAR(255) NOT NULL COMMENT '请求URL',
  `params` TEXT DEFAULT NULL COMMENT '请求参数',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
  `execute_time` BIGINT DEFAULT NULL COMMENT '执行时间（毫秒）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_admin_user_id` (`admin_user_id`),
  KEY `idx_username` (`username`),
  KEY `idx_operation` (`operation`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 18. 文件表（用于存储上传的文件）
CREATE TABLE `file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) DEFAULT NULL COMMENT '文件类型',
  `uploader_id` BIGINT DEFAULT NULL COMMENT '上传人ID',
  `uploader_type` TINYINT DEFAULT NULL COMMENT '上传人类型：1-业主，2-管理员',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '所属模块',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-删除，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_uploader` (`uploader_id`, `uploader_type`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 初始化数据

-- 插入超级管理员（初始密码：admin123）
INSERT INTO `admin_user` (`username`, `password`, `real_name`, `is_super_admin`, `status`) VALUES
('superadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iK6lFj5C7iG/.BqGAK7b8FQJj3O2', '超级管理员', 1, 1);

-- 插入默认角色
INSERT INTO `role` (`role_name`, `role_code`, `description`, `status`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统最高权限管理员', 1),
('财务管理员', 'FINANCE_ADMIN', '负责财务管理', 1),
('客服管理员', 'CUSTOMER_SERVICE', '负责客服和报修处理', 1),
('楼盘管理员', 'PROPERTY_ADMIN', '负责楼盘管理', 1);

-- 插入默认费用类型
INSERT INTO `fee_type` (`type_name`, `type_code`, `unit`, `price`, `description`, `status`) VALUES
('物业费', 'PROPERTY_FEE', '元/月·平方米', 2.50, '物业管理服务费', 1),
('水费', 'WATER_FEE', '元/吨', 3.20, '生活用水费用', 1),
('电费', 'ELECTRICITY_FEE', '元/度', 0.65, '生活用电费用', 1),
('停车费', 'PARKING_FEE', '元/月', 300.00, '地下停车场月租费', 1),
('燃气费', 'GAS_FEE', '元/立方米', 2.80, '天然气费用', 1);

-- 插入默认报修类型
INSERT INTO `repair_type` (`type_name`, `description`, `status`) VALUES
('水电维修', '水管漏水、电路故障等维修', 1),
('门窗维修', '门窗损坏、锁具故障等维修', 1),
('家电故障', '家用电器故障维修', 1),
('管道疏通', '下水道、马桶等管道堵塞疏通', 1),
('其他', '其他类型报修', 1);

-- 创建索引优化查询性能
CREATE INDEX idx_fee_bill_owner_status ON fee_bill(owner_id, status);
CREATE INDEX idx_repair_order_owner_status ON repair_order(owner_id, status);
CREATE INDEX idx_announcement_status_time ON announcement(status, publish_time);
CREATE INDEX idx_room_owner_status ON room(owner_id, status);

-- 创建视图
CREATE VIEW v_owner_dashboard AS
SELECT 
    o.id as owner_id,
    o.real_name,
    o.phone,
    COUNT(DISTINCT r.id) as room_count,
    COUNT(DISTINCT CASE WHEN fb.status = 0 THEN fb.id END) as unpaid_bills,
    SUM(CASE WHEN fb.status = 0 THEN fb.amount ELSE 0 END) as unpaid_amount,
    COUNT(DISTINCT CASE WHEN ro.status IN (0,1,2) THEN ro.id END) as pending_repairs
FROM owner_user o
LEFT JOIN room r ON o.id = r.owner_id AND r.status = 1
LEFT JOIN fee_bill fb ON r.id = fb.room_id
LEFT JOIN repair_order ro ON r.id = ro.room_id
WHERE o.status = 1
GROUP BY o.id, o.real_name, o.phone;

-- 创建存储过程：生成月度账单
DELIMITER //
CREATE PROCEDURE generate_monthly_bills(
    IN p_period VARCHAR(20),
    IN p_fee_type_id BIGINT,
    IN p_operator_id BIGINT
)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_room_id BIGINT;
    DECLARE v_owner_id BIGINT;
    DECLARE v_amount DECIMAL(10,2);
    DECLARE v_bill_number VARCHAR(50);
    DECLARE cur_rooms CURSOR FOR 
        SELECT id, owner_id FROM room WHERE status = 1 AND owner_id IS NOT NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur_rooms;
    
    read_loop: LOOP
        FETCH cur_rooms INTO v_room_id, v_owner_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 计算金额（这里可以根据实际业务逻辑调整）
        SET v_amount = 100.00; -- 示例金额
        
        -- 生成账单编号
        SET v_bill_number = CONCAT('BILL', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), LPAD(v_room_id, 6, '0'));
        
        -- 插入账单
        INSERT INTO fee_bill (
            bill_number, room_id, owner_id, fee_type_id, period, 
            amount, start_date, end_date, due_date, status
        ) VALUES (
            v_bill_number, v_room_id, v_owner_id, p_fee_type_id, p_period,
            v_amount, 
            DATE_FORMAT(CONCAT(p_period, '-01'), '%Y-%m-%d'),
            LAST_DAY(DATE_FORMAT(CONCAT(p_period, '-01'), '%Y-%m-%d')),
            DATE_ADD(LAST_DAY(DATE_FORMAT(CONCAT(p_period, '-01'), '%Y-%m-%d')), INTERVAL 15 DAY),
            0
        );
    END LOOP;
    
    CLOSE cur_rooms;
END //
DELIMITER ;

-- 创建触发器：自动更新房间状态
DELIMITER //
CREATE TRIGGER trg_room_owner_update
AFTER UPDATE ON room
FOR EACH ROW
BEGIN
    IF NEW.owner_id IS NULL AND OLD.owner_id IS NOT NULL THEN
        -- 业主搬出，更新相关账单状态
        UPDATE fee_bill 
        SET status = 4 -- 标记为作废
        WHERE room_id = NEW.id AND status = 0;
    END IF;
END //
DELIMITER ;

-- 创建事件：每天检查逾期账单
DELIMITER //
CREATE EVENT check_overdue_bills
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    UPDATE fee_bill 
    SET status = 2 -- 标记为逾期
    WHERE status = 0 AND due_date < CURDATE();
END //
DELIMITER ;