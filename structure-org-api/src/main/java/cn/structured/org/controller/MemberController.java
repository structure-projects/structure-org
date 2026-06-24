package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.query.MemberQuery;
import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.org.service.IMemberService;
import cn.structured.org.vo.MemberVO;
import cn.structured.org.vo.UserDeptVO;
import cn.structured.org.vo.UserOrganizationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成员管理控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/member")
@Tag(name = "成员管理", description = "成员管理接口")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;

    private Long getCurrentUserId() {
        UserContextEntity userContext = UserContext.get();
        if (userContext != null && userContext.getUserId() != null) {
            return Long.parseLong(userContext.getUserId());
        }
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新成员")
    public ResResultVO<Void> update(
            @Parameter(description = "成员ID") @PathVariable Long id,
            @Valid @RequestBody MemberDTO dto) {
        memberService.update(id, dto);
        return ResultUtilSimpleImpl.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除成员")
    public ResResultVO<Void> delete(@Parameter(description = "成员ID") @PathVariable Long id) {
        memberService.delete(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取成员详情")
    public ResResultVO<MemberVO> getById(@Parameter(description = "成员ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(memberService.findById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询成员")
    public ResResultVO<ResPage<MemberVO>> page(@ParameterObject MemberQuery query, @ParameterObject ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(memberService.page(query, reqPage));
    }

    @GetMapping("/organizations")
    @Operation(summary = "获取当前用户所属组织列表", description = "获取当前登录用户所属的所有组织信息，支持分页查询")
    public ResResultVO<ResPage<UserOrganizationVO>> getUserOrganizations(
            @ParameterObject @Valid ReqPage reqPage) {
        log.info("获取当前用户所属组织列表请求, 分页参数: {}", reqPage);
        
        Long userId = getCurrentUserId();
        if (userId == null) {
            log.warn("用户未登录");
            return ResultUtilSimpleImpl.success(new ResPage<>());
        }
        
        ResPage<UserOrganizationVO> result = memberService.getUserOrganizations(userId, reqPage);
        log.info("获取当前用户所属组织列表成功, 用户ID: {}, 结果数量: {}", userId, result.getTotal());
        return ResultUtilSimpleImpl.success(result);
    }

    @GetMapping("/depts")
    @Operation(summary = "获取当前用户所属部门列表", description = "获取当前登录用户在当前租户（组织）内所属的部门信息，支持树形或扁平化展示")
    public ResResultVO<List<UserDeptVO>> getUserDepts(
            @Parameter(description = "是否树形展示，true表示树形结构，false表示扁平化列表，默认为false")
            @RequestParam(required = false, defaultValue = "false") Boolean treeMode) {
        log.info("获取当前用户所属部门列表请求, 树形模式: {}", treeMode);
        
        Long userId = getCurrentUserId();
        if (userId == null) {
            log.warn("用户未登录");
            return ResultUtilSimpleImpl.success(List.of());
        }
        
        List<UserDeptVO> result = memberService.getUserDepts(userId, treeMode);
        log.info("获取当前用户所属部门列表成功, 用户ID: {}, 结果数量: {}", userId, result.size());
        return ResultUtilSimpleImpl.success(result);
    }
}
