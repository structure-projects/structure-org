package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 邀请链接创建DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "邀请链接创建DTO")
public class InviteLinkDTO {

    @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组织ID不能为空")
    private Long organizationId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "过期天数，默认7天")
    private Integer expireDays = 7;

    @Schema(description = "最大使用次数，0表示不限制")
    private Integer maxUseCount = 0;
}