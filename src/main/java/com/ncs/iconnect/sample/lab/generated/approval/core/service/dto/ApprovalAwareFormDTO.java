package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import java.io.Serializable;

public abstract class ApprovalAwareFormDTO implements Serializable {

	protected static final long serialVersionUID = 1L;

    private Long id;

    private ApprovalRequestDTO approvalRequest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public abstract String getTypeKey();

	public ApprovalRequestDTO getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(ApprovalRequestDTO approvalRequest) {
		this.approvalRequest = approvalRequest;
	}

	@Override
	public String toString() {
		return "ApprovalAwareRequestDTO [id=" + id + ", typeKey=" + getTypeKey() + ", approvalRequest=" + approvalRequest
				+ "]";
	}

}
