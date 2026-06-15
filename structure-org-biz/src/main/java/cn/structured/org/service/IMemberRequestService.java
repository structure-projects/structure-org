package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberAuditDTO;
import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.query.MemberRequestQuery;
import cn.structured.org.vo.MemberRequestVO;

/**
 * 成员申请Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IMemberRequestService {

    /**
     * 创建成员申请
     *
     * @param dto 创建参数
     * @return 成员申请ID
     */
    Long create(MemberRequestDTO dto);

    /**
     * 审核成员申请
     *
     * @param auditDT 审核参数
     */
    void audit(MemberAuditDTO auditDT);

    /**
     * 根据ID查询成员申请
     *
     * @param id 成员申请ID
     * @return 成员申请
     */
    MemberRequestVO findById(Long id);

    /**
     * 分页查询成员申请
     *
     * @param query   查询参数
     * @param reqPage 分页参数
     * @return 分页结果
     */
    ResPage<MemberRequestVO> page(MemberRequestQuery query, ReqPage reqPage);
}
