package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventDispatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.TaskActionDTO;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;

/**
 * Handle approval workflow action
 *
 */
public interface ApprovalRequestService {

	/**
	 * Save Approval Form
	 * @param approvalAwareRequest: Request
	 * @param requestTypeKey: Key of Request Type
	 * @return: Updated ApprovalAwareForm
	 */
    ApprovalAwareForm save(ApprovalAwareForm approvalAwareRequest, String requestTypeKey);
    
    /**
     * Submit request to initiate approval workflow
     * @param approvalRequestId: Id of Approval Request
     * @param taskActionDTO: TaskAction
     * @return: Updated ApprovalRequestDTO
     */
    ApprovalRequestDTO submit(Long approvalRequestId, TaskActionDTO taskActionDTO);

    /**
     * Approve request, and approval will be route to next approver, or complete approval process if current approver is last approver
     * @param taskAction: Task information
     * @return Updated Approval Request
     */
    ApprovalRequestDTO approve(TaskActionDTO taskAction);

    ApprovalRequestDTO complete(TaskActionDTO taskAction);
 
    /**
     * Reject request, approval process will be completed with "REJECT" outcome
     * @param taskAction: Task information
     * @return Updated Approval Request
     */
    ApprovalRequestDTO reject(TaskActionDTO taskAction);

    /**
     * Reject request, approval process will be send back to previous Approver
     * @param taskAction: Task information
     * @return Updated Approval Request
     */
    ApprovalRequestDTO rejectStep(TaskActionDTO taskAction);

    /**
     * Cancel approval request, and approval will be completed with 'Cancel' outcome
     * @param approvalRequestId : Id of approval request
     * @param taskActionDTO
     * @return Updated Approval Request
     */
    ApprovalRequestDTO cancel(Long approvalRequestId, TaskActionDTO taskActionDTO);

    /**
     * Send back request to initiator (Requestor), and Requestor can make changes to approval form, and resubmit
     * @param taskAction: Task Action DTO
     * @return Updated Approval Request
     */
    ApprovalRequestDTO rollbackToApplicant(TaskActionDTO taskAction);

	Page<ApprovalRequestDTO> findApprovalRequests(String requestTypeKey, Pageable pageable);

    ApprovalRequestEntity findOne(Long id);
    
    void deleteOne(Long id);

	void setLoginContxtHelper(LoginContextHelper loginContextHelper);

    void setApprovalEventDispatcher(ApprovalEventDispatcher approvalEventDispatcher);
}
