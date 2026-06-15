package cn.structured.org.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 成员查询对象
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员查询对象")
public class MemberQuery {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "成员手机号")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "成员状态 1:正常 2:离职 3:禁用")
    private Integer state;

    @Schema(description = "组织ID")
    private Long organizationId;
}
