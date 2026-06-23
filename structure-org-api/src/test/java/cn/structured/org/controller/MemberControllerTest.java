package cn.structured.org.controller;

import cn.structured.org.config.AbstractIntegrationTest;
import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.dto.MemberInviteConfirmDTO;
import cn.structured.org.dto.MemberInviteDTO;
import cn.structured.org.dto.OrganizationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 成员管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("成员管理接口测试")
class MemberControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        // 通过邀请创建成员
        MemberInviteDTO inviteDto = new MemberInviteDTO();
        inviteDto.setOrganizationId(organizationId);
        inviteDto.setInvitePhones(Arrays.asList("13800138001"));

        MvcResult inviteResult = mockMvc.perform(post("/api/member-invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();

        String inviteResponse = inviteResult.getResponse().getContentAsString();
        List<?> dataList = objectMapper.readTree(inviteResponse).findValues("data");
        Long inviteId = objectMapper.readTree(dataList.get(0).toString()).get("id").asLong();
        String inviteCode = objectMapper.readTree(dataList.get(0).toString()).get("inviteCode").asText();

        // 确认邀请
        MemberInviteConfirmDTO confirmDto = new MemberInviteConfirmDTO();
        confirmDto.setInviteId(inviteId);
        confirmDto.setInviteCode(inviteCode);
        confirmDto.setUserId(1L);
        confirmDto.setPhone("13800138001");
        confirmDto.setName("张三");

        mockMvc.perform(post("/api/member-invite/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询成员ID（通过手机号查询）
        String pageResponse = mockMvc.perform(get("/api/member/page")
                        .param("phone", "13800138001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(pageResponse).get("data").get("list").get(0).get("id").asLong();

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

        // 通过邀请创建成员
        MemberInviteDTO inviteDto = new MemberInviteDTO();
        inviteDto.setOrganizationId(organizationId);
        inviteDto.setInvitePhones(Arrays.asList("13800138003"));

        MvcResult inviteResult = mockMvc.perform(post("/api/member-invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();

        String inviteResponse = inviteResult.getResponse().getContentAsString();
        List<?> dataList = objectMapper.readTree(inviteResponse).findValues("data");
        Long inviteId = objectMapper.readTree(dataList.get(0).toString()).get("id").asLong();
        String inviteCode = objectMapper.readTree(dataList.get(0).toString()).get("inviteCode").asText();

        // 确认邀请
        MemberInviteConfirmDTO confirmDto = new MemberInviteConfirmDTO();
        confirmDto.setInviteId(inviteId);
        confirmDto.setInviteCode(inviteCode);
        confirmDto.setUserId(1L);
        confirmDto.setPhone("13800138003");
        confirmDto.setName("待删除成员");

        mockMvc.perform(post("/api/member-invite/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询成员ID
        String pageResponse = mockMvc.perform(get("/api/member/page")
                        .param("phone", "13800138003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(pageResponse).get("data").get("list").get(0).get("id").asLong();

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

        // 通过邀请创建成员
        MemberInviteDTO inviteDto = new MemberInviteDTO();
        inviteDto.setOrganizationId(organizationId);
        inviteDto.setInvitePhones(Arrays.asList("13800138004"));

        MvcResult inviteResult = mockMvc.perform(post("/api/member-invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn();

        String inviteResponse = inviteResult.getResponse().getContentAsString();
        List<?> dataList = objectMapper.readTree(inviteResponse).findValues("data");
        Long inviteId = objectMapper.readTree(dataList.get(0).toString()).get("id").asLong();
        String inviteCode = objectMapper.readTree(dataList.get(0).toString()).get("inviteCode").asText();

        // 确认邀请
        MemberInviteConfirmDTO confirmDto = new MemberInviteConfirmDTO();
        confirmDto.setInviteId(inviteId);
        confirmDto.setInviteCode(inviteCode);
        confirmDto.setUserId(1L);
        confirmDto.setPhone("13800138004");
        confirmDto.setName("测试成员详情");

        mockMvc.perform(post("/api/member-invite/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询成员ID
        String pageResponse = mockMvc.perform(get("/api/member/page")
                        .param("phone", "13800138004"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andReturn().getResponse().getContentAsString();

        Long memberId = objectMapper.readTree(pageResponse).get("data").get("list").get(0).get("id").asLong();

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