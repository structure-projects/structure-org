package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.DeptDTO;
import cn.structured.org.query.DeptQuery;
import cn.structured.org.service.IDeptService;
import cn.structured.org.vo.DeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/dept")
@Tag(name = "部门管理", description = "部门管理接口")
@RequiredArgsConstructor
public class DeptController {

    private final IDeptService deptService;

    @PostMapping
    @Operation(summary = "创建部门")
    public ResResultVO<Long> create(@Valid @RequestBody DeptDTO dto) {
        Long id = deptService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新部门")
    public ResResultVO<DeptVO> update(
            @Parameter(description = "部门ID") @PathVariable Long id,
            @Valid @RequestBody DeptDTO dto) {
        deptService.update(id, dto);
        return ResultUtilSimpleImpl.success(deptService.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    public ResResultVO<DeptVO> delete(@Parameter(description = "部门ID") @PathVariable Long id) {
        DeptVO vo = deptService.findById(id);
        deptService.delete(id);
        return ResultUtilSimpleImpl.success(vo);
    }


    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情")
    public ResResultVO<DeptVO> getById(@Parameter(description = "部门ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(deptService.findById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询部门")
    public ResResultVO<ResPage<DeptVO>> page(DeptQuery query, ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(deptService.page(query, reqPage));
    }

    @GetMapping("/options")
    @Operation(summary = "获取部门下拉选")
    public ResResultVO<List<OptionVO>> options(@Parameter(description = "组织ID") @RequestParam Long organizationId) {
        return ResultUtilSimpleImpl.success(deptService.options(organizationId));
    }

    @GetMapping("/tree")
    @Operation(summary = "获取部门树")
    public ResResultVO<List<DeptVO>> tree(@Parameter(description = "组织ID") @RequestParam Long organizationId) {
        return ResultUtilSimpleImpl.success(deptService.tree(organizationId));
    }
}
