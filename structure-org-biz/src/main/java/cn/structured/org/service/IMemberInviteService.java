package cn.structured.org.service;

import cn.structured.org.dto.MemberInviteConfirmDTO;
import cn.structured.org.dto.MemberInviteDTO;
import cn.structured.org.vo.MemberInviteVO;

import java.util.List;

/**
 * 成员邀请Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IMemberInviteService {

    /**
     * 发起成员邀请（批量）
     *
     * @param dto 邀请参数
     * @return 邀请记录列表
     */
    List<MemberInviteVO> invite(MemberInviteDTO dto);

    /**
     * 确认加入（被邀请人接受邀请）
     *
     * @param dto 确认参数
     */
    void confirm(MemberInviteConfirmDTO dto);

    /**
     * 根据ID查询邀请记录
     *
     * @param id 邀请记录ID
     * @return 邀请记录
     */
    MemberInviteVO findById(Long id);

    /**
     * 根据邀请码查询邀请记录
     *
     * @param inviteCode 邀请码
     * @return 邀请记录
     */
    MemberInviteVO findByInviteCode(String inviteCode);

    /**
     * 取消邀请
     *
     * @param id 邀请记录ID
     */
    void cancel(Long id);
}