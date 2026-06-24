package cn.structured.org.mapper;

import cn.structured.org.entity.Member;
import cn.structured.org.vo.UserOrganizationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 成员Mapper接口
 *
 * @author chuck
 * @since 2024-01-01
 */
public interface MemberMapper extends BaseMapper<Member> {

    @Select("SELECT o.id AS organizationId, o.name AS organizationName, o.type AS organizationType, " +
            "m.state AS memberState, m.create_time AS joinTime, " +
            "CASE WHEN m.id = o.create_by THEN 'OWNER' ELSE 'MEMBER' END AS role " +
            "FROM member m " +
            "INNER JOIN organization o ON m.organization_id = o.id " +
            "WHERE m.user_id = #{userId} AND m.state = 1 AND o.state = 1 AND m.is_deleted = 0 AND o.is_deleted = 0 " +
            "ORDER BY m.create_time DESC")
    List<UserOrganizationVO> selectUserOrganizations(@Param("userId") Long userId);
}
