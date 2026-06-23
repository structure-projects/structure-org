package cn.structured.org.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.structured.org.assembler.MemberInviteAssembler;
import cn.structured.org.dto.MemberInviteConfirmDTO;
import cn.structured.org.dto.MemberInviteDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.entity.Member;
import cn.structured.org.entity.MemberInvite;
import cn.structured.org.entity.Organization;
import cn.structured.org.enums.MemberInviteStateEnum;
import cn.structured.org.enums.MemberStateEnum;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IDeptManager;
import cn.structured.org.manager.IMemberInviteManager;
import cn.structured.org.manager.IMemberManager;
import cn.structured.org.manager.IOrganizationManager;
import cn.structured.org.service.IMemberInviteService;
import cn.structured.org.vo.MemberInviteVO;
import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 成员邀请Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInviteServiceImpl implements IMemberInviteService {

    private final IMemberInviteManager memberInviteManager;
    private final IOrganizationManager organizationManager;
    private final IDeptManager deptManager;
    private final IMemberManager memberManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MemberInviteVO> invite(MemberInviteDTO dto) {
        Long organizationId = dto.getOrganizationId();
        Long deptId = dto.getDeptId();
        List<String> invitePhones = dto.getInvitePhones();

        // 查询组织信息
        Organization organization = organizationManager.getById(organizationId);
        if (organization == null) {
            throw new OrgException(OrgExceptionEnum.ORGANIZATION_NOT_FOUND);
        }

        // 查询部门信息
        String deptName = null;
        if (deptId != null) {
            Dept dept = deptManager.getById(deptId);
            if (dept == null) {
                throw new OrgException(OrgExceptionEnum.DEPT_NOT_FOUND);
            }
            deptName = dept.getName();
        }

        // 计算过期时间
        int expireDays = dto.getExpireDays() != null && dto.getExpireDays() > 0 ? dto.getExpireDays() : 7;
        LocalDateTime expireTime = LocalDateTime.now().plusDays(expireDays);

        List<MemberInviteVO> results = new ArrayList<>();

        // 为每个手机号创建邀请记录
        for (String phone : invitePhones) {
            // 生成唯一邀请码
            String inviteCode = IdUtil.simpleUUID();

            MemberInvite invite = new MemberInvite();
            invite.setId(IdUtil.getSnowflakeNextId());
            invite.setOrganizationId(organizationId);
            invite.setOrganizationName(organization.getName());
            invite.setDeptId(deptId);
            invite.setDeptName(deptName);
            UserContextEntity userContextEntity = UserContext.get();
            invite.setInviteUserId(Long.parseLong(userContextEntity.getUserId())); // TODO: 从SecurityContext获取
            invite.setInviteUserName(null); // TODO: 从SecurityContext获取
            invite.setInvitePhone(phone);
            invite.setInviteCode(inviteCode);
            invite.setExpireTime(expireTime);
            invite.setState(MemberInviteStateEnum.PENDING.getCode());

            memberInviteManager.save(invite);
            results.add(MemberInviteAssembler.assembler(invite));

            log.info("发起成员邀请成功, 邀请ID: {}, 被邀请手机号: {}", invite.getId(), phone);
        }

        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(MemberInviteConfirmDTO dto) {
        Long inviteId = dto.getInviteId();
        String inviteCode = dto.getInviteCode();

        // 查询邀请记录
        MemberInvite invite = memberInviteManager.getById(inviteId);
        if (invite == null) {
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }

        // 验证邀请码
        if (!inviteCode.equals(invite.getInviteCode())) {
            throw new OrgException("INVITE_CODE_ERROR", "邀请码错误");
        }

        // 验证邀请状态
        if (invite.getState() != MemberInviteStateEnum.PENDING.getCode()) {
            throw new OrgException("INVITE_STATE_ERROR", "邀请状态不允许确认");
        }

        // 验证是否过期
        if (invite.getExpireTime() != null && invite.getExpireTime().isBefore(LocalDateTime.now())) {
            invite.setState(MemberInviteStateEnum.EXPIRED.getCode());
            memberInviteManager.updateById(invite);
            throw new OrgException(OrgExceptionEnum.INVITE_LINK_EXPIRED);
        }

        // 验证手机号格式（被邀请人手机号必须与邀请时填写的手机号一致）
        if (!dto.getPhone().equals(invite.getInvitePhone())) {
            throw new OrgException("PHONE_MISMATCH", "手机号与邀请接收人不匹配");
        }

        // 更新邀请状态为已接收
        invite.setState(MemberInviteStateEnum.ACCEPTED.getCode());
        memberInviteManager.updateById(invite);

        // 创建正式成员
        Member member = new Member();
        member.setUserId(dto.getUserId());
        member.setPhone(dto.getPhone());
        member.setName(dto.getName());
        member.setDeptId(invite.getDeptId());
        member.setState(MemberStateEnum.NORMAL.getCode());
        member.setOrganizationId(invite.getOrganizationId());
        memberManager.save(member);

        log.info("成员邀请确认成功, 邀请ID: {}, 用户ID: {}, 手机号: {}", inviteId, dto.getUserId(), dto.getPhone());
    }

    @Override
    public MemberInviteVO findById(Long id) {
        MemberInvite invite = memberInviteManager.getById(id);
        if (invite == null) {
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        return MemberInviteAssembler.assembler(invite);
    }

    @Override
    public MemberInviteVO findByInviteCode(String inviteCode) {
        if (StrUtil.isBlank(inviteCode)) {
            throw new OrgException("INVITE_CODE_EMPTY", "邀请码不能为空");
        }
        LambdaQueryWrapper<MemberInvite> queryWrapper = Wrappers.<MemberInvite>lambdaQuery()
                .eq(MemberInvite::getInviteCode, inviteCode);
        MemberInvite invite = memberInviteManager.getOne(queryWrapper);
        if (invite == null) {
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        return MemberInviteAssembler.assembler(invite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        MemberInvite invite = memberInviteManager.getById(id);
        if (invite == null) {
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        if (invite.getState() != MemberInviteStateEnum.PENDING.getCode()) {
            throw new OrgException("INVITE_STATE_ERROR", "只有待接收状态的邀请才能取消");
        }
        invite.setState(MemberInviteStateEnum.CANCELLED.getCode());
        memberInviteManager.updateById(invite);
        log.info("取消邀请成功, 邀请ID: {}", id);
    }
}