package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 成员DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员DTO")
public class MemberDTO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "成员手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别,N 未知,M 男 ,F 女")
    private String sex;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "成员状态 1:正常 2:离职 3:禁用", defaultValue = "1")
    private Integer state;

    @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long organizationId;
}
