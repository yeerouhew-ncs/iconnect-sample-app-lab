package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.Serializable;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;

public class ApprovalRequestDTO implements Serializable{

    protected static final long serialVersionUID = 1L;
    
    private Long id;

    private String key;

    private String templateId;

    private ApprovalBehavior approvalBehavior;

    private ApproverSelection approverSelection;

    private String summary;

    private ApprovalStatus status;

    private MultiInstanceType multiInstanceType;

    private LocalDate createdDate;

    private String initiator;

    private String initiatorDisplayName;

    private LocalDate submittedDate;

    private LocalDate updatedDate;

    private SortedSet<ApproverDTO> approvers = new TreeSet<>();

    private SortedSet<ApprovalHistoryItemDTO> approvalHistoryItems = new TreeSet<>();

    private List<ApprovalRequestAttachmentDTO> attachments = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ApprovalBehavior getApprovalBehavior() {
        return approvalBehavior;
    }

    public void setApprovalBehavior(ApprovalBehavior approvalBehavior) {
        this.approvalBehavior = approvalBehavior;
    }

    private Boolean enableRejectAll = Boolean.TRUE;

    //When enabled, allow approver reject to previous approver
    private Boolean enableRejectStep = Boolean.FALSE;

    //When enabled, allow approver reject to applicant
    private Boolean enableRejectToApplicant = Boolean.FALSE;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public MultiInstanceType getMultiInstanceType() {
        return multiInstanceType;
    }

    public void setMultiInstanceType(MultiInstanceType multiInstanceType) {
        this.multiInstanceType = multiInstanceType;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorDisplayName() {
        return initiatorDisplayName;
    }

    public void setInitiatorDisplayName(String initiatorDisplayName) {
        this.initiatorDisplayName = initiatorDisplayName;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ApproverSelection getApproverSelection() {
        return approverSelection;
    }

    public void setApproverSelection(ApproverSelection approverSelection) {
        this.approverSelection = approverSelection;
    }

    public SortedSet<ApproverDTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(SortedSet<ApproverDTO> approvers) {
        this.approvers = approvers;
    }

    public SortedSet<ApprovalHistoryItemDTO> getApprovalHistoryItems() {
		return approvalHistoryItems;
	}

	public void setApprovalHistoryItems(SortedSet<ApprovalHistoryItemDTO> approvalHistoryItems) {
		this.approvalHistoryItems = approvalHistoryItems;
	}

    public List<ApprovalRequestAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ApprovalRequestAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Boolean getEnableRejectAll() {
        return enableRejectAll;
    }

    public void setEnableRejectAll(Boolean enableRejectAll) {
        this.enableRejectAll = enableRejectAll;
    }

    public Boolean getEnableRejectStep() {
        return enableRejectStep;
    }

    public void setEnableRejectStep(Boolean enableRejectStep) {
        this.enableRejectStep = enableRejectStep;
    }

    public Boolean getEnableRejectToApplicant() {
        return enableRejectToApplicant;
    }

    public void setEnableRejectToApplicant(Boolean enableRejectToApplicant) {
        this.enableRejectToApplicant = enableRejectToApplicant;
    }
}

