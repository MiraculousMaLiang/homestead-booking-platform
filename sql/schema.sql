-- 民宿预订平台数据库建表脚本
-- 数据库名: homestead_booking
-- 创建时间: 2025-11-18

-- 创建数据库
CREATE DATABASE IF NOT EXISTS homestead_booking DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE homestead_booking;

-- ============================
-- 1. 用户表
-- ============================
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
    `birthday` DATE COMMENT '生日',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `user_type` TINYINT DEFAULT 1 COMMENT '用户类型: 1-普通用户 2-房东 3-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    INDEX `idx_email` (`email`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================
-- 2. 房源表
-- ============================
CREATE TABLE `homestead` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '房源ID',
    `user_id` BIGINT NOT NULL COMMENT '房东用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '房源标题',
    `description` TEXT COMMENT '房源描述',
    `province` VARCHAR(50) COMMENT '省份',
    `city` VARCHAR(50) COMMENT '城市',
    `district` VARCHAR(50) COMMENT '区县',
    `address` VARCHAR(500) COMMENT '详细地址',
    `longitude` DECIMAL(10, 7) COMMENT '经度',
    `latitude` DECIMAL(10, 7) COMMENT '维度',
    `room_type` TINYINT COMMENT '房型: 1-整套房 2-独立房间 3-合住房间',
    `bedroom_count` INT DEFAULT 0 COMMENT '卧室数量',
    `bed_count` INT DEFAULT 0 COMMENT '床位数量',
    `bathroom_count` INT DEFAULT 0 COMMENT '卫生间数量',
    `max_guests` INT DEFAULT 1 COMMENT '最多入住人数',
    `area` DECIMAL(10, 2) COMMENT '面积(平方米)',
    `price_per_day` DECIMAL(10, 2) NOT NULL COMMENT '日租金',
    `price_per_week` DECIMAL(10, 2) COMMENT '周租金',
    `price_per_month` DECIMAL(10, 2) COMMENT '月租金',
    `deposit` DECIMAL(10, 2) DEFAULT 0 COMMENT '押金',
    `min_days` INT DEFAULT 1 COMMENT '最少入住天数',
    `cover_image` VARCHAR(500) COMMENT '封面图',
    `check_in_time` TIME COMMENT '入住时间',
    `check_out_time` TIME COMMENT '退房时间',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `favorite_count` INT DEFAULT 0 COMMENT '收藏次数',
    `order_count` INT DEFAULT 0 COMMENT '订单数量',
    `rating_score` DECIMAL(3, 2) DEFAULT 5.00 COMMENT '评分(0-5)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-下架 1-上架 2-维护中',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_city` (`city`),
    INDEX `idx_price` (`price_per_day`),
    INDEX `idx_rating` (`rating_score`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源表';

-- ============================
-- 3. 房源图片表
-- ============================
CREATE TABLE `homestead_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `image_type` TINYINT DEFAULT 1 COMMENT '图片类型: 1-室内图 2-外观图 3-周边环境',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_homestead_id` (`homestead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源图片表';

-- ============================
-- 4. 房源设施表
-- ============================
CREATE TABLE `homestead_facility` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `facility_name` VARCHAR(50) NOT NULL COMMENT '设施名称',
    `facility_type` TINYINT COMMENT '设施类型: 1-基础设施 2-娱乐设施 3-安全设施',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_homestead_id` (`homestead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源设施表';

-- ============================
-- 5. 订单表
-- ============================
CREATE TABLE `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `landlord_id` BIGINT NOT NULL COMMENT '房东ID',
    `check_in_date` DATE NOT NULL COMMENT '入住日期',
    `check_out_date` DATE NOT NULL COMMENT '退房日期',
    `days` INT NOT NULL COMMENT '入住天数',
    `guest_count` INT DEFAULT 1 COMMENT '入住人数',
    `guest_name` VARCHAR(50) COMMENT '入住人姓名',
    `guest_phone` VARCHAR(20) COMMENT '入住人手机号',
    `total_price` DECIMAL(10, 2) NOT NULL COMMENT '总价',
    `deposit` DECIMAL(10, 2) DEFAULT 0 COMMENT '押金',
    `discount_amount` DECIMAL(10, 2) DEFAULT 0 COMMENT '优惠金额',
    `actual_price` DECIMAL(10, 2) NOT NULL COMMENT '实付金额',
    `special_request` TEXT COMMENT '特殊需求',
    `check_in_code` VARCHAR(20) COMMENT '入住码',
    `order_status` TINYINT DEFAULT 1 COMMENT '订单状态: 1-待支付 2-已支付 3-已入住 4-已完成 5-已取消 6-已退款',
    `cancel_reason` VARCHAR(500) COMMENT '取消原因',
    `cancel_time` DATETIME COMMENT '取消时间',
    `pay_time` DATETIME COMMENT '支付时间',
    `check_in_time` DATETIME COMMENT '实际入住时间',
    `check_out_time` DATETIME COMMENT '实际退房时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_homestead_id` (`homestead_id`),
    INDEX `idx_landlord_id` (`landlord_id`),
    INDEX `idx_status` (`order_status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================
-- 6. 支付记录表
-- ============================
CREATE TABLE `payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '支付ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `payment_no` VARCHAR(50) NOT NULL COMMENT '支付流水号',
    `payment_type` TINYINT COMMENT '支付方式: 1-微信支付 2-支付宝 3-银行卡',
    `payment_amount` DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    `payment_status` TINYINT DEFAULT 1 COMMENT '支付状态: 1-待支付 2-支付成功 3-支付失败 4-已退款',
    `transaction_id` VARCHAR(100) COMMENT '第三方交易号',
    `payment_time` DATETIME COMMENT '支付时间',
    `refund_amount` DECIMAL(10, 2) DEFAULT 0 COMMENT '退款金额',
    `refund_time` DATETIME COMMENT '退款时间',
    `refund_reason` VARCHAR(500) COMMENT '退款原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- ============================
-- 7. 评价表
-- ============================
CREATE TABLE `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `rating_overall` TINYINT NOT NULL COMMENT '总体评分(1-5)',
    `rating_location` TINYINT COMMENT '位置评分(1-5)',
    `rating_cleanliness` TINYINT COMMENT '卫生评分(1-5)',
    `rating_facility` TINYINT COMMENT '设施评分(1-5)',
    `rating_service` TINYINT COMMENT '服务评分(1-5)',
    `rating_value` TINYINT COMMENT '性价比评分(1-5)',
    `content` TEXT COMMENT '评价内容',
    `images` TEXT COMMENT '评价图片(多张用逗号分隔)',
    `tags` VARCHAR(500) COMMENT '标签(多个用逗号分隔)',
    `is_anonymous` TINYINT DEFAULT 0 COMMENT '是否匿名: 0-否 1-是',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `reply_content` TEXT COMMENT '房东回复',
    `reply_time` DATETIME COMMENT '回复时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-待审核 1-已通过 2-已拒绝',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_homestead_id` (`homestead_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ============================
-- 8. 收藏表
-- ============================
CREATE TABLE `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_homestead` (`user_id`, `homestead_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_homestead_id` (`homestead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ============================
-- 9. 浏览记录表
-- ============================
CREATE TABLE `browse_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestead_id` BIGINT NOT NULL COMMENT '房源ID',
    `browse_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    `stay_duration` INT DEFAULT 0 COMMENT '停留时长(秒)',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_homestead_id` (`homestead_id`),
    INDEX `idx_browse_time` (`browse_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览记录表';

-- ============================
-- 10. 用户偏好表
-- ============================
CREATE TABLE `user_preference` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '偏好ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `price_min` DECIMAL(10, 2) COMMENT '最低价格',
    `price_max` DECIMAL(10, 2) COMMENT '最高价格',
    `preferred_cities` VARCHAR(500) COMMENT '偏好城市(多个用逗号分隔)',
    `room_type` TINYINT COMMENT '房型偏好',
    `facilities` VARCHAR(500) COMMENT '设施偏好(多个用逗号分隔)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户偏好表';

-- ============================
-- 11. 消息通知表
-- ============================
CREATE TABLE `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT COMMENT '通知内容',
    `type` TINYINT COMMENT '通知类型: 1-订单通知 2-系统通知 3-活动通知',
    `related_id` BIGINT COMMENT '关联ID(如订单ID)',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读: 0-未读 1-已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_is_read` (`is_read`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- ============================
-- 12. 聊天消息表
-- ============================
CREATE TABLE `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `from_user_id` BIGINT NOT NULL COMMENT '发送者ID',
    `to_user_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `message_type` TINYINT DEFAULT 1 COMMENT '消息类型: 1-文本 2-图片 3-语音 4-视频',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读: 0-未读 1-已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_from_user` (`from_user_id`),
    INDEX `idx_to_user` (`to_user_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- ============================
-- 13. 优惠券表
-- ============================
CREATE TABLE `coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
    `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    `type` TINYINT COMMENT '类型: 1-满减券 2-折扣券 3-立减券',
    `discount_amount` DECIMAL(10, 2) COMMENT '优惠金额',
    `discount_rate` DECIMAL(3, 2) COMMENT '折扣率(0-1)',
    `min_amount` DECIMAL(10, 2) DEFAULT 0 COMMENT '最低消费金额',
    `total_count` INT COMMENT '发放总数',
    `received_count` INT DEFAULT 0 COMMENT '已领取数量',
    `used_count` INT DEFAULT 0 COMMENT '已使用数量',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-停用 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- ============================
-- 14. 用户优惠券表
-- ============================
CREATE TABLE `user_coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
    `order_id` BIGINT COMMENT '使用的订单ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1-未使用 2-已使用 3-已过期',
    `receive_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    `use_time` DATETIME COMMENT '使用时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- ============================
-- 插入初始数据
-- ============================

-- 插入管理员账号 (密码: admin123 使用BCrypt加密后的值,需要程序中实际加密)
INSERT INTO `user` (`username`, `password`, `phone`, `email`, `nickname`, `user_type`, `status`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138000', 'admin@homestead.com', '系统管理员', 3, 1);

-- 插入一些基础设施类型数据供参考
-- WiFi, 空调, 电视, 冰箱, 洗衣机, 热水器, 厨房, 停车位等
