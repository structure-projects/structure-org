package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员VO")
public class MemberVO {

    @Schema(description = "成员ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "成员手机号")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别,N 未知,M 男 ,F 女")
    private String sex;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "成员状态 1:正常 2:离职 3:禁用")
    private Integer state;

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}
