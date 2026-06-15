package cn.structured.org.service.impl;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.assembler.OrganizationAssembler;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.entity.Organization;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IOrganizationManager;
import cn.structured.org.query.OrganizationQuery;
import cn.structured.org.service.IOrganizationService;
import cn.structured.org.vo.OrganizationVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 组织Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements IOrganizationService {

    private final IOrganizationManager organizationManager;

    @Override
    public Long create(OrganizationDTO dto) {
        if (StringUtils.hasText(dto.getCode())) {
            long count = organizationManager.count(Wrappers.<Organization>lambdaQuery()
                    .eq(Organization::getCode, dto.getCode()));
            if (count > 0) {
                log.warn("组织编码已存在: {}", dto.getCode());
                throw new OrgException(OrgExceptionEnum.ORGANIZATION_CODE_DUPLICATE);
            }
        }
        Organization organization = OrganizationAssembler.assembler(dto);
        organizationManager.save(organization);
        log.info("创建组织成功, 组织ID: {}", organization.getId());
        return organization.getId();
    }

    @Override
    public void update(Long id, OrganizationDTO dto) {
        Organization organization = organizationManager.getById(id);
        if (StringUtils.hasText(dto.getCode()) && !dto.getCode().equals(organization.getCode())) {
            long count = organizationManager.count(Wrappers.<Organization>lambdaQuery()
                    .eq(Organization::getCode, dto.getCode())
                    .ne(Organization::getId, id));
            if (count > 0) {
                log.warn("组织编码重复: {}", dto.getCode());
                throw new OrgException(OrgExceptionEnum.ORGANIZATION_CODE_DUPLICATE);
            }
        }
        Organization updateOrganization = OrganizationAssembler.assembler(dto);
        updateOrganization.setId(id);
        organizationManager.updateById(updateOrganization);
        log.info("更新组织成功, 组织ID: {}", id);
    }

    @Override
    public void delete(Long id) {
        Organization organization = organizationManager.getById(id);
        if (organization == null) {
            log.warn("组织不存在, 组织ID: {}", id);
            throw new OrgException(OrgExceptionEnum.ORGANIZATION_NOT_FOUND);
        }
        organizationManager.removeById(id);
        log.info("删除组织成功, 组织ID: {}", id);
    }


    @Override
    public OrganizationVO findById(Long id) {
        Organization organization = organizationManager.getById(id);
        if (organization == null) {
            log.warn("组织不存在, 组织ID: {}", id);
            throw new OrgException(OrgExceptionEnum.ORGANIZATION_NOT_FOUND);
        }
        return OrganizationAssembler.assembler(organization);
    }

    @Override
    public ResPage<OrganizationVO> page(OrganizationQuery query, ReqPage reqPage) {
        Page<Organization> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(Organization::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(Organization::getCode, query.getCode());
        }
        if (query.getState() != null) {
            wrapper.eq(Organization::getState, query.getState());
        }
        if (query.getType() != null) {
            wrapper.eq(Organization::getType, query.getType());
        }
        if (StringUtils.hasText(query.getIndustry())) {
            wrapper.like(Organization::getIndustry, query.getIndustry());
        }
        wrapper.orderByDesc(Organization::getCreateTime);
        Page<Organization> result = organizationManager.page(page, wrapper);

        ResPage<OrganizationVO> resPage = new ResPage<>();
        resPage.setRecords(result.convert(OrganizationAssembler::assembler).getRecords());
        resPage.setTotal(result.getTotal());
        resPage.setCurrent(result.getCurrent());
        resPage.setSize(result.getSize());
        resPage.setPages(result.getPages());
        return resPage;
    }
}
