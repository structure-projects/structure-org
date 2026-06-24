package cn.structured.org.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员申请实体类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@TableName("member_request")
public class MemberRequest {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("organization_id")
    private Long organizationId;

    @TableField("user_id")
    private Long userId;

    @TableField("phone")
    private String phone;

    @TableField("name")
    private String name;

    @TableField("dept_id")
    private Long deptId;

    @TableField("type")
    private Integer type;

    @TableField("invite_link_id")
    private Long inviteLinkId;

    @TableField("reason")
    private String reason;

    @TableField("state")
    private Integer state;

    @TableField("reject_reason")
    private String rejectReason;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_by")
    private Long auditBy;

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
