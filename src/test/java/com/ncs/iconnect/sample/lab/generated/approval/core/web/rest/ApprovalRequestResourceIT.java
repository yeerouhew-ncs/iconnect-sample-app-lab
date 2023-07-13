package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestEntityRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApproverDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.TaskActionDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestMapper;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalRequestTestData;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import org.springframework.http.MediaType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestAttachment;
import org.apache.commons.codec.binary.StringUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.Filter;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalRequestResourceIT {

    @Autowired
    TestTokenProvider testTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    private MockMvc mockMvc;
    @Autowired
    private ApprovalRequestEntityRepository approvalRequestEntityRepository;
    @Autowired
    private ApprovalRequestMapper approvalRequestMapper;

    private String requestTypeKey = "RequestTestGeneralApproval";

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void findbyRequestTypeKey() throws Exception {
        createApprovalRequestEntity();
        ResultActions result = mockMvc.perform(get("/api/approval/approval-requests?page=0&size=20&sort=id,asc&requestTypeKey=" + requestTypeKey)
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        result.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$[0].id").value(notNullValue()));
    }


    @Test
    public void submitApprovalRequest() throws Exception {

        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        ResultActions result = mockMvc.perform(post("/api/approval/approval-requests:submit/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO submittedApprovalRequestDTO = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        result.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").value(submittedApprovalRequestDTO.getId()))
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.submittedDate").value(notNullValue()));

        ApproverDTO approver1 = submittedApprovalRequestDTO.getApprovers().iterator().next();
        assertEquals(2, submittedApprovalRequestDTO.getApprovers().size());
        assertEquals("DEF-user-useradmin", approver1.getApproverId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
        assertNotNull(approver1.getApprovalStatus());
        assertNotNull(approver1.getTaskAssignedDate());

    }

    @Test
    public void approveApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        ResultActions result = mockMvc.perform(post("/api/approval/approval-requests:submit/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));


        ApprovalRequestDTO approvalRequestDTO1 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        TaskActionDTO taskAction1 = this.getTaskAction(approvalRequestDTO1, "DEF-user-useradmin");

        result = mockMvc.perform(post("/api/approval/approval-requests:approve/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAction1))
            .header("Authorization", "Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO2 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);
        result.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.status").value(ApprovalStatus.PENDING_APPROVAL.toString()));

        TaskActionDTO taskAction2 = this.getTaskAction(approvalRequestDTO2, "DEF-user-appadmin");
        result = mockMvc.perform(post("/api/approval/approval-requests:approve/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAction2))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO3 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        result.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.status").value(ApprovalStatus.APPROVED.toString()))
            .andExpect(jsonPath("$.id").value(notNullValue()));

        Iterator<ApproverDTO> iterator = approvalRequestDTO3.getApprovers().iterator();
        ApproverDTO approver1 = iterator.next();
        assertEquals(2, approvalRequestDTO2.getApprovers().size());
        assertEquals(ApprovalStatus.APPROVED, approver1.getApprovalStatus());
        assertNotNull(approver1.getApprovalStatus());
        assertNotNull(approver1.getTaskCompletionDate());
        assertEquals("DEF-user-useradmin", approver1.getActualApprover());

        ApproverDTO approver2 = iterator.next();
        assertEquals(ApprovalStatus.APPROVED, approver2.getApprovalStatus());
        assertEquals("DEF-user-appadmin", approver2.getActualApprover());
        assertNotNull(approver2.getTaskCompletionDate());
        assertNotNull(approver2.getApprovalStatus());

    }

    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }


    private TaskActionDTO getTaskAction(ApprovalRequestDTO generalApprovalRequestDTO, String userId) {
        TaskActionDTO taskAction = new TaskActionDTO();
        taskAction.setApproverInstanceId(getApproverId(generalApprovalRequestDTO, userId));
        taskAction.setComment("Review Task");
        return taskAction;
    }

    private Long getApproverId(ApprovalRequestDTO approvalRequest, String userId) {
        for (ApproverDTO approver : approvalRequest.getApprovers()) {
            if ((approver.getApprovalStatus() == ApprovalStatus.PENDING_APPROVAL)
                && (StringUtils.equals(approver.getApproverId(), userId))) {
                return approver.getId();
            }
        }
        return null;
    }

    @Test
    public void rejectApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        ResultActions result = mockMvc.perform(post("/api/approval/approval-requests:submit/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO1 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);
        TaskActionDTO taskAction1 = this.getTaskAction(approvalRequestDTO1, "DEF-user-useradmin");

        ResultActions result2 = mockMvc.perform(post("/api/approval/approval-requests:reject/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAction1))
            .header("Authorization", "Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));

        result2.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.status").value(ApprovalStatus.REJECTED.toString()))
            .andExpect(jsonPath("$.id").value(notNullValue()));

        ApprovalRequestDTO approvalRequestDTO2 = objectMapper
            .readValue(result2.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        Iterator<ApproverDTO> iterator = approvalRequestDTO2.getApprovers().iterator();
        ApproverDTO approver1 = iterator.next();
        assertEquals(2, approvalRequestDTO2.getApprovers().size());
        assertEquals(ApprovalStatus.REJECTED, approver1.getApprovalStatus());
        assertNotNull(approver1.getApprovalStatus());
        assertEquals("DEF-user-useradmin", approver1.getActualApprover());
    }

    @Test
    public void requestChangeForApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        ResultActions result = mockMvc.perform(post("/api/approval/approval-requests:submit/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO1 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);
        TaskActionDTO taskAction1 = this.getTaskAction(approvalRequestDTO1, "DEF-user-useradmin");

        ResultActions result2 = mockMvc
            .perform(post("/api/approval/approval-requests:rollbackToApplicant/" + approvalRequestDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskAction1))
                .header("Authorization", "Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));

        result2.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.status").value(ApprovalStatus.REQUEST_FOR_CHANGE.toString()))
            .andExpect(jsonPath("$.id").value(notNullValue()));

        ApprovalRequestDTO approvalRequestDTO2 = objectMapper
            .readValue(result2.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        assertEquals(2, approvalRequestDTO2.getApprovers().size());

        Iterator<ApproverDTO> iterator = approvalRequestDTO2.getApprovers().iterator();
        ApproverDTO approver1 = iterator.next();
        assertEquals(ApprovalStatus.UNASSIGNED, approver1.getApprovalStatus());
        assertNotNull(approver1.getApprovalStatus());
    }

    @Test
    public void addAttachmentToApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("system-infrastructure-config.xml");
	        final MockMultipartFile testUploadFile = new MockMultipartFile("fileUploadField",
	                "system-infrastructure-config.xml", "html/text", inputStream);

	            ResultActions uploadResult = mockMvc.perform(MockMvcRequestBuilders
	                .multipart("/api/approval/approval-requests/" + approvalRequestDTO.getId() + "/attachments")
	                .file(testUploadFile).header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

	            uploadResult.andDo(print()).andExpect(status().is2xxSuccessful());
		} finally {
			if(inputStream!=null){
				inputStream.close();
			}
		}
    }


    @Test
    public void cancelApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

        ResultActions result = mockMvc.perform(post("/api/approval/approval-requests:submit/" + approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO1 = objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        ResultActions result2 = mockMvc.perform(post("/api/approval/approval-requests:cancel/" + approvalRequestDTO1.getId())
            .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(new TaskActionDTO("Submit 1")))
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        ApprovalRequestDTO approvalRequestDTO2 = objectMapper
            .readValue(result2.andReturn().getResponse().getContentAsString(), ApprovalRequestDTO.class);

        result2.andDo(print()).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.initiator").value("DEF-user-appadmin"))
            .andExpect(jsonPath("$.status").value(ApprovalStatus.CANCELLED.toString()))
            .andExpect(jsonPath("$.id").value(notNullValue()));

        Iterator<ApproverDTO> iterator = approvalRequestDTO2.getApprovers().iterator();
        ApproverDTO approver1 = iterator.next();
        assertEquals(2, approvalRequestDTO2.getApprovers().size());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
        assertNotNull(approver1.getApprovalStatus());
        assertNull(approver1.getActualApprover());

    }

    private ApprovalRequestDTO createApprovalRequestEntity() {
        ApprovalRequestDTO approvalRequestDTO = ApprovalRequestTestData.newApprovalRequestDTO();
        ApprovalRequestEntity approvalRequestEntity = approvalRequestMapper.toEntity(approvalRequestDTO);
        approvalRequestEntity.setInitiator("DEF-user-appadmin");
        approvalRequestEntity.setCreatedDate(LocalDate.now());
        approvalRequestEntity.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);
        approvalRequestEntity.setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);
        approvalRequestEntity.setApproverSelection(ApproverSelection.FIXED_STEP);
        approvalRequestEntity.setKey("RequestTestGeneralApproval-0");
        ApprovalRequestAttachment attach = new ApprovalRequestAttachment();
        attach.setFileName("test");
        attach.setUploadedDate(LocalDate.now());
        List<ApprovalRequestAttachment> attachments = new ArrayList<>();
        attachments.add(attach);
        approvalRequestEntity.setAttachments(attachments);
        return this.approvalRequestMapper.toDto(this.approvalRequestEntityRepository.save(approvalRequestEntity));
    }

    @Test
    public void findApprovalRequestAttachments() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

         mockMvc.perform(get("/api/approval/approval-requests/"+approvalRequestDTO.getAttachments().get(0).getId()+"/attachments")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
    }

    @Test
    public void getApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

         mockMvc.perform(get("/api/approval/approval-requests/"+approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
    }

    @Test
    public void deleteApprovalRequest() throws Exception {
        ApprovalRequestDTO approvalRequestDTO = createApprovalRequestEntity();

         mockMvc.perform(delete("/api/approval/approval-requests/"+approvalRequestDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
    }
}
