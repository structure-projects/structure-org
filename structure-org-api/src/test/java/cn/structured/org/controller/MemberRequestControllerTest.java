package cn.structured.org.controller;

import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.MemberAuditDTO;
import cn.structured.org.dto.MemberRequestDTO;
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
 * 成员申请控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("成员申请管理接口测试")
class MemberRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("创建申请 - 成功")
    void testCreateMemberRequest_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_REQUEST");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setOrganizationId(organizationId);
        dto.setUserId(1L);
        dto.setPhone("13800138000");
        dto.setName("张三");
        dto.setDeptId(1L);
        dto.setType(1);
        dto.setReason("希望加入组织");

        mockMvc.perform(post("/api/member-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建申请 - 失败（缺少必填字段）")
    void testCreateMemberRequest_Fail_MissingRequiredField() throws Exception {
        MemberRequestDTO dto = new MemberRequestDTO();
        // 不设置 organizationId、userId 和 phone，应该校验失败

        mockMvc.perform(post("/api/member-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("审核申请 - 通过")
    void testAuditMemberRequest_Approve() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_REQUEST_APPROVE");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个申请
        MemberRequestDTO createDto = new MemberRequestDTO();
        createDto.setOrganizationId(organizationId);
        createDto.setUserId(1L);
        createDto.setPhone("13800138001");
        createDto.setName("张三");
        createDto.setDeptId(1L);
        createDto.setType(1);
        createDto.setReason("希望加入组织");

        String requestResponse = mockMvc.perform(post("/api/member-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long requestId = objectMapper.readTree(requestResponse).get("data").asLong();

        // 审核通过
        MemberAuditDTO auditDTO = new MemberAuditDTO();
        auditDTO.setId(requestId);
        auditDTO.setState(2); // 通过

        mockMvc.perform(put("/api/member-request/{id}/audit", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("审核申请 - 拒绝")
    void testAuditMemberRequest_Reject() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_REQUEST_REJECT");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个申请
        MemberRequestDTO createDto = new MemberRequestDTO();
        createDto.setOrganizationId(organizationId);
        createDto.setUserId(1L);
        createDto.setPhone("13800138002");
        createDto.setName("李四");
        createDto.setDeptId(1L);
        createDto.setType(1);
        createDto.setReason("希望加入组织");

        String requestResponse = mockMvc.perform(post("/api/member-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long requestId = objectMapper.readTree(requestResponse).get("data").asLong();

        // 审核拒绝
        MemberAuditDTO auditDTO = new MemberAuditDTO();
        auditDTO.setId(requestId);
        auditDTO.setState(3); // 拒绝
        auditDTO.setRejectReason("不符合加入条件");

        mockMvc.perform(put("/api/member-request/{id}/audit", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取申请详情 - 成功")
    void testGetMemberRequestById_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_REQUEST_GET");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        // 创建一个申请
        MemberRequestDTO createDto = new MemberRequestDTO();
        createDto.setOrganizationId(organizationId);
        createDto.setUserId(1L);
        createDto.setPhone("13800138003");
        createDto.setName("王五");
        createDto.setDeptId(1L);
        createDto.setType(1);
        createDto.setReason("希望加入组织");

        String requestResponse = mockMvc.perform(post("/api/member-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long requestId = objectMapper.readTree(requestResponse).get("data").asLong();

        // 获取申请详情
        mockMvc.perform(get("/api/member-request/{id}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询申请 - 成功")
    void testPageMemberRequests_Success() throws Exception {
        mockMvc.perform(get("/api/member-request/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }
}