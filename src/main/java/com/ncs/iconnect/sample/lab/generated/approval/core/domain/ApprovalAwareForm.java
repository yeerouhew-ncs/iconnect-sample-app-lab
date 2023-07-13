package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import java.io.Serializable;

/**
 * A approval form that carries actual approval payload information for approver to make decision on
 */
public interface ApprovalAwareForm extends Serializable {
	
	 Long getId();

	 void setId(Long id);

	 ApprovalRequestEntity getApprovalRequest();

	 void setApprovalRequest(ApprovalRequestEntity approvalRequest);
	
}
