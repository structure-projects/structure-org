package cn.structured.org.controller;

import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.DeptDTO;
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
 * 部门管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("部门管理接口测试")
class DeptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("创建部门 - 成功")
    void testCreateDept_Success() throws Exception {
        DeptDTO dto = new DeptDTO();
        dto.setName("测试部门");
        dto.setParentId(0L);
        dto.setSort(1);
        dto.setEnabled(true);

        mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建部门 - 失败（缺少必填字段）")
    void testCreateDept_Fail_MissingRequiredField() throws Exception {
        DeptDTO dto = new DeptDTO();
        // 不设置 name，应该校验失败

        mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("更新部门 - 成功")
    void testUpdateDept_Success() throws Exception {
        // 先创建一个部门
        DeptDTO createDto = new DeptDTO();
        createDto.setName("测试部门");
        createDto.setParentId(0L);
        createDto.setSort(1);
        createDto.setEnabled(true);

        String response = mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long deptId = objectMapper.readTree(response).get("data").asLong();

        // 更新部门
        DeptDTO updateDto = new DeptDTO();
        updateDto.setName("更新后的部门");
        updateDto.setParentId(0L);
        updateDto.setSort(2);
        updateDto.setEnabled(true);

        mockMvc.perform(put("/api/dept/{id}", deptId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("删除部门 - 成功")
    void testDeleteDept_Success() throws Exception {
        // 先创建一个部门
        DeptDTO createDto = new DeptDTO();
        createDto.setName("待删除的测试部门");
        createDto.setParentId(0L);
        createDto.setSort(1);
        createDto.setEnabled(true);

        String response = mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long deptId = objectMapper.readTree(response).get("data").asLong();

        // 删除部门
        mockMvc.perform(delete("/api/dept/{id}", deptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取部门详情 - 成功")
    void testGetDeptById_Success() throws Exception {
        // 先创建一个部门
        DeptDTO createDto = new DeptDTO();
        createDto.setName("测试部门详情");
        createDto.setParentId(0L);
        createDto.setSort(1);
        createDto.setEnabled(true);

        String response = mockMvc.perform(post("/api/dept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long deptId = objectMapper.readTree(response).get("data").asLong();

        // 获取部门详情
        mockMvc.perform(get("/api/dept/{id}", deptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询部门 - 成功")
    void testPageDepts_Success() throws Exception {
        mockMvc.perform(get("/api/dept/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("获取部门下拉选 - 成功")
    void testGetDeptOptions_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_OPTIONS");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取组织 ID
        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        mockMvc.perform(get("/api/dept/options")
                        .param("organizationId", organizationId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("获取部门列表 - 成功")
    void testGetDeptList_Success() throws Exception {
        // 先创建一个组织
        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName("测试组织");
        orgDto.setCode("TEST_ORG_LIST");
        orgDto.setState(1);

        String orgResponse = mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orgDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取组织 ID
        Long organizationId = objectMapper.readTree(orgResponse).get("data").asLong();

        mockMvc.perform(get("/api/dept/list")
                        .param("organizationId", organizationId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }
}