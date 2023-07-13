package com.ncs.iconnect.sample.lab.generated.approval.testdata;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.Approver;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApproverDTO;
import com.ncs.iconnect.sample.lab.generated.approval.generalapproval.GeneralApprovalRequestForm;

public class ApprovalRequestTestData {

	public static ApprovalRequestDTO newApprovalRequestDTO() {
		ApprovalRequestDTO approvalRequestDTO = new ApprovalRequestDTO();
		approvalRequestDTO.setSummary("test general approval dao");
		approvalRequestDTO.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);

		int seq = 0;
		approvalRequestDTO.getApprovers().add(newApproverDTO("DEF-user-useradmin", ++seq));
		approvalRequestDTO.getApprovers().add(newApproverDTO("DEF-user-appadmin", ++seq));
		return approvalRequestDTO;
	}

	private static ApproverDTO newApproverDTO(String approverId, Integer seq) {
		ApproverDTO approver = new ApproverDTO();
		approver.setApproverId(approverId);
		approver.setApproverTitle("Manager " + seq);
		approver.setApproverSeq(seq);
		return approver;
	}

	public static ApprovalAwareForm newApprovalAwareRequest() {
		GeneralApprovalRequestForm dummyApprovalAwareRequest = new GeneralApprovalRequestForm();
		dummyApprovalAwareRequest.setTemplateKey("DEFAULT");
		dummyApprovalAwareRequest.setApprovalRequest(newApprovalRequest());
		return dummyApprovalAwareRequest;
	}

    private static ApprovalRequestEntity newApprovalRequest() {
		ApprovalRequestEntity approvalRequest = new ApprovalRequestEntity();
		approvalRequest.setSummary("test general approval dao");
		approvalRequest.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);

		int seq = 0;
		approvalRequest.getApprovers().add(newApprover("DEF-user-useradmin", ++seq));
		approvalRequest.getApprovers().add(newApprover("DEF-user-appadmin", ++seq));
		return approvalRequest;
	}

	private static Approver newApprover(String approverId, Integer seq) {
		Approver approver = new Approver();
		approver.setApproverId(approverId);
		approver.setApproverTitle("Manager " + seq);
		approver.setApproverSeq(seq);
		return approver;
	}
}
