package cn.structured.org.service.impl;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.mybatis.plus.starter.convert.ResPageConvert;
import cn.structured.org.assembler.MemberAssembler;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.entity.Member;
import cn.structured.org.manager.IMemberManager;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.service.IMemberService;
import cn.structured.org.vo.MemberVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 成员Service实现类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements IMemberService {

    private final IMemberManager memberManager;

    @Override
    public Long create(MemberDTO dto) {
        Member member = MemberAssembler.assemble(dto);
        memberManager.save(member);
        log.info("创建成员成功, 成员ID: {}", member.getId());
        return member.getId();
    }

    @Override
    public void update(Long id, MemberDTO dto) {
        Member updateMember = MemberAssembler.assemble(dto);
        updateMember.setId(id);
        memberManager.updateById(updateMember);
        log.info("更新成员成功, 成员ID: {}", id);
    }

    @Override
    public void delete(Long id) {
        memberManager.removeById(id);
        log.info("删除成员成功, 成员ID: {}", id);
    }


    @Override
    public MemberVO findById(Long id) {
        Member member = memberManager.getById(id);
        return MemberAssembler.assemble(member);
    }

    @Override
    public ResPage<MemberVO> page(MemberQuery query, ReqPage reqPage) {
        log.info("查询成员列表, 查询参数: {}", query);
        Page<Member> page = new Page<>(reqPage.getCurrentPage(), reqPage.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Member> queryWrapper = Wrappers.<Member>lambdaQuery()
                .eq(null != query.getUserId(), Member::getUserId, query.getUserId())
                .eq(null != query.getDeptId(), Member::getDeptId, query.getDeptId())
                .eq(null != query.getState(), Member::getState, query.getState())
                .like(null != query.getPhone(), Member::getPhone, query.getPhone())
                .like(null != query.getName(), Member::getName, query.getName())
                .orderByDesc(Member::getCreateTime);
        Page<Member> result = memberManager.page(page, queryWrapper);
        return ResPageConvert.convert(result, MemberAssembler::assemble);
    }

}
