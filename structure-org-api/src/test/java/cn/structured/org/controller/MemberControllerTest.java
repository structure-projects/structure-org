package cn.structured.org.controller;

import cn.structured.org.config.AbstractIntegrationTest;
import cn.structured.org.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.structured.org.dto.MemberDTO;
import cn.structured.org.dto.MemberInviteConfirmDTO;
import cn.structured.org.dto.MemberInviteDTO;
import cn.structured.org.dto.OrganizationDTO;
import cn.structured.org.service.IMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IMemberService memberService;

    @Test
    @DisplayName("更新成员 - 成功")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

        // 通过Service创建成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138001");
        createDto.setName("张三");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        Long memberId = memberService.create(createDto);

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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

        // 通过Service创建成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138003");
        createDto.setName("待删除成员");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        Long memberId = memberService.create(createDto);

        // 删除成员
        mockMvc.perform(delete("/api/member/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取成员详情 - 成功")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

        // 通过Service创建成员
        MemberDTO createDto = new MemberDTO();
        createDto.setUserId(1L);
        createDto.setPhone("13800138004");
        createDto.setName("测试成员详情");
        createDto.setSex("M");
        createDto.setDeptId(1L);
        createDto.setState(1);
        createDto.setOrganizationId(organizationId);

        Long memberId = memberService.create(createDto);

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