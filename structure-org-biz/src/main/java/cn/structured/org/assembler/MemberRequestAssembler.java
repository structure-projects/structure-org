package cn.structured.org.assembler;

import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.entity.MemberRequest;
import cn.structured.org.vo.MemberRequestVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberRequestAssembler {

    private MemberRequestAssembler() {
    }

    public static MemberRequestVO toVO(MemberRequest request) {
        return toVO(request, null, null);
    }

    public static MemberRequestVO toVO(MemberRequest request, Map<Long, String> orgNameMap, Map<Long, String> deptNameMap) {
        if (request == null) {
            return null;
        }
        MemberRequestVO vo = new MemberRequestVO();
        vo.setId(request.getId());
        vo.setOrganizationId(request.getOrganizationId());
        if (orgNameMap != null && request.getOrganizationId() != null) {
            vo.setOrganizationName(orgNameMap.get(request.getOrganizationId()));
        }
        vo.setUserId(request.getUserId());
        vo.setPhone(request.getPhone());
        vo.setName(request.getName());
        vo.setDeptId(request.getDeptId());
        if (deptNameMap != null && request.getDeptId() != null) {
            vo.setDeptName(deptNameMap.get(request.getDeptId()));
        }
        vo.setType(request.getType());
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

    public static List<MemberRequestVO> toVOList(List<MemberRequest> requests) {
        return toVOList(requests, null, null);
    }

    public static List<MemberRequestVO> toVOList(List<MemberRequest> requests, Map<Long, String> orgNameMap, Map<Long, String> deptNameMap) {
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream()
                .map(request -> toVO(request, orgNameMap, deptNameMap))
                .collect(Collectors.toList());
    }

    public static MemberRequest toEntity(MemberRequestDTO dto) {
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
        request.setReason(dto.getReason());
        request.setState(1);
        return request;
    }
}
