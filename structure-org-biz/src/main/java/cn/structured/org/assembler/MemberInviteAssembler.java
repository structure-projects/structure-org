package cn.structured.org.assembler;

import cn.structured.org.entity.MemberInvite;
import cn.structured.org.enums.MemberInviteStateEnum;
import cn.structured.org.vo.MemberInviteVO;

/**
 * 成员邀请记录转换器
 *
 * @author chuck
 * @since 2024-01-01
 */
public class MemberInviteAssembler {

    private MemberInviteAssembler() {
    }

    public static MemberInviteVO assembler(MemberInvite invite) {
        if (invite == null) {
            return null;
        }
        MemberInviteVO vo = new MemberInviteVO();
        vo.setId(invite.getId());
        vo.setOrganizationId(invite.getOrganizationId());
        vo.setDeptId(invite.getDeptId());
        vo.setInviteUserId(invite.getInviteUserId());
        vo.setInviteUserName(invite.getInviteUserName());
        vo.setInvitePhone(invite.getInvitePhone());
        vo.setInviteCode(invite.getInviteCode());
        vo.setExpireTime(invite.getExpireTime());
        vo.setState(invite.getState());
        vo.setCreateTime(invite.getCreateTime());

        // 设置状态描述
        if (invite.getState() != null) {
            MemberInviteStateEnum stateEnum = MemberInviteStateEnum.getByCode(invite.getState());
            vo.setStateDesc(stateEnum != null ? stateEnum.getDescription() : null);
        }
        return vo;
    }
}