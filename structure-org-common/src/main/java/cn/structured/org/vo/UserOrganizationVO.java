package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户组织VO")
public class UserOrganizationVO {

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "组织名称")
    private String organizationName;

    @Schema(description = "组织类型 1:企业组织 2:团队组织 3:政府组织 4:教育组织 5:其他组织")
    private Integer organizationType;

    @Schema(description = "用户在组织中的角色")
    private String role;

    @Schema(description = "加入时间")
    private LocalDateTime joinTime;

    @Schema(description = "成员状态 1:正常 2:离职 3:禁用")
    private Integer memberState;
}