package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.entity.Organization;
import cn.structured.org.query.OrganizationQuery;
import cn.structured.org.vo.OrganizationVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 组织Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IOrganizationService extends IService<Organization> {

    Long create(OrganizationDTO dto);

    void update(Long id, OrganizationDTO dto);

    void delete(Long id);

    OrganizationVO findById(Long id);

    ResPage<OrganizationVO> page(OrganizationQuery query, ReqPage reqPage);
}