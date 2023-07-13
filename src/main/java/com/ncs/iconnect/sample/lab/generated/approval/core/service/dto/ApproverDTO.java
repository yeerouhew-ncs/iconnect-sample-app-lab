package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;

public class ApproverDTO implements Comparable<ApproverDTO>, Serializable {

	protected static final long serialVersionUID = 1L;

	private Long id;

	private String approverId;

	private String approverDisplayName;

	private Integer approverSeq;

	private String approverTitle;

	private String actualApprover;

	private LocalDate taskAssignedDate;

    private LocalDate taskCompletionDate;

	private ApprovalStatus approvalStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverDisplayName() {
		return approverDisplayName;
	}

	public void setApproverDisplayName(String approverDisplayName) {
		this.approverDisplayName = approverDisplayName;
	}

	public Integer getApproverSeq() {
		return approverSeq;
	}

	public void setApproverSeq(Integer approverSeq) {
		this.approverSeq = approverSeq;
	}

    public String getApproverTitle() {
		return approverTitle;
	}

	public void setApproverTitle(String approverTitle) {
		this.approverTitle = approverTitle;
	}

	public String getActualApprover() {
		return actualApprover;
	}

	public void setActualApprover(String actualApprover) {
		this.actualApprover = actualApprover;
	}

	public LocalDate getTaskAssignedDate() {
		return taskAssignedDate;
	}

	public void setTaskAssignedDate(LocalDate taskAssignedDate) {
		this.taskAssignedDate = taskAssignedDate;
	}

    public LocalDate getTaskCompletionDate() {
        return taskCompletionDate;
    }

    public void setTaskCompletionDate(LocalDate taskCompletionDate) {
        this.taskCompletionDate = taskCompletionDate;
    }

    public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	@Override
	public int compareTo(ApproverDTO other) {
		if (other == null) {
			return 1;
		}
		return Integer.compare(this.getApproverSeq(), other.getApproverSeq());
	}
}
