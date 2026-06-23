package cn.structured.org.service.impl;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.assembler.DeptAssembler;
import cn.structured.org.dto.DeptDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IDeptManager;
import cn.structured.org.query.DeptQuery;
import cn.structured.org.service.IDeptService;
import cn.structured.org.vo.DeptVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@AllArgsConstructor
public class DeptServiceImpl implements IDeptService {

    private final IDeptManager deptManager;

    @Override
    public Long create(DeptDTO dto) {
        Dept dept = DeptAssembler.assembler(dto);
        if (dto.getParentId() != null && dto.getParentId() > 0) {
            Dept parent = deptManager.getById(dto.getParentId());
            if (parent == null) {
                log.warn("父部门不存在, 父部门ID: {}", dto.getParentId());
                throw new OrgException(OrgExceptionEnum.DEPT_PARENT_NOT_FOUND);
            }
            dept.setTreePath(parent.getTreePath() + parent.getId() + ",");
        } else {
            dept.setTreePath(",");
            dept.setParentId(0L);
        }
        deptManager.save(dept);
        dept.setTreePath(dept.getTreePath() + dept.getId() + ",");
        deptManager.updateById(dept);
        log.info("创建部门成功, 部门ID: {}", dept.getId());
        return dept.getId();
    }

    @Override
    public void update(Long id, DeptDTO dto) {
        Dept dept = deptManager.getById(id);
        Long oldParentId = dept.getParentId();
        Dept udpateDept = DeptAssembler.assembler(dto);

        if (!oldParentId.equals(udpateDept.getParentId())) {
            if (udpateDept.getParentId() != null && dept.getParentId() > 0) {
                Dept parent = deptManager.getById(dept.getParentId());
                if (parent == null) {
                    log.warn("父部门不存在, 父部门ID: {}", dept.getParentId());
                    throw new OrgException(OrgExceptionEnum.DEPT_PARENT_NOT_FOUND);
                }
                udpateDept.setTreePath(parent.getTreePath() + parent.getId() + "," + id + ",");
            } else {
                udpateDept.setTreePath("," + id + ",");
                udpateDept.setParentId(0L);
            }
            updateTreePath(id, dept.getTreePath());
        }
        deptManager.updateById(udpateDept);
        log.info("更新部门成功, 部门ID: {}", id);
    }

    private void updateTreePath(Long parentId, String newTreePath) {
        List<Dept> children = deptManager.list(new LambdaQueryWrapper<Dept>()
                .like(Dept::getTreePath, "," + parentId + ","));
        for (Dept child : children) {
            String oldPath = child.getTreePath();
            String newPath = newTreePath + oldPath.substring(oldPath.indexOf("," + parentId + ",") + String.valueOf(parentId).length() + 2);
            child.setTreePath(newPath);
            deptManager.updateById(child);
            updateTreePath(child.getId(), newPath);
        }
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            long count = deptManager.count(new LambdaQueryWrapper<Dept>().eq(Dept::getParentId, id));
            if (count > 0) {
                log.warn("存在子部门, 无法删除, 部门ID: {}", id);
                throw new OrgException(OrgExceptionEnum.DEPT_HAS_CHILDREN);
            }
        }
        deptManager.removeByIds(ids);
        log.info("批量删除部门成功, 部门ID列表: {}", ids);
    }

    @Override
    public DeptVO findById(Long id) {
        Dept dept = deptManager.getById(id);
        return DeptAssembler.assembler(dept);
    }

    @Override
    public ResPage<DeptVO> page(DeptQuery query, ReqPage reqPage) {
        Page<Dept> page = new Page<>(reqPage.getPage(), reqPage.getSize());
        // 构建查询条件
        LambdaQueryWrapper<Dept> wrapper = Wrappers.<Dept>lambdaQuery()
                .like(query.getName() != null, Dept::getName, query.getName())
                .eq(query.getParentId() != null, Dept::getParentId, query.getParentId())
                .eq(query.getEnabled() != null, Dept::getEnabled, query.getEnabled())
                .eq(query.getOrganizationId() != null, Dept::getOrganizationId, query.getOrganizationId())
                .orderByAsc(Dept::getSort);
        Page<Dept> result = deptManager.page(page, wrapper);

        ResPage<DeptVO> resPage = new ResPage<>();
        resPage.setRecords(result.convert(DeptAssembler::assembler).getRecords());
        resPage.setTotal(result.getTotal());
        resPage.setCurrent(result.getCurrent());
        resPage.setSize(result.getSize());
        resPage.setPages(result.getPages());
        return resPage;
    }

    @Override
    public List<DeptVO> tree(Long organizationId, String keywords, Boolean enabled) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();

        // 添加组织ID过滤（可选）
        if (organizationId != null) {
            wrapper.eq(Dept::getOrganizationId, organizationId);
        }

        // 添加关键字搜索（可选）
        if (keywords != null && !keywords.trim().isEmpty()) {
            wrapper.like(Dept::getName, keywords);
        }

        // 添加启用状态过滤（可选）
        if (enabled != null) {
            wrapper.eq(Dept::getEnabled, enabled);
        }

        wrapper.orderByAsc(Dept::getSort);

        List<Dept> depts = deptManager.list(wrapper);

        List<DeptVO> voList = depts.stream().map(DeptAssembler::assembler).toList();

        Map<Long, DeptVO> voMap = voList.stream()
                .collect(Collectors.toMap(DeptVO::getId, vo -> vo));

        List<DeptVO> result = new ArrayList<>();
        for (DeptVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                result.add(vo);
            } else {
                DeptVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        }
        return result;
    }

    @Override
    public List<OptionVO> options(Long organizationId) {
        return deptManager.list(new LambdaQueryWrapper<Dept>()
                        .eq(Dept::getOrganizationId, organizationId)
                        .eq(Dept::getEnabled, true)
                        .orderByAsc(Dept::getSort)).stream()
                .map(dept -> {
                    OptionVO option = new OptionVO();
                    option.setValue(dept.getId());
                    option.setLabel(dept.getName());
                    return option;
                })
                .collect(Collectors.toList());
    }
}
