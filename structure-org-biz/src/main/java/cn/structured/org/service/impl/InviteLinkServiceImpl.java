package cn.structured.org.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.org.assembler.InviteLinkAssembler;
import cn.structured.org.constant.OrgConstant;
import cn.structured.org.dto.InviteLinkDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.entity.InviteLink;
import cn.structured.org.entity.Organization;
import cn.structured.org.enums.InviteLinkStateEnum;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IDeptManager;
import cn.structured.org.manager.IInviteLinkManager;
import cn.structured.org.manager.IOrganizationManager;
import cn.structured.org.query.InviteLinkQuery;
import cn.structured.org.service.IInviteLinkService;
import cn.structured.org.vo.InviteLinkVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 邀请链接Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InviteLinkServiceImpl implements IInviteLinkService {

    private final IInviteLinkManager inviteLinkManager;
    private final IOrganizationManager organizationManager;
    private final IDeptManager deptManager;

    @Value("${org.invite-link-base-url:http://localhost:5173}")
    private String inviteLinkBaseUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InviteLinkVO createInviteLink(InviteLinkDTO dto) {
        Long organizationId = dto.getOrganizationId();
        Long deptId = dto.getDeptId();

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

        // 生成雪花算法唯一ID作为邀请码
        long snowflakeId = IdUtil.getSnowflakeNextId();
        String inviteCode = String.valueOf(snowflakeId);

        // 构建邀请链接
        String inviteLink = inviteLinkBaseUrl + OrgConstant.INVITE_LINK_PATH + inviteCode;

        // 计算过期时间
        int expireDays = dto.getExpireDays() != null && dto.getExpireDays() > 0 ? dto.getExpireDays() : 7;
        LocalDateTime expireTime = LocalDateTime.now().plusDays(expireDays);

        // 创建邀请链接记录
        InviteLink inviteLinkEntity = new InviteLink();
        inviteLinkEntity.setId(snowflakeId);
        inviteLinkEntity.setOrganizationId(organizationId);
        inviteLinkEntity.setOrganizationName(organization.getName());
        inviteLinkEntity.setDeptId(deptId);
        inviteLinkEntity.setDeptName(deptName);
        inviteLinkEntity.setInviteUserId(null); // TODO: 从SecurityContext获取当前用户
        inviteLinkEntity.setInviteUserName(null); // TODO: 从SecurityContext获取当前用户
        inviteLinkEntity.setInviteCode(inviteCode);
        inviteLinkEntity.setExpireTime(expireTime);
        inviteLinkEntity.setMaxUseCount(dto.getMaxUseCount() != null ? dto.getMaxUseCount() : 0);
        inviteLinkEntity.setUseCount(0);
        inviteLinkEntity.setState(InviteLinkStateEnum.VALID.getCode());

        inviteLinkManager.save(inviteLinkEntity);

        // 构建返回对象
        InviteLinkVO vo = InviteLinkAssembler.assembler(inviteLinkEntity);
        vo.setInviteLink(inviteLink);

        log.info("创建邀请链接成功, 组织: {}, 部门: {}, 邀请码: {}", organization.getName(), deptName, inviteCode);
        return vo;
    }

    @Override
    public InviteLinkVO findById(Long id) {
        InviteLink inviteLink = inviteLinkManager.getById(id);
        if (inviteLink == null) {
            throw new OrgException(OrgExceptionEnum.INVITE_LINK_NOT_FOUND);
        }
        InviteLinkVO vo = InviteLinkAssembler.assembler(inviteLink);
        vo.setInviteLink(buildInviteLink(inviteLink.getInviteCode()));
        return vo;
    }

    @Override
    public InviteLinkVO findByInviteCode(String inviteCode) {
        if (StrUtil.isBlank(inviteCode)) {
            throw new OrgException("INVITE_CODE_EMPTY", "邀请码不能为空");
        }
        LambdaQueryWrapper<InviteLink> queryWrapper = Wrappers.<InviteLink>lambdaQuery()
                .eq(InviteLink::getInviteCode, inviteCode);
        InviteLink inviteLink = inviteLinkManager.getOne(queryWrapper);
        if (inviteLink == null) {
            throw new OrgException(OrgExceptionEnum.INVITE_LINK_NOT_FOUND);
        }
        InviteLinkVO vo = InviteLinkAssembler.assembler(inviteLink);
        vo.setInviteLink(buildInviteLink(inviteLink.getInviteCode()));
        return vo;
    }

    @Override
    public ResPage<InviteLinkVO> pageByCurrentUser(InviteLinkQuery query, ReqPage reqPage) {
        log.debug("开始分页查询当前用户的邀请链接, 查询条件: {}, 分页参数: {}", query, reqPage);

        // 构建分页对象
        Page<InviteLink> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<InviteLink> queryWrapper = Wrappers.<InviteLink>lambdaQuery()
                .eq(null != query.getOrganizationId(), InviteLink::getOrganizationId, query.getOrganizationId())
                .eq(null != query.getState(), InviteLink::getState, query.getState())
                .orderByDesc(InviteLink::getCreateTime);

        // 执行分页查询
        Page<InviteLink> result = inviteLinkManager.page(page, queryWrapper);

        // 转换为响应分页对象
        return ResPageConvert.convert(result, inviteLink -> {
            InviteLinkVO vo = InviteLinkAssembler.assembler(inviteLink);
            vo.setInviteLink(buildInviteLink(inviteLink.getInviteCode()));
            return vo;
        });
    }

    @Override
    public ResPage<InviteLinkVO> page(InviteLinkQuery query, ReqPage reqPage) {
        log.debug("开始分页查询邀请链接, 查询条件: {}, 分页参数: {}", query, reqPage);

        // 构建分页对象
        Page<InviteLink> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<InviteLink> queryWrapper = Wrappers.<InviteLink>lambdaQuery()
                .eq(null != query.getOrganizationId(), InviteLink::getOrganizationId, query.getOrganizationId())
                .eq(null != query.getDeptId(), InviteLink::getDeptId, query.getDeptId())
                .eq(StrUtil.isNotBlank(query.getInviteCode()), InviteLink::getInviteCode, query.getInviteCode())
                .eq(null != query.getState(), InviteLink::getState, query.getState())
                .orderByDesc(InviteLink::getCreateTime);

        // 执行分页查询
        Page<InviteLink> result = inviteLinkManager.page(page, queryWrapper);

        // 转换为响应分页对象
        return ResPageConvert.convert(result, inviteLink -> {
            InviteLinkVO vo = InviteLinkAssembler.assembler(inviteLink);
            vo.setInviteLink(buildInviteLink(inviteLink.getInviteCode()));
            return vo;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        InviteLink inviteLink = inviteLinkManager.getById(id);
        if (inviteLink == null) {
            throw new OrgException(OrgExceptionEnum.INVITE_LINK_NOT_FOUND);
        }
        inviteLink.setState(InviteLinkStateEnum.DISABLED.getCode());
        inviteLinkManager.updateById(inviteLink);
        log.info("禁用邀请链接成功, ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        InviteLink inviteLink = inviteLinkManager.getById(id);
        if (inviteLink == null) {
            throw new OrgException(OrgExceptionEnum.INVITE_LINK_NOT_FOUND);
        }
        inviteLinkManager.removeById(id);
        log.info("删除邀请链接成功, ID: {}", id);
    }

    /**
     * 构建邀请链接
     */
    private String buildInviteLink(String inviteCode) {
        return inviteLinkBaseUrl + OrgConstant.INVITE_LINK_PATH + inviteCode;
    }
}