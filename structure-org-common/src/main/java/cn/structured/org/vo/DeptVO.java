package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "部门VO")
public class DeptVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父节点id")
    private Long parentId;

    @Schema(description = "父节点id路径")
    private String treePath;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;

    @Schema(description = "子部门列表")
    private List<DeptVO> children;
}
