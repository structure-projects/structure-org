package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.InviteLinkDTO;
import cn.structured.org.query.InviteLinkQuery;
import cn.structured.org.vo.InviteLinkVO;

/**
 * 邀请链接Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IInviteLinkService {

    /**
     * 创建邀请链接
     *
     * @param dto 邀请链接参数
     * @return 邀请链接信息
     */
    InviteLinkVO createInviteLink(InviteLinkDTO dto);

    /**
     * 根据ID查询邀请链接
     *
     * @param id 邀请链接ID
     * @return 邀请链接信息
     */
    InviteLinkVO findById(Long id);

    /**
     * 根据邀请码查询邀请链接
     *
     * @param inviteCode 邀请码
     * @return 邀请链接信息
     */
    InviteLinkVO findByInviteCode(String inviteCode);

    /**
     * 分页查询当前用户的邀请链接
     *
     * @param query   查询参数
     * @param reqPage 分页参数
     * @return 分页结果
     */
    ResPage<InviteLinkVO> pageByCurrentUser(InviteLinkQuery query, ReqPage reqPage);

    /**
     * 分页查询邀请链接（管理端）
     *
     * @param query   查询参数
     * @param reqPage 分页参数
     * @return 分页结果
     */
    ResPage<InviteLinkVO> page(InviteLinkQuery query, ReqPage reqPage);

    /**
     * 禁用邀请链接
     *
     * @param id 邀请链接ID
     */
    void disable(Long id);

    /**
     * 删除邀请链接
     *
     * @param id 邀请链接ID
     */
    void delete(Long id);
}