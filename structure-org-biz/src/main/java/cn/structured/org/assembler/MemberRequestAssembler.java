package cn.structured.org.assembler;

import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.entity.MemberRequest;
import cn.structured.org.vo.MemberRequestVO;

public class MemberRequestAssembler {

    private MemberRequestAssembler() {
    }


    public static MemberRequestVO assemble(MemberRequest request) {
        if (request == null) {
            return null;
        }
        MemberRequestVO vo = new MemberRequestVO();
        vo.setId(request.getId());
        vo.setOrganizationId(request.getOrganizationId());
        vo.setUserId(request.getUserId());
        vo.setPhone(request.getPhone());
        vo.setName(request.getName());
        vo.setDeptId(request.getDeptId());
        vo.setType(request.getType());
        vo.setInviteLinkId(request.getInviteLinkId());
        vo.setReason(request.getReason());
        vo.setState(request.getState());
        vo.setRejectReason(request.getRejectReason());
        vo.setAuditTime(request.getAuditTime());
        vo.setAuditBy(request.getAuditBy());
        vo.setCreateTime(request.getCreateTime());
        vo.setCreateBy(request.getCreateBy());
        vo.setUpdateTime(request.getUpdateTime());
        vo.setUpdateBy(request.getUpdateBy());
        return vo;
    }


    public static MemberRequest assemble(MemberRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        MemberRequest request = new MemberRequest();
        request.setOrganizationId(dto.getOrganizationId());
        request.setUserId(dto.getUserId());
        request.setPhone(dto.getPhone());
        request.setName(dto.getName());
        request.setDeptId(dto.getDeptId());
        request.setType(dto.getType() != null ? dto.getType() : 1);
        request.setInviteLinkId(dto.getInviteLinkId());
        request.setReason(dto.getReason());
        request.setState(1);
        return request;
    }
}
