package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;

/**
 * Basic entity carries information for approver
 *
 */
@Entity
@Table(name = ApprovalTables.APPROVERS)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Approver implements Serializable, Comparable<Approver> {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "APPROVER_ID", nullable = false)
	private String approverId;

    @Column(name = "APPROVER_DISPLAY_NAME")
    private String approverDisplayName;

	@Column(name = "APPROVER_SEQ", nullable = false)
	private Integer approverSeq;

	@Column(name = "APPROVER_TITLE")
	private String approverTitle;

	@Column(name = "ACTUAL_APPROVER_ID")
	private String actualApprover;

	@Column(name = "TASK_ASSIGNED_DATE")
	private LocalDate taskAssignedDate;

    @Column(name = "TASK_COMPLETION_DATE")
    private LocalDate taskCompletionDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "APPROVAL_STATUS")
	private ApprovalStatus approvalStatus;

	@ManyToOne
	@JoinColumn(name = "REQUEST_ID", nullable = false, insertable = false, updatable = false)
	private ApprovalRequestEntity request;

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

	public String getApproverTitle() {
		return approverTitle;
	}

	public void setApproverTitle(String approverTitle) {
		this.approverTitle = approverTitle;
	}

	public ApprovalRequestEntity getRequest() {
		return request;
	}

	public void setRequest(ApprovalRequestEntity request) {
		this.request = request;
	}

	@Override
	public int compareTo(Approver other) {
		if (other == null) {
			return 1;
		}
		
		return ObjectUtils.compare(this.getApproverSeq(), other.getApproverSeq());
	}
}
