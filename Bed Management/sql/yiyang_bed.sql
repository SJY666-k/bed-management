/* =====================================================================================
   东软颐养中心管理系统 —— 床位管理模块 数据库脚本（MySQL 8.x）
   -------------------------------------------------------------------------------------
   依据《02.东软颐养中心管理系统-数据库设计》中 bed / room / beddetails / customer 四张表整理。

   使用步骤：
     1. 用 Navicat / MySQL Workbench / 命令行执行本脚本
        （命令行： mysql -uroot -p < yiyang_bed.sql）
     2. 如果你们的数据库名不是 yiyang，请把下面的 yiyang 换成实际库名，
        并同步修改后端 application.yml 中的 url。

   说明：建表语句使用 CREATE TABLE IF NOT EXISTS、数据使用 INSERT IGNORE，
        重复执行不会报错，也不会覆盖你已有的数据。
   ===================================================================================== */

-- 1. 创建数据库（已存在则跳过）
CREATE DATABASE IF NOT EXISTS `yiyang` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `yiyang`;

-- 2. 房间表：床位示意图的「楼层 → 房间」来源
CREATE TABLE IF NOT EXISTS `room` (
    `id`         int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `room_floor` varchar(10) NOT NULL COMMENT '房间楼层',
    `room_no`    int         NOT NULL COMMENT '房间编号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_no` (`room_no`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='房间表';

-- 3. 床位表：床位示意图的「房间 → 床位」来源
CREATE TABLE IF NOT EXISTS `bed` (
    `id`         int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `room_no`    int         NOT NULL COMMENT '房间编号',
    `bed_status` int         NOT NULL DEFAULT 1 COMMENT '床位状态 1：空闲 2：有人 3：外出',
    `remarks`    varchar(255)         DEFAULT NULL COMMENT '备注',
    `bed_no`     varchar(20) NOT NULL COMMENT '床位编号（房间内第几号床）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_bed` (`room_no`, `bed_no`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='床位表';

-- 4. 床位详细信息表：客户的入住 / 退住 / 调换历史
CREATE TABLE IF NOT EXISTS `beddetails` (
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `start_date`  date                  DEFAULT NULL COMMENT '开始时间（入住时间）',
    `end_date`    date                  DEFAULT NULL COMMENT '结束时间（退住时间）',
    `bed_details` varchar(255)          DEFAULT NULL COMMENT '床位详情',
    `customer_id` int                   DEFAULT NULL COMMENT '客户 id',
    `bed_id`      int                   DEFAULT NULL COMMENT '床位 id',
    `is_deleted`  int          NOT NULL DEFAULT 0 COMMENT '0：正在使用 1：已失效（使用历史）',
    PRIMARY KEY (`id`),
    KEY `idx_beddetails_customer` (`customer_id`),
    KEY `idx_beddetails_bed` (`bed_id`),
    KEY `idx_beddetails_is_deleted` (`is_deleted`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='床位详细信息表';

-- 5. 客户表（床位管理只用到姓名、性别、房间号、床号、楼号等字段，其余字段按数据库设计文档一并保留）
CREATE TABLE IF NOT EXISTS `customer` (
    `id`                 int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `is_deleted`         int         NOT NULL DEFAULT 0 COMMENT '是否删除 0：未删除 1：已删除',
    `customer_name`      varchar(20) NOT NULL COMMENT '客户姓名',
    `customer_age`       int                  DEFAULT NULL COMMENT '年龄',
    `customer_sex`       int         NOT NULL DEFAULT 0 COMMENT '性别 0：男 1：女',
    `idcard`             varchar(20)          DEFAULT NULL COMMENT '身份证号',
    `room_no`            varchar(20)          DEFAULT NULL COMMENT '房间号',
    `building_no`        varchar(11)          DEFAULT NULL COMMENT '所属楼房',
    `checkin_date`       date                 DEFAULT NULL COMMENT '入住时间',
    `expiration_date`    date                 DEFAULT NULL COMMENT '合同到期时间',
    `contact_tel`        varchar(20)          DEFAULT NULL COMMENT '联系电话',
    `bed_id`             int                  DEFAULT NULL COMMENT '床号（bed.id）',
    `psychosomatic_state` varchar(255)        DEFAULT NULL COMMENT '身心状况',
    `attention`          varchar(255)         DEFAULT NULL COMMENT '注意事项',
    `birthday`           date                 DEFAULT NULL COMMENT '出生日期',
    `height`             varchar(10)          DEFAULT NULL COMMENT '身高',
    `weight`             varchar(10)          DEFAULT NULL COMMENT '体重',
    `blood_type`         varchar(10)          DEFAULT NULL COMMENT '血型',
    `filepath`           varchar(255)         DEFAULT NULL COMMENT '档案文件路径',
    `user_id`            int                  DEFAULT NULL COMMENT '所属用户 id',
    `level_id`           int                  DEFAULT NULL COMMENT '护理等级 id',
    `family_member`      varchar(255)         DEFAULT NULL COMMENT '家属信息',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='客户表';

/* =====================================================================================
   初始化演示数据
   一层：101 ~ 106 共 6 个房间；二层：201 ~ 206 共 6 个房间；每个房间 4 张床位。
   合计 48 张床位：空闲 40、有人 6、外出 2。
   ===================================================================================== */

-- 房间：一层 6 间、二层 6 间
INSERT IGNORE INTO `room` (`id`, `room_floor`, `room_no`)
VALUES (1, '1', 101),
       (2, '1', 102),
       (3, '1', 103),
       (4, '1', 104),
       (5, '1', 105),
       (6, '1', 106),
       (7, '2', 201),
       (8, '2', 202),
       (9, '2', 203),
       (10, '2', 204),
       (11, '2', 205),
       (12, '2', 206);

-- 床位：48 张（床位 id 1~24 属于一层，25~48 属于二层）
-- 其中 2、6、10、26、30、41 为「有人」，3、27 为「外出」，其余为「空闲」
INSERT IGNORE INTO `bed` (`id`, `room_no`, `bed_status`, `remarks`, `bed_no`)
VALUES (1, 101, 1, NULL, '1'),
       (2, 101, 2, NULL, '2'),
       (3, 101, 3, NULL, '3'),
       (4, 101, 1, NULL, '4'),
       (5, 102, 1, NULL, '1'),
       (6, 102, 2, NULL, '2'),
       (7, 102, 1, NULL, '3'),
       (8, 102, 1, NULL, '4'),
       (9, 103, 1, NULL, '1'),
       (10, 103, 2, NULL, '2'),
       (11, 103, 1, NULL, '3'),
       (12, 103, 1, NULL, '4'),
       (13, 104, 1, NULL, '1'),
       (14, 104, 1, NULL, '2'),
       (15, 104, 1, NULL, '3'),
       (16, 104, 1, NULL, '4'),
       (17, 105, 1, NULL, '1'),
       (18, 105, 1, NULL, '2'),
       (19, 105, 1, NULL, '3'),
       (20, 105, 1, NULL, '4'),
       (21, 106, 1, NULL, '1'),
       (22, 106, 1, NULL, '2'),
       (23, 106, 1, NULL, '3'),
       (24, 106, 1, NULL, '4'),
       (25, 201, 1, NULL, '1'),
       (26, 201, 2, NULL, '2'),
       (27, 201, 3, NULL, '3'),
       (28, 201, 1, NULL, '4'),
       (29, 202, 1, NULL, '1'),
       (30, 202, 2, NULL, '2'),
       (31, 202, 1, NULL, '3'),
       (32, 202, 1, NULL, '4'),
       (33, 203, 1, NULL, '1'),
       (34, 203, 1, NULL, '2'),
       (35, 203, 1, NULL, '3'),
       (36, 203, 1, NULL, '4'),
       (37, 204, 1, NULL, '1'),
       (38, 204, 1, NULL, '2'),
       (39, 204, 1, NULL, '3'),
       (40, 204, 1, NULL, '4'),
       (41, 205, 2, NULL, '1'),
       (42, 205, 1, NULL, '2'),
       (43, 205, 1, NULL, '3'),
       (44, 205, 1, NULL, '4'),
       (45, 206, 1, NULL, '1'),
       (46, 206, 1, NULL, '2'),
       (47, 206, 1, NULL, '3'),
       (48, 206, 1, NULL, '4');

-- 客户：8 位已入住的老人（客户1~6住「有人」床位，客户7~8住「外出」床位）
INSERT IGNORE INTO `customer` (`id`, `is_deleted`, `customer_name`, `customer_age`, `customer_sex`, `idcard`,
                               `room_no`, `building_no`, `checkin_date`, `expiration_date`,
                               `contact_tel`, `bed_id`, `psychosomatic_state`, `attention`, `blood_type`)
VALUES (1, 0, '张伟', 78, 0, '210102194701011234', '101', '606', '2025-03-01', '2026-03-01', '13804011111', 2, '自理', '无', 'A'),
       (2, 0, '李秀英', 82, 1, '210102194303155678', '102', '606', '2025-04-15', '2026-04-15', '13804012222', 6, '半自理', '高血压，注意血压', 'B'),
       (3, 0, '王守义', 75, 0, '210102195004207890', '103', '606', '2025-05-20', '2026-05-20', '13804013333', 10, '自理', '无', 'O'),
       (4, 0, '刘桂芳', 80, 1, '210102194509122345', '201', '606', '2025-06-01', '2026-06-01', '13804014444', 26, '不能自理', '需全天看护', 'A'),
       (5, 0, '陈国强', 69, 0, '210102195611034567', '202', '606', '2025-06-18', '2026-06-18', '13804015555', 30, '自理', '糖尿病，控制饮食', 'AB'),
       (6, 0, '赵春梅', 85, 1, '210102194002186789', '205', '606', '2025-07-05', '2026-07-05', '13804016666', 41, '半自理', '行动不便', 'O'),
       (7, 0, '孙建国', 79, 0, '210102194608084321', '101', '606', '2025-02-10', '2026-02-10', '13804017777', 3, '自理', '无', 'B'),
       (8, 0, '周雅琴', 84, 1, '210102194112098765', '201', '606', '2025-03-22', '2026-03-22', '13804018888', 27, '半自理', '听力较差', 'A');

-- 床位使用详情：
--   id 1~8      正在使用（is_deleted = 0），对应 8 位老人当前的床位
--   id 9~11     使用历史（is_deleted = 1），用于演示「使用历史」页签
INSERT IGNORE INTO `beddetails` (`id`, `start_date`, `end_date`, `bed_details`, `customer_id`, `bed_id`, `is_deleted`)
VALUES (1, '2025-03-01', NULL, '606栋-1层-101房间-2号床位', 1, 2, 0),
       (2, '2025-04-15', NULL, '606栋-1层-102房间-2号床位', 2, 6, 0),
       (3, '2025-05-20', NULL, '606栋-1层-103房间-2号床位', 3, 10, 0),
       (4, '2025-06-01', NULL, '606栋-2层-201房间-2号床位', 4, 26, 0),
       (5, '2025-06-18', NULL, '606栋-2层-202房间-2号床位', 5, 30, 0),
       (6, '2025-07-05', NULL, '606栋-2层-205房间-1号床位', 6, 41, 0),
       (7, '2025-02-10', NULL, '606栋-1层-101房间-3号床位', 7, 3, 0),
       (8, '2025-03-22', NULL, '606栋-2层-201房间-3号床位', 8, 27, 0),
       (9, '2024-12-01', '2025-03-01', '606栋-1层-102房间-1号床位', 1, 5, 1),
       (10, '2025-01-10', '2025-05-31', '606栋-2层-203房间-1号床位', 4, 33, 1),
       (11, '2024-10-08', '2025-07-04', '606栋-2层-206房间-1号床位', 6, 45, 1);

/* =====================================================================================
   自检查询（可选执行，用于确认数据是否导入成功）
   ===================================================================================== */
-- 床位统计：期望 total=48、free=40、occupied=6、out=2
SELECT COUNT(*)                                                     AS total_cnt,
       COALESCE(SUM(CASE WHEN bed_status = 1 THEN 1 ELSE 0 END), 0) AS free_cnt,
       COALESCE(SUM(CASE WHEN bed_status = 2 THEN 1 ELSE 0 END), 0) AS occupied_cnt,
       COALESCE(SUM(CASE WHEN bed_status = 3 THEN 1 ELSE 0 END), 0) AS out_cnt
FROM `bed`;

-- 床位管理列表（正在使用）：期望 8 行
SELECT bd.id        AS id,
       bd.customer_id AS customerId,
       c.customer_name AS customerName,
       c.customer_sex  AS customerSex,
       bd.bed_details  AS bedDetails,
       bd.start_date   AS startDate,
       bd.end_date     AS endDate
FROM `beddetails` bd
         LEFT JOIN `customer` c ON c.id = bd.customer_id
WHERE bd.is_deleted = 0
ORDER BY bd.start_date DESC, bd.id DESC;
