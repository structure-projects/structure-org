package cn.structured.org.manager.impl;

import cn.structured.org.entity.MemberInvite;
import cn.structured.org.mapper.MemberInviteMapper;
import cn.structured.org.manager.IMemberInviteManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 成员邀请Manager实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Component
@RequiredArgsConstructor
public class MemberInviteManagerImpl extends ServiceImpl<MemberInviteMapper, MemberInvite> implements IMemberInviteManager {
}