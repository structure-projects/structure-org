package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员申请VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员申请VO")
public class MemberRequestVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "组织名称")
    private String organizationName;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "申请部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "记录类型 1:申请加入 2:邀请加入")
    private Integer type;

    @Schema(description = "邀请链接ID")
    private Long inviteLinkId;

    @Schema(description = "申请理由")
    private String reason;

    @Schema(description = "状态 1:待审核 2:已通过 3:已拒绝")
    private Integer state;

    @Schema(description = "拒绝理由")
    private String rejectReason;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核人")
    private Long auditBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}
