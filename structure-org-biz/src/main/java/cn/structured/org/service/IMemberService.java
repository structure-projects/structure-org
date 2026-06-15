package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.query.MemberQuery;
import cn.structured.org.vo.MemberVO;

/**
 * 成员Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IMemberService {

    /**
     * 创建成员
     *
     * @param dto 创建参数
     * @return 成员ID
     */
    Long create(MemberDTO dto);

    /**
     * 更新成员
     *
     * @param id  成员ID
     * @param dto 更新参数
     */
    void update(Long id, MemberDTO dto);

    /**
     * 删除成员
     *
     * @param id 成员ID
     */
    void delete(Long id);

    /**
     * 根据ID查询成员
     *
     * @param id 成员ID
     * @return 成员信息
     */
    MemberVO findById(Long id);

    /**
     * 分页查询成员
     *
     * @param query   查询参数
     * @param reqPage 分页参数
     * @return 分页结果
     */
    ResPage<MemberVO> page(MemberQuery query, ReqPage reqPage);
}
