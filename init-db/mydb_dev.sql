/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3306
 Source Schema         : mydb_dev

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 05/04/2025 10:17:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_element_operation
-- ----------------------------
DROP TABLE IF EXISTS `auth_element_operation`;
CREATE TABLE `auth_element_operation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elementName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面元素名称',
  `elementCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面元素唯一编码',
  `operationType` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型：0可点击  1可见',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限控制--页面元素操作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_element_operation
-- ----------------------------
INSERT INTO `auth_element_operation` VALUES (1, '视频投稿按钮', 'VIDEO_POST_BUTTON', '0', '2025-03-10 19:31:38', '2025-03-10 19:31:38');

-- ----------------------------
-- Table structure for auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单项目名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一编码',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限控制-页面访问表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------
INSERT INTO `auth_menu` VALUES (1, '购买邀请码', 'PurchaseInvitationCode', '2025-03-10 19:37:27', '2025-03-10 19:37:27');

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色唯一编码',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES (1, '开发工程师', 'p5', '2025-03-10 19:26:01', '2025-03-10 19:26:01');
INSERT INTO `auth_role` VALUES (2, '高级开发工程师', 'p6', '2025-03-10 19:26:25', '2025-03-10 19:26:25');
INSERT INTO `auth_role` VALUES (3, '技术专家', 'p7', '2025-03-10 19:26:36', '2025-03-10 19:26:36');
INSERT INTO `auth_role` VALUES (4, '高级技术专家', 'p8', '2025-03-10 19:26:59', '2025-03-10 19:26:59');
INSERT INTO `auth_role` VALUES (5, '资深技术专家', 'p9', '2025-03-10 19:27:36', '2025-03-10 19:27:36');

-- ----------------------------
-- Table structure for auth_role_element_operation
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_element_operation`;
CREATE TABLE `auth_role_element_operation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `elementOperationId` bigint(20) NOT NULL COMMENT '元素操作ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_roleId`(`roleId` ASC) USING BTREE,
  INDEX `idx_elementOperationId`(`elementOperationId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限控制--角色与元素操作关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role_element_operation
-- ----------------------------
INSERT INTO `auth_role_element_operation` VALUES (1, 1, 1, '2025-03-10 19:33:31');

-- ----------------------------
-- Table structure for auth_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_menu`;
CREATE TABLE `auth_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `menuId` bigint(20) NOT NULL COMMENT '页面菜单ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_roleId`(`roleId` ASC) USING BTREE,
  INDEX `idx_menuId`(`menuId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限控制--角色页面菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role_menu
-- ----------------------------
INSERT INTO `auth_role_menu` VALUES (1, 5, 1, '2025-03-10 19:38:32');

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件存储路径',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
  `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件md5唯一标识串',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_file_md5`(`file_md5` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES (1, 'M00/00/00/wKgAb2fhXnSEUIPxAAAAALr17vc529.pdf', 'pdf', 'b04c58707fca42814293ec50160db30a', '2025-03-24 21:30:29');
INSERT INTO `file` VALUES (2, 'M00/00/00/wKgAb2fiuTKEETkCAAAAALRq364073.mp4', 'mp4', '456321', '2025-03-25 22:09:56');
INSERT INTO `file` VALUES (3, 'M00/00/00/wKgAb2fixoSEQLg4AAAAAHY3BG4348.mp4', 'mp4', '456321789', '2025-03-25 23:06:46');

-- ----------------------------
-- Table structure for following_group
-- ----------------------------
DROP TABLE IF EXISTS `following_group`;
CREATE TABLE `following_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `groupName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  `groupType` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组类型：0特别关注 1悄悄关注 2默认分组 3用户自定义分组',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of following_group
-- ----------------------------
INSERT INTO `following_group` VALUES (1, NULL, '特别关注', '0', '2025-03-08 21:45:05', '2025-03-08 21:45:05');
INSERT INTO `following_group` VALUES (2, NULL, '悄悄关注', '1', '2025-03-08 21:45:26', '2025-03-08 21:45:26');
INSERT INTO `following_group` VALUES (3, NULL, '默认分组', '2', '2025-03-08 21:45:54', '2025-03-08 21:45:54');

-- ----------------------------
-- Table structure for refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `refresh_token`;
CREATE TABLE `refresh_token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(20) NULL DEFAULT NULL COMMENT '用户ID，关联用户表',
  `refreshToken` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '刷新令牌',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `expireTime` datetime NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_refresh_token`(`refreshToken` ASC) USING BTREE,
  INDEX `idx_user_id`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '刷新令牌记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refresh_token
-- ----------------------------
INSERT INTO `refresh_token` VALUES (1, 2, 'eyJraWQiOiIyIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiLnrb7lj5HogIUiLCJleHAiOjE3NDI5MDY2OTZ9.qzHWx_tWySVmkWhhZLwONUN34m8r4b-Wo8eIKwm2mLVfA-lDPGyxj_8cLA-yWiEI4gcjskiIZs0l6_rj1f2bALcJoq8k9dik62Q0a13zTgh03xz6gFpb5e_2UgxKXvMzM83Q-3Wkw9n1TshimsE0SslK4QywSdUHgwgRiV1rgxs', NULL, NULL);
INSERT INTO `refresh_token` VALUES (2, 1, 'eyJraWQiOiIxIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiLnrb7lj5HogIUiLCJleHAiOjE3NDI5MDY5NjB9.tx6f_aaln_1kVjzg91pPWtmD1zpJc6yD1U5qk6EQuvy4xmuULoAU6D3eFdkdEGFyPiUFh0bEYh3Z4BODlpcmICwEr3JQLnx1pZlluZqzwlMTlempe9i2CRV03oxCp6cTiaJqAiQqOb-pTMSpAkMibw70CU8B1rlUi9ZZ1cY02GI', NULL, NULL);
INSERT INTO `refresh_token` VALUES (5, 6, 'eyJraWQiOiI2IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiLnrb7lj5HogIUiLCJleHAiOjE3NDQyOTQ1MzN9.EFLtqDOgFve0F0jwf__mmMRQyCsxyMIb4xc7IGk60Num3pyk7t7vDbZ_2WkEm8K3tFWXM2LndBE3uOGlUQ2B1xT17ljkrojC6OE97qmFv8AUV4zQkEwBr5Be80Iho-PQnLt2QIdqLHDn7BVFLvXhYbkXIEaPTugqUkK9V_5XfKQ', NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码哈希',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '用户状态（1: 激活, 0: 禁用）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `lastLoginTime` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `unique_email`(`email` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '138001380033', '', '5d38c18050cf81651a613e6d3f8cb5bd', '1741468941', NULL, '2025-03-08 21:22:22', '2025-03-28 22:35:00', NULL);
INSERT INTO `user` VALUES (2, '1380013800', 'user1@example.com', '8e6c4e2d65e9534bb573a87d0c782243', '1741539573', NULL, '2025-03-09 16:59:33', '2025-03-09 16:59:33', NULL);
INSERT INTO `user` VALUES (3, '1380013810', 'user2@example.com', '62d8fcc94e712178d95ca912df3756a4', '1741539589', NULL, '2025-03-09 16:59:49', '2025-03-09 16:59:49', NULL);
INSERT INTO `user` VALUES (4, '123456', '123456@example.com', 'f38bc0106c44f6712bdde3803f86b1ce', '1743632013', NULL, '2025-04-02 22:13:33', '2025-04-02 22:13:33', NULL);
INSERT INTO `user` VALUES (5, '123456789', '123456789@example.com', '0826c5fe206330511aa479303c53e088', '1743632506', NULL, '2025-04-02 22:21:47', '2025-04-02 22:21:46', NULL);
INSERT INTO `user` VALUES (6, '23456789', '23456789@example.com', 'e81befd98cb450ab91760c8d4e6166d0', '1743713299', NULL, '2025-04-03 20:48:20', '2025-04-03 20:50:04', NULL);

-- ----------------------------
-- Table structure for user_following
-- ----------------------------
DROP TABLE IF EXISTS `user_following`;
CREATE TABLE `user_following`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `followingId` bigint(20) NOT NULL COMMENT '被关注用户ID',
  `groupId` bigint(20) NULL DEFAULT NULL COMMENT '关注分组ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE,
  INDEX `idx_followingId`(`followingId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_following
-- ----------------------------
INSERT INTO `user_following` VALUES (1, 1, 2, 3, '2025-03-09 19:49:04', '2025-03-09 19:49:04');
INSERT INTO `user_following` VALUES (2, 3, 2, 3, '2025-03-09 19:50:14', '2025-03-09 19:50:14');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `nick` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sign` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '签名',
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别：0男 1女 2未知',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE,
  INDEX `idx_nick`(`nick` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 1, '码上阅读', NULL, NULL, 0, '2025-03-08', '2025-03-08 21:22:22', '2025-03-08 21:22:21');
INSERT INTO `user_info` VALUES (2, 2, '码上阅读', NULL, NULL, 0, '2025-03-09', '2025-03-09 16:59:33', '2025-03-09 16:59:33');
INSERT INTO `user_info` VALUES (3, 3, '码上阅读', NULL, NULL, 0, '2025-03-09', '2025-03-09 16:59:49', '2025-03-09 16:59:49');
INSERT INTO `user_info` VALUES (4, 4, '码上阅读', NULL, NULL, 0, '2025-04-02', '2025-04-02 22:13:33', '2025-04-02 22:13:33');
INSERT INTO `user_info` VALUES (5, 5, '码上阅读', NULL, NULL, 0, '2025-04-02', '2025-04-02 22:21:47', '2025-04-02 22:21:46');
INSERT INTO `user_info` VALUES (6, 6, '码上阅读', NULL, NULL, 0, '2025-04-03', '2025-04-03 20:48:20', '2025-04-03 20:50:58');

-- ----------------------------
-- Table structure for user_moments
-- ----------------------------
DROP TABLE IF EXISTS `user_moments`;
CREATE TABLE `user_moments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `type` tinyint(4) NOT NULL COMMENT '动态类型：0视频, 1直播, 2专栏动态',
  `contentId` bigint(20) NULL DEFAULT NULL COMMENT '内容详情ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户动态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_moments
-- ----------------------------
INSERT INTO `user_moments` VALUES (1, 1, 0, 123, '2025-03-09 14:55:10', '2025-03-09 14:55:10');
INSERT INTO `user_moments` VALUES (2, 1, 0, 123, '2025-03-09 15:02:37', '2025-03-09 15:02:38');
INSERT INTO `user_moments` VALUES (3, 1, 0, 123, '2025-03-09 16:44:47', '2025-03-09 16:44:46');
INSERT INTO `user_moments` VALUES (4, 2, 0, 123, '2025-03-09 19:59:17', '2025-03-09 19:59:17');
INSERT INTO `user_moments` VALUES (5, 2, 0, 123, '2025-03-09 20:04:31', '2025-03-09 20:04:32');
INSERT INTO `user_moments` VALUES (6, 2, 0, 123, '2025-03-09 20:11:19', '2025-03-09 20:11:18');
INSERT INTO `user_moments` VALUES (7, 2, 0, 456, '2025-03-09 20:11:22', '2025-03-09 20:11:22');
INSERT INTO `user_moments` VALUES (8, 2, 0, 456789, '2025-03-09 20:43:07', '2025-03-09 20:43:07');
INSERT INTO `user_moments` VALUES (9, 2, 0, 456789, '2025-03-16 22:17:56', '2025-03-16 22:17:56');
INSERT INTO `user_moments` VALUES (10, 2, 0, 456789, '2025-03-16 22:22:19', '2025-03-16 22:22:18');
INSERT INTO `user_moments` VALUES (11, 2, 0, 456789, '2025-03-16 22:27:17', '2025-03-16 22:27:17');
INSERT INTO `user_moments` VALUES (12, 2, 0, 456789, '2025-03-16 22:29:01', '2025-03-16 22:29:01');
INSERT INTO `user_moments` VALUES (13, 2, 0, 456789, '2025-03-16 22:30:04', '2025-03-16 22:30:04');
INSERT INTO `user_moments` VALUES (14, 2, 0, 456789, '2025-03-16 22:30:40', '2025-03-16 22:30:39');
INSERT INTO `user_moments` VALUES (15, 2, 0, 456789, '2025-03-18 15:38:11', '2025-03-18 15:38:11');
INSERT INTO `user_moments` VALUES (16, 3, 0, 456789, '2025-03-18 20:50:10', '2025-03-18 20:50:10');
INSERT INTO `user_moments` VALUES (17, 2, 0, 456789, '2025-04-04 10:46:54', '2025-04-04 10:46:53');
INSERT INTO `user_moments` VALUES (18, 2, 0, 456789123, '2025-04-04 10:53:20', '2025-04-04 10:53:19');
INSERT INTO `user_moments` VALUES (19, 2, 0, 456789123, '2025-04-04 10:53:54', '2025-04-04 10:53:53');
INSERT INTO `user_moments` VALUES (20, 2, 0, 456789123, '2025-04-04 12:35:24', '2025-04-04 12:36:01');
INSERT INTO `user_moments` VALUES (21, 2, 0, 789456123, '2025-04-04 12:41:44', '2025-04-04 12:41:43');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `roleId` bigint(20) NOT NULL COMMENT '角色ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE,
  INDEX `idx_roleId`(`roleId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1, '2025-03-10 19:32:41');
INSERT INTO `user_role` VALUES (2, 2, 5, '2025-03-10 19:37:55');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码哈希',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '用户状态（1: 激活, 0: 禁用）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `lastLoginTime` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `unique_email`(`email` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (4, '198-4397-7663', '', '1jHd19KZmX', 'DTlWkUjFA1', 53, '2008-10-13 15:32:55', '2025-03-30 20:17:18', '2007-06-20 06:45:15');
INSERT INTO `users` VALUES (5, '(1223) 94 9061', 'kwokkc@gmail.com', 'eODjQlqPjc', 'Ue7kbKqREh', 103, '2013-09-30 05:58:49', '2012-02-05 19:58:33', '2009-06-28 20:41:14');
INSERT INTO `users` VALUES (6, '838-097-8295', 'tgran@icloud.com', 'd702cas30A', '1Djjy5KIcF', 102, '2024-12-02 22:35:45', '2002-07-21 14:50:28', '2016-05-22 00:03:55');
INSERT INTO `users` VALUES (7, '134-9591-4348', 'wuziyi@hotmail.com', 'ZpQBsIhvrD', 'abJ1sLCHm9', 4, '2007-09-20 09:58:26', '2001-02-13 23:56:01', '2001-09-30 04:34:37');
INSERT INTO `users` VALUES (8, '10-7033-7564', 'margaret9@yahoo.com', 'ulhwsa48Fd', 'SplxUjmUcI', 76, '2023-05-22 11:10:20', '2021-05-21 19:53:28', '2021-04-08 02:55:22');
INSERT INTO `users` VALUES (9, '(121) 461 6666', 'skaming96@outlook.com', 'xspSJqELUJ', 'zqYWr8nYBy', 125, '2016-04-03 07:42:55', '2012-09-27 13:15:06', '2013-03-29 12:45:01');
INSERT INTO `users` VALUES (10, '5954 583837', 'marn64@outlook.com', 'hkia6P97i1', 'lmGWCPfhhX', 89, '2002-10-01 15:19:47', '2007-08-04 14:07:10', '2007-07-27 19:23:34');
INSERT INTO `users` VALUES (11, '155-7948-7380', 'xu91@mail.com', 'tg4Cl10naD', '1TA1Bq1C67', 17, '2013-08-09 10:08:05', '2012-05-10 17:25:44', '2010-08-01 05:09:05');
INSERT INTO `users` VALUES (12, '(121) 308 1116', 'wuyuning314@outlook.com', 'xdSa1nrlQj', 'CATGibOmNo', 118, '2017-04-02 10:31:20', '2014-12-29 14:47:01', '2002-08-17 07:13:59');
INSERT INTO `users` VALUES (13, '7784 274525', 'yamazaki9@gmail.com', 'YdCA8t1DRx', 'Bx879HISKW', 109, '2013-04-21 06:43:25', '2008-11-12 21:19:33', '2018-05-03 19:25:34');
INSERT INTO `users` VALUES (14, NULL, NULL, NULL, NULL, 1, '2025-03-30 00:00:00', '2025-03-30 20:20:02', NULL);

SET FOREIGN_KEY_CHECKS = 1;
