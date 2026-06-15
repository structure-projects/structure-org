package cn.structured.org.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员实体类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@TableName("member")
public class Member {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("phone")
    private String phone;

    @TableField("name")
    private String name;

    @TableField("sex")
    private String sex;

    @TableField("dept_id")
    private Long deptId;

    @TableField("state")
    private Integer state;

    @TableField("organization_id")
    private Long organizationId;

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
