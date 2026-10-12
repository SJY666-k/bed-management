-- H2 建表脚本（h2 profile 本地演示用，表结构与 MySQL 一致）
-- H2 运行在 MODE=MySQL 下，已去掉 ENGINE / COMMENT 等 MySQL 专有语法

DROP TABLE IF EXISTS beddetails;
DROP TABLE IF EXISTS bed;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS customer;

CREATE TABLE room
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    room_floor VARCHAR(10) NOT NULL,
    room_no    INT         NOT NULL
);

CREATE TABLE bed
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    room_no    INT          NOT NULL,
    bed_status INT          NOT NULL DEFAULT 1,
    remarks    VARCHAR(255),
    bed_no     VARCHAR(20)  NOT NULL
);

CREATE TABLE beddetails
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    start_date  DATE,
    end_date    DATE,
    bed_details VARCHAR(255),
    customer_id INT,
    bed_id      INT,
    is_deleted  INT NOT NULL DEFAULT 0
);

CREATE TABLE customer
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    is_deleted          INT         NOT NULL DEFAULT 0,
    customer_name       VARCHAR(20) NOT NULL,
    customer_age        INT,
    customer_sex        INT         NOT NULL DEFAULT 0,
    idcard              VARCHAR(20),
    room_no             VARCHAR(20),
    building_no         VARCHAR(11),
    checkin_date        DATE,
    expiration_date     DATE,
    contact_tel         VARCHAR(20),
    bed_id              INT,
    psychosomatic_state VARCHAR(255),
    attention           VARCHAR(255),
    birthday            DATE,
    height              VARCHAR(10),
    weight              VARCHAR(10),
    blood_type          VARCHAR(10),
    filepath            VARCHAR(255),
    user_id             INT,
    level_id            INT,
    family_member       VARCHAR(255)
);
