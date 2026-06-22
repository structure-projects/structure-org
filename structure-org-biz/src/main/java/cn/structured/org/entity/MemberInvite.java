package cn.structured.org.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员邀请记录实体类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@TableName("member_invite")
public class MemberInvite {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("organization_id")
    private Long organizationId;

    @TableField("organization_name")
    private String organizationName;

    @TableField("dept_id")
    private Long deptId;

    @TableField("dept_name")
    private String deptName;

    @TableField("invite_user_id")
    private Long inviteUserId;

    @TableField("invite_user_name")
    private String inviteUserName;

    @TableField("invite_phone")
    private String invitePhone;

    @TableField("invite_code")
    private String inviteCode;

    @TableField("expire_time")
    private LocalDateTime expireTime;

    @TableField("state")
    private Integer state;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}