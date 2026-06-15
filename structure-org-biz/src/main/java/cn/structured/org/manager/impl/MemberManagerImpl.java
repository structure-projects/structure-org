package cn.structured.org.manager.impl;

import cn.structured.org.entity.Member;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IMemberManager;
import cn.structured.org.mapper.MemberMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 成员Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagerImpl extends ServiceImpl<MemberMapper, Member> implements IMemberManager {

    @Override
    public Member getById(Serializable id) {
        Member member = super.getById(id);
        if (member == null) {
            log.warn("成员不存在, 成员ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_NOT_FOUND);
        }
        return member;
    }
}
