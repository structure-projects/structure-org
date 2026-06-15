package cn.structured.org.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 成员申请查询对象
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员申请查询对象")
public class MemberRequestQuery {

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "申请部门ID")
    private Long deptId;

    @Schema(description = "记录类型 1:申请加入 2:邀请加入")
    private Integer type;

    @Schema(description = "状态 1:待审核 2:已通过 3:已拒绝")
    private Integer state;
}
