package cn.structured.org.manager.impl;

import cn.structured.org.entity.InviteLink;
import cn.structured.org.mapper.InviteLinkMapper;
import cn.structured.org.manager.IInviteLinkManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 邀请链接Manager实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Component
@RequiredArgsConstructor
public class InviteLinkManagerImpl extends ServiceImpl<InviteLinkMapper, InviteLink> implements IInviteLinkManager {
}