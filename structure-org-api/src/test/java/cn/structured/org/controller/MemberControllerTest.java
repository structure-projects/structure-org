package cn.structured.org.controller;

import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.dto.OrganizationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 成员管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("成员管理接口测试")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("创建成员 - 成功")
    void testCreateMember_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_MEMBER");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        MemberDTO dto = new MemberDTO();
        dto.setUserId(1L);
        dto.setPhone("13800138000");
        dto.setName("张三");
        dto.setSex("M");
        dto.setDeptId(1L);
        dto.setState(1);
        dto.setOrganizationId(organizationId);

        mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建成员 - 失败（缺少必填字段）")
    void testCreateMember_Fail_MissingRequiredField() throws Exception {
        MemberDTO dto = new MemberDTO();
        // 不设置 userId 和 phone，应该校验失败

        mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("更新成员 - 成功")
    void testUpdateMember_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_MEMBER_UPDATE");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138001");
        createDto.setName("张三");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        String memberResponse = mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(memberResponse).get("data").asLong();

        // 更新成员
        MemberDTO updateDto = new MemberDTO();
        updateDto.setUserId(1L);
        updateDto.setPhone("13800138002");
        updateDto.setName("李四");
        updateDto.setSex("F");
        updateDto.setDeptId(2L);
        updateDto.setState(1);
        updateDto.setOrganizationId(organizationId);

        mockMvc.perform(put("/api/member/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("删除成员 - 成功")
    void testDeleteMember_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_MEMBER_DELETE");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138003");
        createDto.setName("待删除成员");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        String memberResponse = mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(memberResponse).get("data").asLong();

        // 删除成员
        mockMvc.perform(delete("/api/member/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取成员详情 - 成功")
    void testGetMemberById_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_MEMBER_GET");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138004");
        createDto.setName("测试成员详情");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        String memberResponse = mockMvc.perform(post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(memberResponse).get("data").asLong();

        // 获取成员详情
        mockMvc.perform(get("/api/member/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询成员 - 成功")
    void testPageMembers_Success() throws Exception {
        mockMvc.perform(get("/api/member/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }
}