package cn.structured.org.assembler;

import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.entity.Organization;
import cn.structured.org.vo.OrganizationVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizationAssembler {

    private OrganizationAssembler() {
    }

    public static OrganizationVO assembler(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationVO vo = new OrganizationVO();
        vo.setId(organization.getId());
        vo.setName(organization.getName());
        vo.setCode(organization.getCode());
        vo.setLogo(organization.getLogo());
        vo.setDescription(organization.getDescription());
        vo.setIndustry(organization.getIndustry());
        vo.setAddress(organization.getAddress());
        vo.setContactPhone(organization.getContactPhone());
        vo.setState(organization.getState());
        vo.setType(organization.getType());
        vo.setCreateTime(organization.getCreateTime());
        vo.setCreateBy(organization.getCreateBy());
        vo.setUpdateTime(organization.getUpdateTime());
        vo.setUpdateBy(organization.getUpdateBy());
        return vo;
    }


    public static Organization assembler(OrganizationDTO dto) {
        if (dto == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setName(dto.getName());
        organization.setCode(dto.getCode());
        organization.setLogo(dto.getLogo());
        organization.setDescription(dto.getDescription());
        organization.setIndustry(dto.getIndustry());
        organization.setAddress(dto.getAddress());
        organization.setContactPhone(dto.getContactPhone());
        organization.setState(dto.getState());
        organization.setType(dto.getType());
        return organization;
    }

}
