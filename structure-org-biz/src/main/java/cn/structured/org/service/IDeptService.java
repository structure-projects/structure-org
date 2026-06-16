package cn.structured.org.service;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.DeptDTO;
import cn.structured.org.query.DeptQuery;
import cn.structured.org.vo.DeptVO;

import java.util.List;

/**
 * 部门Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IDeptService  {

    Long create(DeptDTO dto);

    void update(Long id, DeptDTO dto);

    void delete(Long id);

    DeptVO findById(Long id);

    ResPage<DeptVO> page(DeptQuery query, ReqPage reqPage);

    List<DeptVO> tree(Long organizationId);

    List<OptionVO> options(Long organizationId);
}
