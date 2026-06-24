package cn.structured.org.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.org.assembler.MemberAssembler;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.entity.Member;
import cn.structured.org.manager.IDeptManager;
import cn.structured.org.manager.IMemberManager;
import cn.structured.org.mapper.MemberMapper;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.service.IMemberService;
import cn.structured.org.vo.MemberVO;
import cn.structured.org.vo.UserDeptVO;
import cn.structured.org.vo.UserOrganizationVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
public class MemberServiceImpl implements IMemberService {

    private final IMemberManager memberManager;
    private final MemberMapper memberMapper;
    private final IDeptManager deptManager;

    @Override
    public Long create(MemberDTO dto) {
        Member member = MemberAssembler.assemble(dto);
        memberManager.save(member);
        log.info("创建成员成功, 成员ID: {}", member.getId());
        return member.getId();
    }

    @Override
    public void update(Long id, MemberDTO dto) {
        Member updateMember = MemberAssembler.assemble(dto);
        updateMember.setId(id);
        memberManager.updateById(updateMember);
        log.info("更新成员成功, 成员ID: {}", id);
    }

    @Override
    public void delete(Long id) {
        memberManager.removeById(id);
        log.info("删除成员成功, 成员ID: {}", id);
    }


    @Override
    public MemberVO findById(Long id) {
        Member member = memberManager.getById(id);
        return MemberAssembler.assemble(member);
    }

    @Override
    public ResPage<MemberVO> page(MemberQuery query, ReqPage reqPage) {
        log.info("查询成员列表, 查询参数: {}", query);
        Page<Member> page = new Page<>(reqPage.getPage(), reqPage.getSize());

        // 构建查询条件
        LambdaQueryWrapper<Member> queryWrapper = Wrappers.<Member>lambdaQuery()
                .eq(null != query.getUserId(), Member::getUserId, query.getUserId())
                .eq(null != query.getDeptId(), Member::getDeptId, query.getDeptId())
                .eq(null != query.getState(), Member::getState, query.getState())
                .like(StrUtil.isNotBlank(query.getPhone()), Member::getPhone, query.getPhone())
                .like(StrUtil.isNotBlank(query.getName()), Member::getName, query.getName())
                .orderByDesc(Member::getCreateTime);
        Page<Member> result = memberManager.page(page, queryWrapper);
        return ResPageConvert.convert(result, MemberAssembler::assemble);
    }

    @Override
    public ResPage<UserOrganizationVO> getUserOrganizations(Long userId, ReqPage reqPage) {
        log.info("查询用户所属组织列表, 用户ID: {}, 分页参数: {}", userId, reqPage);
        
        if (userId == null) {
            log.warn("用户ID为空");
            Page<UserOrganizationVO> emptyPage = new Page<>(1, 10);
            emptyPage.setTotal(0);
            return ResPageConvert.convert(emptyPage, vo -> vo);
        }

        List<UserOrganizationVO> allOrgList = memberMapper.selectUserOrganizations(userId);
        
        int pageSize = reqPage.getSize();
        int pageNum = reqPage.getPage();
        
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 50) {
            pageSize = 50;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }

        int total = allOrgList.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<UserOrganizationVO> pageList;
        if (fromIndex >= total) {
            pageList = new ArrayList<>();
        } else {
            pageList = new ArrayList<>(allOrgList.subList(fromIndex, toIndex));
        }

        Page<UserOrganizationVO> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setTotal(total);
        resultPage.setRecords(pageList);
        return ResPageConvert.convert(resultPage, vo -> vo);
    }

    @Override
    public List<UserDeptVO> getUserDepts(Long userId, Boolean treeMode) {
        log.info("查询用户部门列表, 用户ID: {}, 树形模式: {}", userId, treeMode);
        
        if (userId == null) {
            log.warn("用户ID为空");
            return new ArrayList<>();
        }

        List<Member> memberList = memberManager.list(Wrappers.<Member>lambdaQuery()
                .eq(Member::getUserId, userId)
                .eq(Member::getState, 1));

        if (CollUtil.isEmpty(memberList)) {
            return new ArrayList<>();
        }

        List<Long> deptIds = memberList.stream()
                .map(Member::getDeptId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(deptIds)) {
            return new ArrayList<>();
        }

        List<Dept> deptList = deptManager.list(Wrappers.<Dept>lambdaQuery()
                .in(Dept::getId, deptIds)
                .eq(Dept::getEnabled, true)
                .orderByAsc(Dept::getSort));

        List<UserDeptVO> result = deptList.stream()
                .map(this::convertToUserDeptVO)
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(treeMode)) {
            return buildDeptTree(result);
        }

        return result;
    }

    private UserDeptVO convertToUserDeptVO(Dept dept) {
        UserDeptVO vo = new UserDeptVO();
        vo.setDeptId(dept.getId());
        vo.setDeptName(dept.getName());
        vo.setParentId(dept.getParentId());
        vo.setOrganizationId(dept.getOrganizationId());
        vo.setCreateTime(dept.getCreateTime());

        String treePath = dept.getTreePath();
        if (StrUtil.isNotBlank(treePath)) {
            int level = treePath.length() - treePath.replace(",", "").length();
            vo.setLevel(level);
        } else {
            vo.setLevel(1);
        }

        return vo;
    }

    private List<UserDeptVO> buildDeptTree(List<UserDeptVO> deptList) {
        if (CollUtil.isEmpty(deptList)) {
            return new ArrayList<>();
        }

        Map<Long, UserDeptVO> deptMap = new HashMap<>();
        List<UserDeptVO> rootDepts = new ArrayList<>();

        for (UserDeptVO dept : deptList) {
            deptMap.put(dept.getDeptId(), dept);
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                rootDepts.add(dept);
            }
        }

        for (UserDeptVO dept : deptList) {
            Long parentId = dept.getParentId();
            if (parentId != null && parentId != 0) {
                UserDeptVO parent = deptMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(dept);
                }
            }
        }

        return rootDepts;
    }
}
