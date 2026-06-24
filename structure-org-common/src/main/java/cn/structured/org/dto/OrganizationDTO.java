package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 组织DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "组织DTO")
public class OrganizationDTO {

    @Schema(description = "组织名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组织名称不能为空")
    private String name;

    @Schema(description = "组织编码")
    private String code;

    @Schema(description = "组织logo")
    private String logo;

    @Schema(description = "组织描述")
    private String description;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "组织地址")
    private String address;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "组织状态 1:正常 2:停用 3:冻结", defaultValue = "1")
    private Integer state;

    @Schema(description = "组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织", defaultValue = "1")
    private Integer type;
}
