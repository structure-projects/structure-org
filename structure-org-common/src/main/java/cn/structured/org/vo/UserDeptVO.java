package cn.structured.org.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "用户部门VO")
public class UserDeptVO {

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门层级")
    private Integer level;

    @Schema(description = "部门负责人ID")
    private Long managerId;

    @Schema(description = "部门负责人姓名")
    private String managerName;

    @Schema(description = "用户在该部门中的职位")
    private String position;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "组织ID")
    private Long organizationId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子部门列表")
    private List<UserDeptVO> children;
}