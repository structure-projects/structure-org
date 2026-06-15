package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.query.OrganizationQuery;
import cn.structured.org.service.IOrganizationService;
import cn.structured.org.vo.OrganizationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/organization")
@Tag(name = "组织管理", description = "组织管理接口")
@RequiredArgsConstructor
public class OrganizationController {

    private final IOrganizationService organizationService;

    @PostMapping
    @Operation(summary = "创建组织")
    public ResResultVO<Long> create(@Valid @RequestBody OrganizationDTO dto) {
        Long id = organizationService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新组织")
    public ResResultVO<OrganizationVO> update(
            @Parameter(description = "组织ID") @PathVariable Long id,
            @Valid @RequestBody OrganizationDTO dto) {
        organizationService.update(id, dto);
        return ResultUtilSimpleImpl.success(organizationService.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除组织")
    public ResResultVO<OrganizationVO> delete(@Parameter(description = "组织ID") @PathVariable Long id) {
        OrganizationVO vo = organizationService.findById(id);
        organizationService.delete(id);
        return ResultUtilSimpleImpl.success(vo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取组织详情")
    public ResResultVO<OrganizationVO> getById(@Parameter(description = "组织ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(organizationService.findById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询组织")
    public ResResultVO<ResPage<OrganizationVO>> page(OrganizationQuery query, ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(organizationService.page(query, reqPage));
    }
}
