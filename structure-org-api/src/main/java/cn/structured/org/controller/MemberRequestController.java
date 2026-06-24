package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberAuditDTO;
import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.query.MemberRequestQuery;
import cn.structured.org.service.IMemberRequestService;
import cn.structured.org.vo.MemberRequestVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 成员申请控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/member-request")
@Tag(name = "成员申请管理", description = "成员申请管理接口")
@RequiredArgsConstructor
public class MemberRequestController {

    private final IMemberRequestService memberRequestService;

    @PostMapping
    @Operation(summary = "申请加入组织")
    public ResResultVO<Long> create(@Valid @RequestBody MemberRequestDTO dto) {
        Long id = memberRequestService.create(dto);
        return ResultUtilSimpleImpl.success(id);
    }

    @PutMapping("/{id}/audit")
    @Operation(summary = "审核申请")
    public ResResultVO<Void> audit(@RequestBody MemberAuditDTO auditDTO) {
        memberRequestService.audit(auditDTO);
        return ResultUtilSimpleImpl.success(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取申请详情")
    public ResResultVO<MemberRequestVO> getById(@Parameter(description = "申请ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(memberRequestService.findById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询申请")
    public ResResultVO<ResPage<MemberRequestVO>> page(@ParameterObject MemberRequestQuery query, @ParameterObject ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(memberRequestService.page(query, reqPage));
    }
}
