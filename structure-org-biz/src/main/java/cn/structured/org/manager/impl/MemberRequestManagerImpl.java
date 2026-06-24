package cn.structured.org.manager.impl;

import cn.structured.org.entity.MemberRequest;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IMemberRequestManager;
import cn.structured.org.mapper.MemberRequestMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 成员申请Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRequestManagerImpl extends ServiceImpl<MemberRequestMapper, MemberRequest> implements IMemberRequestManager {

    @Override
    public MemberRequest getById(Serializable id) {
        MemberRequest request = super.getById(id);
        if (request == null) {
            log.warn("成员申请不存在, 申请ID: {}", id);
            throw new OrgException(OrgExceptionEnum.MEMBER_REQUEST_NOT_FOUND);
        }
        return request;
    }
}