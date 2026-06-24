package cn.structured.org.manager.impl;

import cn.structured.org.entity.Dept;
import cn.structured.org.enums.OrgExceptionEnum;
import cn.structured.org.exception.OrgException;
import cn.structured.org.manager.IDeptManager;
import cn.structured.org.mapper.DeptMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 部门Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptManagerImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptManager {

    @Override
    public Dept getById(Serializable id) {
        Dept dept = super.getById(id);
        if (dept == null) {
            log.warn("部门不存在, 部门ID: {}", id);
            throw new OrgException(OrgExceptionEnum.DEPT_NOT_FOUND);
        }
        return dept;
    }
}
