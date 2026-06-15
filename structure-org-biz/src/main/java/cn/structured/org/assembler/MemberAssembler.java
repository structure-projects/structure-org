package cn.structured.org.assembler;

import cn.structured.org.dto.MemberDTO;
import cn.structured.org.entity.Member;
import cn.structured.org.vo.MemberVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberAssembler {

    private MemberAssembler() {
    }

    public static MemberVO toVO(Member member) {
        return toVO(member, null);
    }

    public static MemberVO toVO(Member member, Map<Long, String> deptNameMap) {
        if (member == null) {
            return null;
        }
        MemberVO vo = new MemberVO();
        vo.setId(member.getId());
        vo.setUserId(member.getUserId());
        vo.setPhone(member.getPhone());
        vo.setName(member.getName());
        vo.setSex(member.getSex());
        vo.setDeptId(member.getDeptId());
        if (deptNameMap != null && member.getDeptId() != null) {
            vo.setDeptName(deptNameMap.get(member.getDeptId()));
        }
        vo.setState(member.getState());
        vo.setOrganizationId(member.getOrganizationId());
        vo.setCreateTime(member.getCreateTime());
        vo.setCreateBy(member.getCreateBy());
        vo.setUpdateTime(member.getUpdateTime());
        vo.setUpdateBy(member.getUpdateBy());
        return vo;
    }

    public static List<MemberVO> toVOList(List<Member> members) {
        return toVOList(members, null);
    }

    public static List<MemberVO> toVOList(List<Member> members, Map<Long, String> deptNameMap) {
        if (members == null) {
            return Collections.emptyList();
        }
        return members.stream()
                .map(member -> toVO(member, deptNameMap))
                .collect(Collectors.toList());
    }

    public static Member toEntity(MemberDTO dto) {
        if (dto == null) {
            return null;
        }
        Member member = new Member();
        member.setUserId(dto.getUserId());
        member.setPhone(dto.getPhone());
        member.setName(dto.getName());
        member.setSex(dto.getSex());
        member.setDeptId(dto.getDeptId());
        member.setState(dto.getState() != null ? dto.getState() : 1);
        member.setOrganizationId(dto.getOrganizationId());
        return member;
    }

    public static void updateEntity(MemberDTO dto, Member member) {
        if (dto == null || member == null) {
            return;
        }
        member.setUserId(dto.getUserId());
        member.setPhone(dto.getPhone());
        member.setName(dto.getName());
        member.setSex(dto.getSex());
        member.setDeptId(dto.getDeptId());
        member.setState(dto.getState());
    }
}
