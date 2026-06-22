package cn.structured.org.controller;

import cn.structured.org.config.TestConfig;
import cn.structured.org.dto.InviteLinkDTO;
import cn.structured.org.dto.OrganizationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 邀请链接控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("邀请链接管理接口测试")
class InviteLinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("创建邀请链接 - 成功")
    void testCreateInviteLink_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织-邀请链接");
        orgDto.setCode("TEST_ORG_INVITE");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建邀请链接
        InviteLinkDTO dto = new InviteLinkDTO();
        dto.setOrganizationId(organizationId);
        dto.setExpireDays(7);
        dto.setMaxUseCount(10);

        mockMvc.perform(post("/api/invite-link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.inviteCode").isString())
                .andExpect(jsonPath("$.data.inviteLink").isString());
    }

    @Test
    @DisplayName("创建邀请链接 - 失败（缺少必填字段）")
    void testCreateInviteLink_Fail_MissingRequiredField() throws Exception {
        InviteLinkDTO dto = new InviteLinkDTO();
        // 不设置 organizationId，应该校验失败

        mockMvc.perform(post("/api/invite-link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("创建邀请链接 - 失败（组织不存在）")
    void testCreateInviteLink_Fail_OrganizationNotFound() throws Exception {
        InviteLinkDTO dto = new InviteLinkDTO();
        dto.setOrganizationId(999999L);

        mockMvc.perform(post("/api/invite-link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORG_001"));
    }

    @Test
    @DisplayName("根据邀请码查询邀请链接 - 成功")
    void testGetInviteLinkByCode_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织-邀请链接2");
        orgDto.setCode("TEST_ORG_INVITE2");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建邀请链接
        InviteLinkDTO dto = new InviteLinkDTO();
        dto.setOrganizationId(organizationId);
        dto.setExpireDays(7);

        String createResponse = mockMvc.perform(post("/api/invite-link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String inviteCode = objectMapper.readTree(createResponse).get("data").get("inviteCode").asText();

        // 根据邀请码查询
        mockMvc.perform(get("/api/invite-link/code/{inviteCode}", inviteCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.inviteCode").value(inviteCode));
    }

    @Test
    @DisplayName("根据邀请码查询邀请链接 - 失败（邀请码不存在）")
    void testGetInviteLinkByCode_Fail_NotFound() throws Exception {
        mockMvc.perform(get("/api/invite-link/code/{inviteCode}", "non_existent_code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORG_401"));
    }

    @Test
    @DisplayName("分页查询邀请链接 - 成功")
    void testPageInviteLinks_Success() throws Exception {
        mockMvc.perform(get("/api/invite-link/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询当前用户邀请链接 - 成功")
    void testPageMyInviteLinks_Success() throws Exception {
        mockMvc.perform(get("/api/invite-link/my")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }
}