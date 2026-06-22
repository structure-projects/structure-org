package cn.structured.org.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structured.org.dto.MemberInviteConfirmDTO;
import cn.structured.org.dto.MemberInviteDTO;
import cn.structured.org.service.IMemberInviteService;
import cn.structured.org.vo.MemberInviteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成员邀请控制器
 *
 * @author chuck
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/member-invite")
@Tag(name = "成员邀请管理", description = "成员邀请管理接口")
@RequiredArgsConstructor
public class MemberInviteController {

    private final IMemberInviteService memberInviteService;

    @PostMapping
    @Operation(summary = "发起成员邀请（批量）")
    public ResResultVO<List<MemberInviteVO>> invite(@Valid @RequestBody MemberInviteDTO dto) {
        List<MemberInviteVO> results = memberInviteService.invite(dto);
        return ResultUtilSimpleImpl.success(results);
    }

    @PostMapping("/confirm")
    @Operation(summary = "确认加入（接受邀请）")
    public ResResultVO<Void> confirm(@Valid @RequestBody MemberInviteConfirmDTO dto) {
        memberInviteService.confirm(dto);
        return ResultUtilSimpleImpl.success(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取邀请记录详情")
    public ResResultVO<MemberInviteVO> getById(@Parameter(description = "邀请记录ID") @PathVariable Long id) {
        return ResultUtilSimpleImpl.success(memberInviteService.findById(id));
    }

    @GetMapping("/code/{inviteCode}")
    @Operation(summary = "根据邀请码获取邀请记录")
    public ResResultVO<MemberInviteVO> getByInviteCode(@Parameter(description = "邀请码") @PathVariable String inviteCode) {
        return ResultUtilSimpleImpl.success(memberInviteService.findByInviteCode(inviteCode));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消邀请")
    public ResResultVO<Void> cancel(@Parameter(description = "邀请记录ID") @PathVariable Long id) {
        memberInviteService.cancel(id);
        return ResultUtilSimpleImpl.success(null);
    }
}