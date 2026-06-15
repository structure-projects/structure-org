package cn.structured.org.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 部门DTO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "部门DTO")
public class DeptDTO {

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "部门名称不能为空")
    private String name;

    @Schema(description = "父节点id", defaultValue = "0")
    private Long parentId;

    @Schema(description = "显示顺序", defaultValue = "0")
    private Integer sort;

    @Schema(description = "是否启用", defaultValue = "true")
    private Boolean enabled;

}
