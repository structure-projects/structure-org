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


    public static MemberVO assemble(Member member) {
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
        vo.setState(member.getState());
        vo.setOrganizationId(member.getOrganizationId());
        vo.setCreateTime(member.getCreateTime());
        return vo;
    }
    public static Member assemble(MemberDTO dto) {
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

}
