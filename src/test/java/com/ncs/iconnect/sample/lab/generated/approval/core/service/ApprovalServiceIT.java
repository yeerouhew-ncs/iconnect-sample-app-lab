package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalAction;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventDispatcher;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalHistoryItemDTO;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalTemplateTestData;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApproverDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.TaskActionDTO;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalRequestTestData;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;
import java.util.List;
import java.util.SortedSet;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalServiceIT {
	@Autowired
	private ApprovalRequestService approvalRequestService;

    @Autowired
    private ApprovalTemplateRepository approvalTemplateRepository;

    @Autowired
    private ApprovalTemplateDataRepository approvalTemplateDataRepository;

    @Autowired
	private LoginContextHelper loginContextHelper;

    private LoginContextHelper mockLoginContextHelper = Mockito.mock(LoginContextHelper.class);
    private ApprovalEventDispatcher approvalEventDispatcher = Mockito.mock (ApprovalEventDispatcher.class);

	private String requestTypeKey = "GeneralApproval";

    ApprovalTemplate sequentialApprovalTemplate = null;
    ApprovalTemplate parallelApprovalTemplate = null;

	@BeforeEach
	public void setup(){
		approvalRequestService.setLoginContxtHelper(mockLoginContextHelper);
        approvalRequestService.setApprovalEventDispatcher(approvalEventDispatcher);
		Mockito.when(mockLoginContextHelper.getCurrentUserUUID()).thenReturn("DEF-user-useradmin");
        sequentialApprovalTemplate = prepareSequentialApprovalTemplate();
        parallelApprovalTemplate = prepareParallelApprovalTemplate();
	}

	@AfterEach
    public void cleanup() {
        approvalRequestService.setLoginContxtHelper(this.loginContextHelper);
    }

    public ApprovalTemplate prepareSequentialApprovalTemplate() {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approvalTemplateDataRepository.save(approver1);
        approvalTemplateDataRepository.save(approver2);
        return savedApprovalTemplate;
    }

    public ApprovalTemplate prepareParallelApprovalTemplate() {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newParallelApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approvalTemplateDataRepository.save(approver1);
        approvalTemplateDataRepository.save(approver2);
        return savedApprovalTemplate;
    }

    @Test
	@WithMockUser("useradmin")
	public void saveApprovalAwareRequest() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

 		assertNotNull(approvalAwareRequest.getId());
		
		ApprovalRequestEntity entity = approvalAwareRequest.getApprovalRequest();
		assertEquals(entity.getKey(), requestTypeKey + "-" +  approvalAwareRequest.getId());
	}

	@Test
	public void submitSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());

		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		

		assertNotNull(approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
		
		assertEquals(ApprovalStatus.DRAFT, approver2.getApprovalStatus());

        SortedSet<ApprovalHistoryItemDTO> approvalHistorys = approvalRequest.getApprovalHistoryItems();
        assertEquals(1, approvalHistorys.size());

        ApprovalHistoryItemDTO approvalHistoryDTO = approvalHistorys.first();;

        assertEquals("DEF-user-useradmin", approvalHistoryDTO.getActUserId());
        assertEquals(ApprovalAction.SUBMIT, approvalHistoryDTO.getActionName());
        assertEquals(ApprovalStatus.DRAFT, approvalHistoryDTO.getOldRequestStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistoryDTO.getNewRequestStatus());
        assertEquals("Submit 1", approvalHistoryDTO.getComment());
        assertNotNull(approvalHistoryDTO.getActionDate());
	}
	
	@Test
	@WithMockUser("useradmin")
	public void approveSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
        ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver approve
		ApprovalRequestDTO result1 = approvalRequestService.approve(newTaskAction(approver1.getId(), "Approve 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		assertEquals(ApprovalStatus.APPROVED, approver1.getApprovalStatus());
		assertNotNull(approver1.getTaskAssignedDate());
        assertNotNull(approver1.getTaskCompletionDate());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());
        assertNotNull(approver2.getTaskAssignedDate());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals("DEF-user-useradmin", approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getNewRequestStatus());

        assertEquals("Approve 1", approvalHistory.getComment());

        Mockito.when(mockLoginContextHelper.getCurrentUserUUID()).thenReturn(approver2.getApproverId() + "");

		//Second approver approve
		ApprovalRequestDTO result2 = approvalRequestService.approve(newTaskAction(approver2.getId(), "Approve 2"));
		approvers = result2.getApprovers().toArray(new ApproverDTO[0]);
	    approver2 = approvers[1];
        verify(approvalEventDispatcher, times(3)).dispatchEvents(any());

		assertEquals(ApprovalStatus.APPROVED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.APPROVED, result2.getStatus());
        assertEquals(ApprovalStatus.APPROVED, result2.getStatus());
        assertEquals(3, result2.getApprovalHistoryItems().size());
        ApprovalHistoryItemDTO approvalHistory2 = result2.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver2.getApproverId(), approvalHistory2.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory2.getOldRequestStatus());
        assertEquals(ApprovalStatus.APPROVED, approvalHistory2.getNewRequestStatus());
        assertEquals("Approve 2", approvalHistory2.getComment());
	}

	@Test
	@WithMockUser("useradmin")
	public void rejectSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.reject(newTaskAction(approver1.getId(), "Reject 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvent(any());
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.REJECTED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.DRAFT, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REJECTED, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.REJECTED, approvalHistory.getNewRequestStatus());
        assertEquals("Reject 1", approvalHistory.getComment());
	}

	@Test
	@WithMockUser("useradmin")
	public void requestForChangeSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.rollbackToApplicant(newTaskAction(approver1.getId(), "requestForChange 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvent(any());
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.UNASSIGNED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.UNASSIGNED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, approvalHistory.getNewRequestStatus());
        assertEquals("requestForChange 1", approvalHistory.getComment());
	}
	
	@Test
	@WithMockUser("useradmin")
	public void requestForChangeThenResubmitSubmitSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
        ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.rollbackToApplicant(newTaskAction(approver1.getId(), "Approve 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvent(any());
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.UNASSIGNED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.UNASSIGNED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, result1.getStatus());
		
		ApprovalRequestDTO resubmitedApprovalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

		ApproverDTO[] resubmittedApprovers = resubmitedApprovalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO resubmittedApprover1 = resubmittedApprovers[0];
		ApproverDTO resubmittedApprover2 = resubmittedApprovers[1];
		

		assertNotNull(resubmittedApprover1.getApproverId());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, resubmittedApprover1.getApprovalStatus());

		assertEquals(ApprovalStatus.UNASSIGNED, resubmittedApprover2.getApprovalStatus());

        ApprovalHistoryItemDTO approvalHistory = resubmitedApprovalRequest.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.DRAFT, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getNewRequestStatus());
        assertEquals("Submit 1", approvalHistory.getComment());
	}

	@Test
	@WithMockUser("useradmin")
	public void cancelSequential() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.sequentialApprovalTemplate.getId());

		ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
		ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.cancel(approvalRequest.getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvent(any());
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.DRAFT, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.CANCELLED, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.CANCELLED, approvalHistory.getNewRequestStatus());
        assertEquals("Submit 1", approvalHistory.getComment());
	}
	
	
	@Test
	@WithMockUser("useradmin")
	public void submitParallel() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

		ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
		ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
        verify(approvalEventDispatcher, times(1)).dispatchEvents(any());
		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		

		assertNotNull(approver1.getApproverId());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
		
		assertNotNull(approver2.getApproverId());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());

        ApprovalHistoryItemDTO approvalHistory = approvalRequest.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.DRAFT, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getNewRequestStatus());
        assertEquals("Submit 1", approvalHistory.getComment());
	}
	
	@Test
	@WithMockUser("useradmin")
	public void approveParallelWithUnimiousApproval() {
	    this.parallelApprovalTemplate.setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);
	    this.approvalTemplateRepository.save(parallelApprovalTemplate);

        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());
        approvalAwareForm.getApprovalRequest().setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
		
		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver approve
		ApprovalRequestDTO result1 = approvalRequestService.approve(newTaskAction(approver1.getId(), "Approve 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.APPROVED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, result1.getStatus());

        Mockito.when(mockLoginContextHelper.getCurrentUserUUID()).thenReturn(approver2.getApproverId() + "");

		//Second approver approve
		ApprovalRequestDTO result2 = approvalRequestService.approve(newTaskAction(approver2.getId(), "Approve 2"));
		approvers = result2.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
	    approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.APPROVED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.APPROVED, result2.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result2.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver2.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.APPROVED, approvalHistory.getNewRequestStatus());
        assertEquals("Approve 2", approvalHistory.getComment());
	}

    @Test
    @WithMockUser("useradmin")
    public void approveParallelWithFirstApproval() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

        ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
        ApproverDTO approver1 = approvers[0];

        //First approver approve
        ApprovalRequestDTO result1 = approvalRequestService.approve(newTaskAction(approver1.getId(), "Approve 1"));
        approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
        approver1 = approvers[0];
        ApproverDTO approver2 = approvers[1];

        assertEquals(ApprovalStatus.APPROVED, approver1.getApprovalStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());
        assertEquals(ApprovalStatus.APPROVED, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.APPROVED, approvalHistory.getNewRequestStatus());
        assertEquals("Approve 1", approvalHistory.getComment());
    }

	@Test
	@WithMockUser("useradmin")
	public void rejectParallel() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.reject(newTaskAction(approver1.getId(), "Reject 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.REJECTED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REJECTED, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.REJECTED, approvalHistory.getNewRequestStatus());
        assertEquals("Reject 1", approvalHistory.getComment());
	}

	@Test
	@WithMockUser("useradmin")
	public void requestForChangeParallel() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.rollbackToApplicant(newTaskAction(approver1.getId(), "requestForChangeParallel 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.UNASSIGNED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.UNASSIGNED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, approvalHistory.getNewRequestStatus());
        assertEquals("requestForChangeParallel 1", approvalHistory.getComment());
	}
	
	@Test
	@WithMockUser("useradmin")
	public void requestForChangeThenResubmitParallel() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.rollbackToApplicant(newTaskAction(approver1.getId(), "Approve 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.UNASSIGNED, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.UNASSIGNED, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.REQUEST_FOR_CHANGE, result1.getStatus());
		
		ApprovalRequestDTO resubmitedApprovalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));
		ApproverDTO[] resubmittedApprovers = resubmitedApprovalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO resubmittedApprover1 = resubmittedApprovers[0];
		ApproverDTO resubmittedApprover2 = resubmittedApprovers[1];
		

		assertNotNull(resubmittedApprover1.getApproverId());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, resubmittedApprover1.getApprovalStatus());
		
		assertNotNull(resubmittedApprover2.getApproverId());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, resubmittedApprover2.getApprovalStatus());

        ApprovalHistoryItemDTO approvalHistory = resubmitedApprovalRequest.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.DRAFT, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getNewRequestStatus());
        assertEquals("Submit 1", approvalHistory.getComment());
	}

	@Test
	@WithMockUser("useradmin")
	public void cancelParallel() {
        ApprovalAwareForm approvalAwareForm = ApprovalRequestTestData.newApprovalAwareRequest();
        approvalAwareForm.getApprovalRequest().setTemplateId(this.parallelApprovalTemplate.getId());

        ApprovalAwareForm approvalAwareRequest = approvalRequestService.save(approvalAwareForm, requestTypeKey);
        ApprovalRequestDTO approvalRequest =approvalRequestService.submit(approvalAwareRequest.getApprovalRequest().getId(), newTaskAction(-1L, "Submit 1"));

		ApproverDTO[] approvers = approvalRequest.getApprovers().toArray(new ApproverDTO[0]);
		ApproverDTO approver1 = approvers[0];
		
		//First approver reject
		ApprovalRequestDTO result1 = approvalRequestService.cancel(approvalRequest.getId(), newTaskAction(-1L, "Cancel 1"));
		approvers = result1.getApprovers().toArray(new ApproverDTO[0]);
		approver1 = approvers[0];
		ApproverDTO approver2 = approvers[1];
		
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver1.getApprovalStatus());
		assertEquals(ApprovalStatus.PENDING_APPROVAL, approver2.getApprovalStatus());
		assertEquals(ApprovalStatus.CANCELLED, result1.getStatus());

        ApprovalHistoryItemDTO approvalHistory = result1.getApprovalHistoryItems().stream().reduce((first, second) -> second).get();
        assertEquals(approver1.getApproverId(), approvalHistory.getActUserId());
        assertEquals(ApprovalStatus.PENDING_APPROVAL, approvalHistory.getOldRequestStatus());
        assertEquals(ApprovalStatus.CANCELLED, approvalHistory.getNewRequestStatus());
        assertEquals("Cancel 1", approvalHistory.getComment());
        verify(approvalEventDispatcher, times(1)).dispatchEvent(any());
	}
	
	private TaskActionDTO newTaskAction(Long approverInstanceId, String comment) {
		TaskActionDTO taskAction = new TaskActionDTO();
		taskAction.setApproverInstanceId(approverInstanceId);
		taskAction.setComment(comment);
		return taskAction;
	}
}
