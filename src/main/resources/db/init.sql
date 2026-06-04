-- 创建数据库
CREATE DATABASE IF NOT EXISTS `college_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `college_platform`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 用户表 (sys_user)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `student_id` varchar(32) NOT NULL COMMENT '学号/工号',
  `password` varchar(128) NOT NULL COMMENT '密码(BCrypt加密)',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `role` varchar(32) NOT NULL DEFAULT 'STUDENT' COMMENT '角色: STUDENT, TEACHER, ADMIN, LIBRARIAN',
  `email` varchar(128) DEFAULT NULL COMMENT '联系邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像链接',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态: 1正常, 0禁用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 初始化超级管理员: admin / admin123 (BCrypt 强哈希)
INSERT INTO `sys_user` (`id`, `student_id`, `password`, `name`, `role`) VALUES (1, 'admin', '$2a$10$wT.f2V.yR7E13zOfE0xN/eO6Y3yB6F1v5V.f2V.yR7E13OfE0xN/e', '超级管理员', 'ADMIN');

-- ----------------------------
-- 2. 二手商品表 (market_goods)
-- ----------------------------
DROP TABLE IF EXISTS `market_goods`;
CREATE TABLE `market_goods` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `seller_id` bigint NOT NULL COMMENT '发布者用户ID',
  `title` varchar(128) NOT NULL COMMENT '商品标题',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '交易价格',
  `category` varchar(64) DEFAULT NULL COMMENT '商品分类',
  `images` json DEFAULT NULL COMMENT '图片关联JSON',
  `status` varchar(32) NOT NULL DEFAULT 'ON_SALE' COMMENT '状态: ON_SALE(上架), SOLD(售出), OFF_SHELF(下架)',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二手交易商品表';

-- ----------------------------
-- 3. 图书表 (lib_book)
-- ----------------------------
DROP TABLE IF EXISTS `lib_book`;
CREATE TABLE `lib_book` (
  `id` bigint NOT NULL COMMENT '图书ID',
  `isbn` varchar(32) NOT NULL COMMENT '标准书号',
  `title` varchar(128) NOT NULL COMMENT '书籍名称',
  `author` varchar(128) NOT NULL COMMENT '作者',
  `publisher` varchar(128) DEFAULT NULL COMMENT '出版社',
  `total_stock` int NOT NULL DEFAULT 0 COMMENT '总库存',
  `current_stock` int NOT NULL DEFAULT 0 COMMENT '当前在馆可用库存',
  `location` varchar(64) DEFAULT NULL COMMENT '排架号/具体位置',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_isbn` (`isbn`),
  KEY `idx_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书馆藏表';

-- ----------------------------
-- 4. 教务课程表 (edu_course)
-- ----------------------------
DROP TABLE IF EXISTS `edu_course`;
CREATE TABLE `edu_course` (
  `id` bigint NOT NULL COMMENT '课程ID',
  `course_code` varchar(32) NOT NULL COMMENT '课程代码',
  `course_name` varchar(128) NOT NULL COMMENT '课程名称',
  `credits` decimal(4,1) NOT NULL COMMENT '学分数',
  `teacher_name` varchar(64) DEFAULT NULL COMMENT '任课教师',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教务课程表';

-- ----------------------------
-- 5. 教务成绩表 (edu_grade)
-- ----------------------------
DROP TABLE IF EXISTS `edu_grade`;
CREATE TABLE `edu_grade` (
  `id` bigint NOT NULL COMMENT '成绩清单ID',
  `user_id` bigint NOT NULL COMMENT '关联学生用户ID',
  `course_id` bigint NOT NULL COMMENT '关联课程ID',
  `term` varchar(32) NOT NULL COMMENT '学期(如2024-2025-1)',
  `score` decimal(5,2) DEFAULT NULL COMMENT '考试得分',
  `gpa_point` decimal(3,2) DEFAULT NULL COMMENT '换算绩点',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_course_term` (`user_id`, `course_id`, `term`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成绩表';

-- ----------------------------
-- 6. 学术期刊表 (journal_article)
-- ----------------------------
DROP TABLE IF EXISTS `journal_article`;
CREATE TABLE `journal_article` (
  `id` bigint NOT NULL COMMENT '期刊文献ID',
  `title` varchar(255) NOT NULL COMMENT '文献标题',
  `authors` varchar(255) DEFAULT NULL COMMENT '作者群',
  `abstract_text` text COMMENT '摘要',
  `source_url` varchar(512) DEFAULT NULL COMMENT '原始出处链接',
  `publish_date` date DEFAULT NULL COMMENT '发表日期',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学术期刊数据表';

SET FOREIGN_KEY_CHECKS = 1;
