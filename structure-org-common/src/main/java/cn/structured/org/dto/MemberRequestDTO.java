package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 成员申请DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员申请DTO")
public class MemberRequestDTO {

    @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组织ID不能为空")
    private Long organizationId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "申请部门ID")
    private Long deptId;

    @Schema(description = "记录类型 1:申请加入 2:邀请加入", defaultValue = "1")
    private Integer type;

    @Schema(description = "申请理由")
    private String reason;
}
