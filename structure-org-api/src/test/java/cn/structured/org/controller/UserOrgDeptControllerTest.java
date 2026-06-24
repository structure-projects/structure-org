package cn.structured.org.controller;

import cn.structured.org.config.AbstractIntegrationTest;
import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.DeptDTO;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.service.IDeptService;
import cn.structured.org.service.IMemberService;
import cn.structured.org.service.IOrganizationService;
import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * 用户组织和部门接口测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("用户组织和部门接口测试")
class UserOrgDeptControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IMemberService memberService;

    @BeforeEach
    void setUp() {
        UserContext.remove();
    }

    @AfterEach
    void tearDown() {
        UserContext.remove();
    }

    private void setUserContext(Long userId, Long tenantId) {
        UserContextEntity userContext = UserContextEntity.builder()
                .userId(String.valueOf(userId))
                .tenantId(String.valueOf(tenantId))
                .loginTime(LocalDateTime.now())
                .build();
        UserContext.set(userContext);
    }

    @Test
    @DisplayName("获取当前用户所属组织列表 - 成功")
    void testGetUserOrganizations_Success() throws Exception {
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织1");
        orgDto.setCode("TEST_ORG_USER_001");
        orgDto.setState(1);
        Long orgId = organizationService.create(orgDto);

        MemberDTO memberDto = new MemberDTO();
        memberDto.setUserId(100L);
        memberDto.setPhone("13800138000");
        memberDto.setName("测试用户");
        memberDto.setSex("M");
        memberDto.setState(1);
        memberDto.setOrganizationId(orgId);
        memberService.create(memberDto);

        setUserContext(100L, orgId);

        mockMvc.perform(get("/api/member/organizations")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("获取当前用户所属组织列表 - 分页验证")
    void testGetUserOrganizations_Pagination() throws Exception {
        for (int i = 0; i < 3; i++) {
            OrganizationDTO orgDto = new OrganizationDTO();
            orgDto.setName("测试组织" + i);
            orgDto.setCode("TEST_ORG_PAGE_" + i);
            orgDto.setState(1);
            Long orgId = organizationService.create(orgDto);

            MemberDTO memberDto = new MemberDTO();
            memberDto.setUserId(200L);
            memberDto.setPhone("1390013900" + i);
            memberDto.setName("测试用户" + i);
            memberDto.setSex("M");
            memberDto.setState(1);
            memberDto.setOrganizationId(orgId);
            memberService.create(memberDto);
        }

        setUserContext(200L, 1L);

        mockMvc.perform(get("/api/member/organizations")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("获取当前用户所属部门列表 - 扁平化模式")
    void testGetUserDepts_FlatMode() throws Exception {
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织部门");
        orgDto.setCode("TEST_ORG_DEPT");
        orgDto.setState(1);
        Long orgId = organizationService.create(orgDto);

        DeptDTO deptDto = new DeptDTO();
        deptDto.setName("测试部门");
        deptDto.setParentId(0L);
        deptDto.setTreePath(",");
        deptDto.setOrganizationId(orgId);
        deptDto.setEnabled(true);
        Long deptId = deptService.create(deptDto);

        MemberDTO memberDto = new MemberDTO();
        memberDto.setUserId(300L);
        memberDto.setPhone("13700137000");
        memberDto.setName("部门用户");
        memberDto.setSex("M");
        memberDto.setState(1);
        memberDto.setOrganizationId(orgId);
        memberDto.setDeptId(deptId);
        memberService.create(memberDto);

        setUserContext(300L, orgId);

        mockMvc.perform(get("/api/member/depts")
                        .param("treeMode", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(greaterThan(0)));
    }

    @Test
    @DisplayName("获取当前用户所属部门列表 - 树形模式")
    void testGetUserDepts_TreeMode() throws Exception {
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("树形组织");
        orgDto.setCode("TEST_ORG_TREE");
        orgDto.setState(1);
        Long orgId = organizationService.create(orgDto);

        DeptDTO parentDept = new DeptDTO();
        parentDept.setName("父部门");
        parentDept.setParentId(0L);
        parentDept.setTreePath(",");
        parentDept.setOrganizationId(orgId);
        parentDept.setEnabled(true);
        Long parentId = deptService.create(parentDept);

        DeptDTO childDept = new DeptDTO();
        childDept.setName("子部门");
        childDept.setParentId(parentId);
        childDept.setTreePath("," + parentId + ",");
        childDept.setOrganizationId(orgId);
        childDept.setEnabled(true);
        Long childId = deptService.create(childDept);

        MemberDTO memberDto = new MemberDTO();
        memberDto.setUserId(400L);
        memberDto.setPhone("13600136000");
        memberDto.setName("树形用户");
        memberDto.setSex("M");
        memberDto.setState(1);
        memberDto.setOrganizationId(orgId);
        memberDto.setDeptId(childId);
        memberService.create(memberDto);

        setUserContext(400L, orgId);

        mockMvc.perform(get("/api/member/depts")
                        .param("treeMode", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("获取当前用户所属部门列表 - 未登录返回空")
    void testGetUserDepts_NotLogin() throws Exception {
        mockMvc.perform(get("/api/member/depts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}