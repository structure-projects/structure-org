package cn.structured.org.assembler;

import cn.structured.org.entity.InviteLink;
import cn.structured.org.vo.InviteLinkVO;

/**
 * 邀请链接转换器
 *
 * @author chuck
 * @since 2024-01-01
 */
public class InviteLinkAssembler {

    private InviteLinkAssembler() {
    }

    public static InviteLinkVO assembler(InviteLink inviteLink) {
        if (inviteLink == null) {
            return null;
        }
        InviteLinkVO vo = new InviteLinkVO();
        vo.setId(inviteLink.getId());
        vo.setOrganizationId(inviteLink.getOrganizationId());
        vo.setOrganizationName(inviteLink.getOrganizationName());
        vo.setDeptId(inviteLink.getDeptId());
        vo.setDeptName(inviteLink.getDeptName());
        vo.setInviteUserId(inviteLink.getInviteUserId());
        vo.setInviteUserName(inviteLink.getInviteUserName());
        vo.setInviteCode(inviteLink.getInviteCode());
        vo.setExpireTime(inviteLink.getExpireTime());
        vo.setMaxUseCount(inviteLink.getMaxUseCount());
        vo.setUseCount(inviteLink.getUseCount());
        vo.setState(inviteLink.getState());
        vo.setCreateTime(inviteLink.getCreateTime());
        return vo;
    }
}