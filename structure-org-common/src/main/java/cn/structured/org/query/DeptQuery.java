package cn.structured.org.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门查询对象
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "部门查询对象")
public class DeptQuery {

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父节点id")
    private Long parentId;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "组织ID")
    private Long organizationId;
}
