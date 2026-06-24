package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成员邀请记录VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员邀请记录VO")
public class MemberInviteVO {

    @Schema(description = "邀请记录ID")
    private Long id;

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "组织名称")
    private String organizationName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "邀请人用户ID")
    private Long inviteUserId;

    @Schema(description = "邀请人姓名")
    private String inviteUserName;

    @Schema(description = "被邀请人手机号")
    private String invitePhone;

    @Schema(description = "邀请码")
    private String inviteCode;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "状态: 1-待接收 2-已接收 3-已过期 4-已取消")
    private Integer state;

    @Schema(description = "状态描述")
    private String stateDesc;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}