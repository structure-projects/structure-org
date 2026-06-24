package cn.structured.org.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 邀请链接查询条件
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "邀请链接查询条件")
public class InviteLinkQuery {

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "邀请码")
    private String inviteCode;

    @Schema(description = "状态: 1-有效 2-已过期 3-已禁用")
    private Integer state;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;
}