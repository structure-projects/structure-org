-- H2 compatible schema for testing

-- Organization table
CREATE TABLE IF NOT EXISTS organization (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(128) NOT NULL COMMENT '组织名称',
    code VARCHAR(64) COMMENT '组织编码',
    logo VARCHAR(512) COMMENT '组织logo',
    description VARCHAR(512) COMMENT '组织描述',
    industry VARCHAR(64) COMMENT '所属行业',
    address VARCHAR(255) COMMENT '组织地址',
    contact_phone VARCHAR(64) COMMENT '联系电话',
    state TINYINT NOT NULL DEFAULT 1 COMMENT '组织状态 1:正常 2:停用 3:冻结',
    type TINYINT NOT NULL DEFAULT 1 COMMENT '组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_org_code ON organization(code);

-- Department table
CREATE TABLE IF NOT EXISTS dept (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(64) DEFAULT '' COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父节点id',
    tree_path VARCHAR(255) DEFAULT '' COMMENT '父节点id路径',
    sort INT DEFAULT 0 COMMENT '显示顺序',
    is_enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用 1: 启用 0:未启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_dept_org_id ON dept(organization_id);
CREATE INDEX IF NOT EXISTS idx_dept_parent_id ON dept(parent_id);

-- Member table
CREATE TABLE IF NOT EXISTS member (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '成员ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    phone VARCHAR(64) NOT NULL COMMENT '成员手机号',
    name VARCHAR(64) COMMENT '姓名',
    sex CHAR(1) COMMENT '性别,N 未知,M 男 ,F 女',
    dept_id BIGINT COMMENT '部门id',
    state TINYINT NOT NULL DEFAULT 1 COMMENT '成员状态 1:正常 2:离职 3:禁用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_member_org ON member(organization_id);
CREATE INDEX IF NOT EXISTS idx_member_dept ON member(dept_id);
CREATE INDEX IF NOT EXISTS idx_member_state ON member(state);

-- Member request table
CREATE TABLE IF NOT EXISTS member_request (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    phone VARCHAR(64) NOT NULL COMMENT '手机号',
    name VARCHAR(64) COMMENT '姓名',
    dept_id BIGINT COMMENT '申请部门ID',
    type TINYINT NOT NULL DEFAULT 1 COMMENT '记录类型 1:申请加入 2:邀请加入',
    invite_link_id BIGINT COMMENT '邀请链接ID',
    reason VARCHAR(512) COMMENT '申请理由',
    state TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1:待审核 2:已通过 3:已拒绝',
    reject_reason VARCHAR(512) COMMENT '拒绝理由',
    audit_time TIMESTAMP NULL COMMENT '审核时间',
    audit_by BIGINT COMMENT '审核人',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_request_org_user ON member_request(organization_id, user_id);
CREATE INDEX IF NOT EXISTS idx_request_state ON member_request(state);
CREATE INDEX IF NOT EXISTS idx_request_invite_link ON member_request(invite_link_id);

-- Invite link table
CREATE TABLE IF NOT EXISTS invite_link (
    id BIGINT NOT NULL COMMENT '主键（雪花算法ID）',
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    organization_name VARCHAR(128) COMMENT '组织名称',
    dept_id BIGINT COMMENT '部门ID',
    dept_name VARCHAR(64) COMMENT '部门名称',
    invite_user_id BIGINT COMMENT '邀请人用户ID',
    invite_user_name VARCHAR(64) COMMENT '邀请人姓名',
    invite_code VARCHAR(64) NOT NULL COMMENT '邀请码',
    expire_time TIMESTAMP NULL COMMENT '过期时间',
    max_use_count INT DEFAULT 0 COMMENT '最大使用次数，0表示不限制',
    use_count INT DEFAULT 0 COMMENT '已使用次数',
    state TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1:有效 2:已过期 3:已禁用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_il_invite_code ON invite_link(invite_code);
CREATE INDEX IF NOT EXISTS idx_il_org_id ON invite_link(organization_id);
CREATE INDEX IF NOT EXISTS idx_il_state ON invite_link(state);

-- Member invite table
CREATE TABLE IF NOT EXISTS member_invite (
    id BIGINT NOT NULL COMMENT '主键（雪花算法ID）',
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    organization_name VARCHAR(128) COMMENT '组织名称',
    dept_id BIGINT COMMENT '部门ID',
    dept_name VARCHAR(64) COMMENT '部门名称',
    invite_user_id BIGINT COMMENT '邀请人用户ID',
    invite_user_name VARCHAR(64) COMMENT '邀请人姓名',
    invite_phone VARCHAR(64) NOT NULL COMMENT '被邀请人手机号',
    invite_code VARCHAR(64) NOT NULL COMMENT '邀请码',
    expire_time TIMESTAMP NULL COMMENT '过期时间',
    state TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1:待接收 2:已接收 3:已过期 4:已取消',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0 否 ，1 是',
    create_time TIMESTAMP NULL,
    create_by BIGINT NULL,
    update_time TIMESTAMP NULL,
    update_by BIGINT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_mi_invite_phone ON member_invite(invite_phone);
CREATE INDEX IF NOT EXISTS idx_mi_invite_code ON member_invite(invite_code);
CREATE INDEX IF NOT EXISTS idx_mi_org_id ON member_invite(organization_id);
CREATE INDEX IF NOT EXISTS idx_mi_state ON member_invite(state);
