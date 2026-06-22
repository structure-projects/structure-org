package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.service.IMemberService;
import cn.structured.org.vo.MemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 成员管理控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/member")
@Tag(name = "成员管理", description = "成员管理接口")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;

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
}
