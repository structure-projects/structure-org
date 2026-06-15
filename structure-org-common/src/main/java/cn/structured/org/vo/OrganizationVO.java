package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 组织VO
 *
 * @author chuck
 * @since 2024-01-01
 */
@Data
@Schema(description = "组织VO")
public class OrganizationVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "组织名称")
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

    @Schema(description = "组织状态 1:正常 2:停用 3:冻结")
    private Integer state;

    @Schema(description = "组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织")
    private Integer type;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateBy;
}
