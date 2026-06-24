package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.InviteLinkDTO;
import cn.structured.org.query.InviteLinkQuery;
import cn.structured.org.service.IInviteLinkService;
import cn.structured.org.vo.InviteLinkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 邀请链接控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/invite-link")
@Tag(name = "邀请链接管理", description = "邀请链接管理接口")
@RequiredArgsConstructor
public class InviteLinkController {

    private final IInviteLinkService inviteLinkService;

    @PostMapping
    @Operation(summary = "创建邀请链接")
    public ResResultVO<InviteLinkVO> create(@Valid @RequestBody InviteLinkDTO dto) {
        InviteLinkVO result = inviteLinkService.createInviteLink(dto);
        return ResultUtilSimpleImpl.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取邀请链接详情")
    public ResResultVO<InviteLinkVO> getById(@Parameter(description = "邀请链接ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(inviteLinkService.findById(id));
    }

    @GetMapping("/code/{inviteCode}")
    @Operation(summary = "根据邀请码获取邀请链接")
    public ResResultVO<InviteLinkVO> getByInviteCode(@Parameter(description = "邀请码") @PathVariable String inviteCode) {
        return ResultUtilSimpleImpl.success(inviteLinkService.findByInviteCode(inviteCode));
    }

    @GetMapping("/my")
    @Operation(summary = "获取当前用户的邀请链接列表")
    public ResResultVO<ResPage<InviteLinkVO>> getMyInviteLinks(
            @ParameterObject InviteLinkQuery query,
            @ParameterObject ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(inviteLinkService.pageByCurrentUser(query, reqPage));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询邀请链接")
    public ResResultVO<ResPage<InviteLinkVO>> page(
            @ParameterObject InviteLinkQuery query,
            @ParameterObject ReqPage reqPage) {
        return ResultUtilSimpleImpl.success(inviteLinkService.page(query, reqPage));
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "禁用邀请链接")
    public ResResultVO<Void> disable(@Parameter(description = "邀请链接ID") @PathVariable Long id) {
        inviteLinkService.disable(id);
        return ResultUtilSimpleImpl.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除邀请链接")
    public ResResultVO<Void> delete(@Parameter(description = "邀请链接ID") @PathVariable Long id) {
        inviteLinkService.delete(id);
        return ResultUtilSimpleImpl.success(null);
    }
}