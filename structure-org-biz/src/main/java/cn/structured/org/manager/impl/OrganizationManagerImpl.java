package cn.structured.org.manager.impl;

import cn.structured.org.entity.Organization;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IOrganizationManager;
import cn.structured.org.mapper.OrganizationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 组织Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationManagerImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationManager {
    @Override
    public Organization getById(Serializable id) {
        Organization organization = super.getById(id);
        if (organization == null) {
            log.warn("组织不存在, 组织ID: {}", id);
            throw new OrgException(OrgExceptionEnum.ORGANIZATION_NOT_FOUND);
        }
        return organization;
    }
}
