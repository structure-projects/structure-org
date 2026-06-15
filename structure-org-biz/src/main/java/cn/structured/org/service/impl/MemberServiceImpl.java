package cn.structured.org.service.impl;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.assembler.MemberAssembler;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.entity.Member;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.mapper.MemberMapper;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.service.IDeptService;
import cn.structured.org.service.IMemberService;
import cn.structured.org.vo.MemberVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 成员Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    private final IDeptService deptService;

    @Override
    public Long create(MemberDTO dto) {
        Member member = MemberAssembler.toEntity(dto);
        save(member);
        log.info("创建成员成功, 成员ID: {}", member.getId());
        return member.getId();
    }

    @Override
    public void update(Long id, MemberDTO dto) {
        Member member = super.getById(id);
        if (member == null) {
            log.warn("成员不存在, 成员ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_NOT_FOUND);
        }
        MemberAssembler.updateEntity(dto, member);
        member.setId(id);
        updateById(member);
        log.info("更新成员成功, 成员ID: {}", id);
    }

    @Override
    public void delete(Long id) {
        Member member = super.getById(id);
        if (member == null) {
            log.warn("成员不存在, 成员ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_NOT_FOUND);
        }
        removeById(id);
        log.info("删除成员成功, 成员ID: {}", id);
    }


    @Override
    public MemberVO findById(Long id) {
        Member member = super.getById(id);
        if (member == null) {
            log.warn("成员不存在, 成员ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_NOT_FOUND);
        }
        return MemberAssembler.toVO(member, buildDeptNameMap(List.of(member)));
    }

    @Override
    public ResPage<MemberVO> page(MemberQuery query, ReqPage reqPage) {
        Page<Member> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (query.getUserId() != null) {
            wrapper.eq(Member::getUserId, query.getUserId());
        }
        if (query.getPhone() != null) {
            wrapper.like(Member::getPhone, query.getPhone());
        }
        if (query.getName() != null) {
            wrapper.like(Member::getName, query.getName());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(Member::getDeptId, query.getDeptId());
        }
        if (query.getState() != null) {
            wrapper.eq(Member::getState, query.getState());
        }
        if (query.getOrganizationId() != null) {
            wrapper.eq(Member::getOrganizationId, query.getOrganizationId());
        }
        wrapper.orderByDesc(Member::getCreateTime);
        Page<Member> result = page(page, wrapper);

        Map<Long, String> deptNameMap = buildDeptNameMap(result.getRecords());
        
        ResPage<MemberVO> resPage = new ResPage<>();
        resPage.setRecords(result.getRecords().stream()
                .map(member -> MemberAssembler.toVO(member, deptNameMap))
                .collect(Collectors.toList()));
        resPage.setTotal(result.getTotal());
        resPage.setCurrent(result.getCurrent());
        resPage.setSize(result.getSize());
        resPage.setPages(result.getPages());
        return resPage;
    }

    private Map<Long, String> buildDeptNameMap(List<Member> members) {
        List<Long> deptIds = members.stream()
                .filter(m -> m.getDeptId() != null)
                .map(Member::getDeptId)
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
