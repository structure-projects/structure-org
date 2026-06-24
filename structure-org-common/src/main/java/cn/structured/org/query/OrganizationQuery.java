package cn.structured.org.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 组织查询对象
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "组织查询对象")
public class OrganizationQuery {

    @Schema(description = "组织名称")
    private String name;

    @Schema(description = "组织编码")
    private String code;

    @Schema(description = "组织状态 1:正常 2:停用 3:冻结")
    private Integer state;

    @Schema(description = "组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织")
    private Integer type;

    @Schema(description = "所属行业")
    private String industry;
}
