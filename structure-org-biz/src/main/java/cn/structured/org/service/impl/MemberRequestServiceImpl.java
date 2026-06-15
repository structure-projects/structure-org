package cn.structured.org.service.impl;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.assembler.MemberRequestAssembler;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.entity.MemberRequest;
import cn.structured.org.entity.Organization;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.mapper.MemberRequestMapper;
import cn.structured.org.query.MemberRequestQuery;
import cn.structured.org.service.IDeptService;
import cn.structured.org.service.IMemberRequestService;
import cn.structured.org.service.IMemberService;
import cn.structured.org.service.IOrganizationService;
import cn.structured.org.vo.MemberRequestVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 成员申请Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRequestServiceImpl extends ServiceImpl<MemberRequestMapper, MemberRequest> implements IMemberRequestService {

    private final IOrganizationService organizationService;
    private final IDeptService deptService;

    @Lazy
    private final IMemberService memberService;

    @Override
    public Long create(MemberRequestDTO dto) {
        MemberRequest request = MemberRequestAssembler.toEntity(dto);
        save(request);
        log.info("创建成员申请成功, 申请ID: {}", request.getId());
        return request.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long id, Integer state, String rejectReason) {
        MemberRequest request = super.getById(id);
        if (request == null) {
            log.warn("成员申请不存在, 申请ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        if (request.getState() != 1) {
            log.warn("申请状态不允许审核, 申请ID: {}, 当前状态: {}", id, request.getState());
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_STATUS_ERROR);
        }
        request.setState(state);
        request.setAuditTime(LocalDateTime.now());
        request.setRejectReason(rejectReason);
        updateById(request);
        log.info("审核成员申请成功, 申请ID: {}, 审核状态: {}", id, state);

        if (state == 2) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUserId(request.getUserId());
            memberDTO.setPhone(request.getPhone());
            memberDTO.setName(request.getName());
            memberDTO.setDeptId(request.getDeptId());
            memberDTO.setState(1);
            memberDTO.setOrganizationId(request.getOrganizationId());
            memberService.create(memberDTO);
            log.info("审核通过，已创建成员");
        }
    }

    @Override
    public MemberRequestVO findById(Long id) {
        MemberRequest request = super.getById(id);
        if (request == null) {
            log.warn("成员申请不存在, 申请ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        return MemberRequestAssembler.toVO(request,
                buildOrgNameMap(List.of(request)), buildDeptNameMap(List.of(request)));
    }

    @Override
    public ResPage<MemberRequestVO> page(MemberRequestQuery query, ReqPage reqPage) {
        log.debug("开始分页查询成员申请, 查询条件: {}, 分页参数: {}", query, reqPage);
        
        // 构建分页对象
        Page<MemberRequest> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<MemberRequest> wrapper = buildQueryWrapper(query);
        
        // 执行分页查询
        Page<MemberRequest> result = page(page, wrapper);
        log.debug("分页查询完成, 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());
        
        // 构建名称映射（用于关联组织和部门名称）
        List<MemberRequest> records = result.getRecords();
        Map<Long, String> orgNameMap = buildOrgNameMap(records);
        Map<Long, String> deptNameMap = buildDeptNameMap(records);
        
        // 转换为响应分页对象
        ResPage<MemberRequestVO> resPage = convertToResPage(result, orgNameMap, deptNameMap);
        
        return resPage;
    }

    /**
     * 构建成员申请查询条件
     *
     * @param query 查询条件对象
     * @return LambdaQueryWrapper 查询包装器
     */
    private LambdaQueryWrapper<MemberRequest> buildQueryWrapper(MemberRequestQuery query) {
        LambdaQueryWrapper<MemberRequest> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getOrganizationId() != null) {
            wrapper.eq(MemberRequest::getOrganizationId, query.getOrganizationId());
            log.debug("添加组织ID查询条件: {}", query.getOrganizationId());
        }
        if (query.getUserId() != null) {
            wrapper.eq(MemberRequest::getUserId, query.getUserId());
            log.debug("添加用户ID查询条件: {}", query.getUserId());
        }
        if (query.getPhone() != null) {
            wrapper.like(MemberRequest::getPhone, query.getPhone());
            log.debug("添加手机号模糊查询条件: {}", query.getPhone());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(MemberRequest::getDeptId, query.getDeptId());
            log.debug("添加部门ID查询条件: {}", query.getDeptId());
        }
        if (query.getType() != null) {
            wrapper.eq(MemberRequest::getType, query.getType());
            log.debug("添加申请类型查询条件: {}", query.getType());
        }
        if (query.getState() != null) {
            wrapper.eq(MemberRequest::getState, query.getState());
            log.debug("添加申请状态查询条件: {}", query.getState());
        }
        
        // 默认按创建时间降序排序
        wrapper.orderByDesc(MemberRequest::getCreateTime);
        
        return wrapper;
    }

    /**
     * 将MyBatis Plus分页结果转换为统一响应分页对象
     *
     * @param result      MyBatis Plus分页结果
     * @param orgNameMap  组织名称映射
     * @param deptNameMap 部门名称映射
     * @return ResPage 统一响应分页对象
     */
    private ResPage<MemberRequestVO> convertToResPage(Page<MemberRequest> result, 
                                                      Map<Long, String> orgNameMap, 
                                                      Map<Long, String> deptNameMap) {
        ResPage<MemberRequestVO> resPage = new ResPage<>();
        
        resPage.setRecords(result.getRecords().stream()
                .map(request -> MemberRequestAssembler.toVO(request, orgNameMap, deptNameMap))
                .collect(Collectors.toList()));
        resPage.setTotal(result.getTotal());
        resPage.setCurrent(result.getCurrent());
        resPage.setSize(result.getSize());
        resPage.setPages(result.getPages());
        
        return resPage;
    }

    private Map<Long, String> buildOrgNameMap(List<MemberRequest> requests) {
        List<Long> orgIds = requests.stream()
                .filter(r -> r.getOrganizationId() != null)
                .map(MemberRequest::getOrganizationId)
                .distinct()
                .collect(Collectors.toList());
        if (orgIds.isEmpty()) {
            return Map.of();
        }
        List<Organization> orgs = organizationService.listByIds(orgIds);
        return orgs.stream()
                .collect(Collectors.toMap(Organization::getId, Organization::getName));
    }

    private Map<Long, String> buildDeptNameMap(List<MemberRequest> requests) {
        List<Long> deptIds = requests.stream()
                .filter(r -> r.getDeptId() != null)
                .map(MemberRequest::getDeptId)
                .distinct()
                .collect(Collectors.toList());
        if (deptIds.isEmpty()) {
            return Map.of();
        }
        List<Dept> depts = deptService.listByIds(deptIds);
        return depts.stream()
                .collect(Collectors.toMap(Dept::getId, Dept::getName));
    }
}
