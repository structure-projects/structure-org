package cn.structured.org.assembler;

import cn.structured.org.dto.DeptDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.vo.DeptVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeptAssembler {

    private DeptAssembler() {
    }

    public static DeptVO toVO(Dept dept) {
        if (dept == null) {
            return null;
        }
        DeptVO vo = new DeptVO();
        vo.setId(dept.getId());
        vo.setName(dept.getName());
        vo.setParentId(dept.getParentId());
        vo.setTreePath(dept.getTreePath());
        vo.setSort(dept.getSort());
        vo.setEnabled(dept.getEnabled());
        vo.setOrganizationId(dept.getOrganizationId());
        vo.setCreateTime(dept.getCreateTime());
        vo.setCreateBy(dept.getCreateBy());
        vo.setUpdateTime(dept.getUpdateTime());
        vo.setUpdateBy(dept.getUpdateBy());
        return vo;
    }

    public static List<DeptVO> toVOList(List<Dept> depts) {
        if (depts == null) {
            return Collections.emptyList();
        }
        return depts.stream()
                .map(DeptAssembler::toVO)
                .collect(Collectors.toList());
    }

    public static Dept toEntity(DeptDTO dto) {
        if (dto == null) {
            return null;
        }
        Dept dept = new Dept();
        dept.setName(dto.getName());
        dept.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        dept.setSort(dto.getSort() != null ? dto.getSort() : 0);
        dept.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        return dept;
    }

    public static void updateEntity(DeptDTO dto, Dept dept) {
        if (dto == null || dept == null) {
            return;
        }
        dept.setName(dto.getName());
        dept.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        dept.setSort(dto.getSort() != null ? dto.getSort() : 0);
        dept.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
    }
}
