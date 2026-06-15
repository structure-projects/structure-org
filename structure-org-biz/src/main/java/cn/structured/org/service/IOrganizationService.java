package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.query.OrganizationQuery;
import cn.structured.org.vo.OrganizationVO;

/**
 * 组织Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IOrganizationService {

    /**
     * 创建组织
     *
     * @param dto 组织DTO
     * @return 组织ID
     */
    Long create(OrganizationDTO dto);

    /**
     * 更新组织
     *
     * @param id  组织ID
     * @param dto 组织DTO
     */
    void update(Long id, OrganizationDTO dto);

    /**
     * 删除组织
     *
     * @param id 组织ID
     */
    void delete(Long id);

    /**
     * 查询组织
     *
     * @param id 组织ID
     * @return 组织VO
     */
    OrganizationVO findById(Long id);

    /**
     * 查询组织列表
     *
     * @param query   查询条件
     * @param reqPage 分页参数
     * @return 组织列表
     */
    ResPage<OrganizationVO> page(OrganizationQuery query, ReqPage reqPage);
}