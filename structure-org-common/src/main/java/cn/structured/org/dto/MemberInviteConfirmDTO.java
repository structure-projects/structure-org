package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 成员邀请确认DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员邀请确认DTO")
public class MemberInviteConfirmDTO {

    @Schema(description = "邀请记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "邀请记录ID不能为空")
    private Long inviteId;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}