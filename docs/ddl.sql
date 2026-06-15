-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织名称',
                                `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织编码',
                                `logo` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织logo',
                                `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织描述',
                                `industry` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属行业',
                                `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织地址',
                                `contact_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
                                `state` tinyint NOT NULL DEFAULT '1' COMMENT '组织状态 1:正常 2:停用 3:冻结',
                                `type` tinyint NOT NULL DEFAULT '1' COMMENT '组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织',
                                `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否 ，1 是',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `create_by` bigint DEFAULT NULL COMMENT '创建人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `update_by` bigint DEFAULT NULL COMMENT '更新人',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `uk_org_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='组织表';

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
                        `parent_id` bigint DEFAULT '0' COMMENT '父节点id',
                        `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '父节点id路径',
                        `sort` int DEFAULT '0' COMMENT '显示顺序',
                        `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1:  启用 0:未启用',
                        `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否 ，1 是',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `create_by` bigint DEFAULT NULL COMMENT '创建人',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `update_by` bigint DEFAULT NULL COMMENT '更新人',
                        `organization_id` bigint NOT NULL COMMENT '组织ID',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `idx_org_id` (`organization_id`) USING BTREE,
                        KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '成员ID',
                          `user_id` bigint NOT NULL COMMENT '用户ID',
                          `phone` varchar(64) NOT NULL COMMENT '成员手机号',
                          `name` varchar(64) DEFAULT NULL COMMENT '姓名',
                          `sex` char(1) DEFAULT NULL COMMENT '性别,N 未知,M 男 ,F 女',
                          `dept_id` bigint DEFAULT NULL COMMENT '部门id',
                          `state` tinyint NOT NULL DEFAULT '1' COMMENT '成员状态 1:正常 2:离职 3:禁用',
                          `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否 ，1 是',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `create_by` bigint DEFAULT NULL COMMENT '创建人',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `update_by` bigint DEFAULT NULL COMMENT '更新人',
                          `organization_id` bigint NOT NULL COMMENT '组织ID',
                          PRIMARY KEY (`id`) USING BTREE,
                          KEY `idx_member_org` (`organization_id`) USING BTREE,
                          KEY `idx_member_dept` (`dept_id`) USING BTREE,
                          KEY `idx_member_state` (`state`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='成员表';

-- ----------------------------
-- ----------------------------
-- Table structure for member_request
-- ----------------------------
DROP TABLE IF EXISTS `member_request`;
CREATE TABLE `member_request` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `organization_id` bigint NOT NULL COMMENT '组织ID',
                                  `user_id` bigint NOT NULL COMMENT '用户ID',
                                  `phone` varchar(64) NOT NULL COMMENT '手机号',
                                  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
                                  `dept_id` bigint DEFAULT NULL COMMENT '申请部门ID',
                                  `type` tinyint NOT NULL DEFAULT '1' COMMENT '记录类型 1:申请加入 2:邀请加入',
                                  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '申请理由',
                                  `state` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:待审核 2:已通过 3:已拒绝',
                                  `reject_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拒绝理由',
                                  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
                                  `audit_by` bigint DEFAULT NULL COMMENT '审核人',
                                  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否 ，1 是',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `create_by` bigint DEFAULT NULL COMMENT '创建人',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `update_by` bigint DEFAULT NULL COMMENT '更新人',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `idx_org_user` (`organization_id`, `user_id`) USING BTREE,
                                  KEY `idx_request_state` (`state`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='成员邀请申请记录表';

SET FOREIGN_KEY_CHECKS = 1;
