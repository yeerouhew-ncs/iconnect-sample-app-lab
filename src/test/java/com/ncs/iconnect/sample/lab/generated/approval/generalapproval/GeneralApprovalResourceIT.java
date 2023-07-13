package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalRequestTestData;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import javax.servlet.Filter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class GeneralApprovalResourceIT {

    @Autowired
    TestTokenProvider testTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneralApprovalFormDTO createEntity() {
        GeneralApprovalFormDTO generalApprovalFormDTO = new GeneralApprovalFormDTO();
        generalApprovalFormDTO.setTemplateKey("DEFAULT");
        generalApprovalFormDTO.setApprovalRequest(ApprovalRequestTestData.newApprovalRequestDTO());
        return generalApprovalFormDTO;
    }

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
        objectMapper.findAndRegisterModules();
    }

    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

    @Test
    @Transactional
    public void createGeneralApprovalRequest() throws Exception {
        // Create the Order
        GeneralApprovalFormDTO generalApprovalFormDTO = createEntity();

        ResultActions result = mockMvc
            .perform(post("/api/approval/general-approvals").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(generalApprovalFormDTO)).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        result.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("id").value(notNullValue()));
    }

    @Test
    @Transactional
    public void getGeneralApprovalRequest() throws Exception {
        GeneralApprovalFormDTO generalApprovalFormDTO = createEntity();

        ResultActions result = mockMvc
            .perform(post("/api/approval/general-approvals").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(generalApprovalFormDTO)).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        GeneralApprovalFormDTO submittedGeneralApprovalFormDTO = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), GeneralApprovalFormDTO.class);

        mockMvc.perform(get("/api/approval/general-approvals/" + submittedGeneralApprovalFormDTO.getId()).header("Authorization",
            "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        result.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("id").value(notNullValue()));

    }

    @Test
    @Transactional
    public void updateGeneralApprovalRequest() throws Exception {
        GeneralApprovalFormDTO generalApprovalFormDTO = createEntity();

        ResultActions result = mockMvc
            .perform(post("/api/approval/general-approvals").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(generalApprovalFormDTO)).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        GeneralApprovalFormDTO submittedGeneralApprovalFormDTO = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), GeneralApprovalFormDTO.class);

        ResultActions updatedResult = mockMvc.perform(put("/api/approval/general-approvals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(submittedGeneralApprovalFormDTO))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        updatedResult.andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    public void deleteGeneralApprovalRequest() throws Exception {
        GeneralApprovalFormDTO generalApprovalFormDTO = createEntity();

        ResultActions result = mockMvc
            .perform(post("/api/approval/general-approvals").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(generalApprovalFormDTO)).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        GeneralApprovalFormDTO submittedGeneralApprovalFormDTO = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), GeneralApprovalFormDTO.class);

        ResultActions deleteResult = mockMvc.perform(
            delete("/api/approval/general-approvals/" + submittedGeneralApprovalFormDTO.getId()).header("Authorization",
                "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        deleteResult.andDo(print()).andExpect(status().is2xxSuccessful());
    }

}
