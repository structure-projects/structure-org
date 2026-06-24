package cn.structured.org.controller;

import cn.structured.org.config.AbstractIntegrationTest;
import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.OrganizationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 组织管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("组织管理接口测试")
class OrganizationControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("创建组织 - 成功")
    void testCreateOrganization_Success() throws Exception {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setName("测试组织");
        dto.setCode("TEST_ORG_001");
        dto.setDescription("这是一个测试组织");
        dto.setIndustry("IT");
        dto.setAddress("北京市海淀区");
        dto.setContactPhone("13800138000");
        dto.setState(1);
        dto.setType(1);

        mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建组织 - 失败（缺少必填字段）")
    void testCreateOrganization_Fail_MissingRequiredField() throws Exception {
        OrganizationDTO dto = new OrganizationDTO();
        // 不设置 name，应该校验失败

        mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("更新组织 - 成功")
    void testUpdateOrganization_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO createDto = new OrganizationDTO();
        createDto.setName("测试组织");
        createDto.setCode("TEST_ORG_UPDATE");
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long organizationId = objectMapper.readTree(response).get("data").asLong();

        // 更新组织
        OrganizationDTO updateDto = new OrganizationDTO();
        updateDto.setName("更新后的组织");
        updateDto.setCode("UPDATED_ORG_001");
        updateDto.setDescription("这是更新后的组织描述");
        updateDto.setState(1);

        mockMvc.perform(put("/api/organization/{id}", organizationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("删除组织 - 成功")
    void testDeleteOrganization_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO createDto = new OrganizationDTO();
        createDto.setName("待删除的测试组织");
        createDto.setCode("TEST_ORG_DELETE");
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long organizationId = objectMapper.readTree(response).get("data").asLong();

        // 删除组织
        mockMvc.perform(delete("/api/organization/{id}", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取组织详情 - 成功")
    void testGetOrganizationById_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO createDto = new OrganizationDTO();
        createDto.setName("测试组织详情");
        createDto.setCode("TEST_ORG_GET");
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long organizationId = objectMapper.readTree(response).get("data").asLong();

        // 获取组织详情
        mockMvc.perform(get("/api/organization/{id}", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询组织 - 成功")
    void testPageOrganizations_Success() throws Exception {
        mockMvc.perform(get("/api/organization/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }
}