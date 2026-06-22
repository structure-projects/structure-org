package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * 成员邀请请求DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "成员邀请请求DTO")
public class MemberInviteDTO {

    @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组织ID不能为空")
    private Long organizationId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "被邀请人手机号列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "被邀请人手机号不能为空")
    private List<@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String> invitePhones;

    @Schema(description = "邀请有效期（天），默认7天")
    private Integer expireDays = 7;
}