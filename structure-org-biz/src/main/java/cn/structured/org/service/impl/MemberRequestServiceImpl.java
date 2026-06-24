package cn.structured.org.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.org.assembler.MemberRequestAssembler;
import cn.structured.org.dto.MemberAuditDTO;
import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.entity.Member;
import cn.structured.org.entity.MemberRequest;
import cn.structured.org.enums.MemberRequestStateEnum;
import cn.structured.org.enums.MemberStateEnum;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IMemberManager;
import cn.structured.org.manager.IMemberRequestManager;
import cn.structured.org.query.MemberRequestQuery;
import cn.structured.org.service.IMemberRequestService;
import cn.structured.org.vo.MemberRequestVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 成员申请Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRequestServiceImpl implements IMemberRequestService {

    private final IMemberRequestManager memberRequestManager;
    private final IMemberManager memberManager;

    @Override
    public Long create(MemberRequestDTO dto) {
        MemberRequest request = MemberRequestAssembler.assemble(dto);
        memberRequestManager.save(request);
        log.info("创建成员申请成功, 申请ID: {}", request.getId());
        return request.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(MemberAuditDTO audit) {
        Long id = audit.getId();
        Integer state = audit.getState();
        // 获取申请信息
        MemberRequest request = memberRequestManager.getById(id);

        // 验证申请状态
        if (request.getState() != MemberRequestStateEnum.PENDING.getCode().intValue()) {
            log.warn("申请状态不允许审核, 申请ID: {}, 当前状态: {}", id, request.getState());
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_STATUS_ERROR);
        }
        // 更新申请状态
        MemberRequest memberRequest = new MemberRequest();
        request.setState(state);
        request.setAuditTime(LocalDateTime.now());
        request.setRejectReason(audit.getRejectReason());
        memberRequestManager.updateById(memberRequest);
        log.info("审核成员申请成功, 申请ID: {}, 审核状态: {}", id, state);

        // 成员申请通过
        if (state == MemberRequestStateEnum.APPROVED.getCode().intValue()) {
            Member member = new Member();
            member.setUserId(request.getUserId());
            member.setPhone(request.getPhone());
            member.setName(request.getName());
            member.setDeptId(request.getDeptId());
            member.setState(MemberStateEnum.NORMAL.getCode());
            member.setOrganizationId(request.getOrganizationId());
            memberManager.save(member);
            log.info("审核通过，已创建成员");
        }
    }

    @Override
    public MemberRequestVO findById(Long id) {
        MemberRequest request = memberRequestManager.getById(id);
        return MemberRequestAssembler.assemble(request);
    }

    @Override
    public ResPage<MemberRequestVO> page(MemberRequestQuery query, ReqPage reqPage) {
        log.debug("开始分页查询成员申请, 查询条件: {}, 分页参数: {}", query, reqPage);

        // 构建分页对象
        Page<MemberRequest> page = new Page<>(reqPage.getPage(), reqPage.getSize());

        // 构建查询条件
        LambdaQueryWrapper<MemberRequest> queryWrapper = Wrappers.<MemberRequest>lambdaQuery()
                .eq(null != query.getUserId(), MemberRequest::getUserId, query.getUserId())
                .eq(null != query.getType(), MemberRequest::getType, query.getType())
                .eq(null != query.getDeptId(), MemberRequest::getDeptId, query.getDeptId())
                .eq(null != query.getState(), MemberRequest::getState, query.getState())
                .eq(StrUtil.isNotBlank(query.getPhone()), MemberRequest::getPhone, query.getPhone())
                .orderByDesc(MemberRequest::getCreateTime);

        // 执行分页查询
        Page<MemberRequest> result = memberRequestManager.page(page, queryWrapper);
        log.debug("分页查询完成, 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

        // 转换为响应分页对象
        return ResPageConvert.convert(result, MemberRequestAssembler::assemble);
    }
}