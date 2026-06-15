package cn.structured.org.assembler;

import cn.structured.org.dto.DeptDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.vo.DeptVO;

public class DeptAssembler {

    private DeptAssembler() {
    }

    public static DeptVO assembler(Dept dept) {
        DeptVO deptVo = new DeptVO();
        deptVo.setId(dept.getId());
        deptVo.setName(dept.getName());
        deptVo.setParentId(dept.getParentId());
        deptVo.setTreePath(dept.getTreePath());
        deptVo.setSort(dept.getSort());
        deptVo.setEnabled(dept.getEnabled());
        deptVo.setCreateTime(dept.getCreateTime());
        deptVo.setUpdateTime(dept.getUpdateTime());
        return deptVo;
    }

    public static Dept assembler(DeptDTO deptDto) {
        Dept dept = new Dept();
        dept.setName(deptDto.getName());
        dept.setParentId(deptDto.getParentId());
        dept.setTreePath(deptDto.getTreePath());
        dept.setSort(deptDto.getSort());
        dept.setEnabled(deptDto.getEnabled());
        return dept;
    }
}
