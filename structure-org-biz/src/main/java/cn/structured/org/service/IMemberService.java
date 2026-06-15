package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.entity.Member;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.vo.MemberVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 成员Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IMemberService extends IService<Member> {

    Long create(MemberDTO dto);

    void update(Long id, MemberDTO dto);

    void delete(Long id);

    MemberVO findById(Long id);

    ResPage<MemberVO> page(MemberQuery query, ReqPage reqPage);
}
