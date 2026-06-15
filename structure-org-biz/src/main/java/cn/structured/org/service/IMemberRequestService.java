package cn.structured.org.service;

import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.org.dto.MemberRequestDTO;
import cn.structured.org.entity.MemberRequest;
import cn.structured.org.query.MemberRequestQuery;
import cn.structured.org.vo.MemberRequestVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 成员申请Service接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface IMemberRequestService extends IService<MemberRequest> {

    Long create(MemberRequestDTO dto);

    void audit(Long id, Integer state, String rejectReason);

    MemberRequestVO findById(Long id);

    ResPage<MemberRequestVO> page(MemberRequestQuery query, ReqPage reqPage);
}
