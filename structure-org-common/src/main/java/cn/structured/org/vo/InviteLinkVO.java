package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 邀请链接VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "邀请链接VO")
public class InviteLinkVO {

    @Schema(description = "邀请链接ID")
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

    @Schema(description = "邀请码")
    private String inviteCode;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "最大使用次数")
    private Integer maxUseCount;

    @Schema(description = "已使用次数")
    private Integer useCount;

    @Schema(description = "状态 1:有效 2:已过期 3:已禁用")
    private Integer state;

    @Schema(description = "邀请链接")
    private String inviteLink;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}