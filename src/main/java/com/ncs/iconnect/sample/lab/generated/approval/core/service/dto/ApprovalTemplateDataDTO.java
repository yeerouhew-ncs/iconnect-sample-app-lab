package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ApprovalTemplateData entity.
 */
public class ApprovalTemplateDataDTO implements Serializable {

    protected static final long serialVersionUID = 1L;
    
    private String id;

    private String approverId;

    private String approverDisplayName;

    private String approverTitle;

    @NotNull
    private Integer approverSeq;

    @NotNull
    private String approveAction;

    @NotNull
    private String rejectAction;

    private String rollBackAction;

    private Boolean enableRollBackAction;

    private String approvalTemplateId;

    private String approvalTemplateTemplateKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getApproverTitle() {
        return approverTitle;
    }

    public void setApproverTitle(String approverTitle) {
        this.approverTitle = approverTitle;
    }

    public Integer getApproverSeq() {
        return approverSeq;
    }

    public void setApproverSeq(Integer approverSeq) {
        this.approverSeq = approverSeq;
    }

    public String getApproveAction() {
        return approveAction;
    }

    public void setApproveAction(String approveAction) {
        this.approveAction = approveAction;
    }

    public String getRejectAction() {
        return rejectAction;
    }

    public void setRejectAction(String rejectAction) {
        this.rejectAction = rejectAction;
    }

    public String getRollBackAction() {
        return rollBackAction;
    }

    public void setRollBackAction(String rollBackAction) {
        this.rollBackAction = rollBackAction;
    }

    public Boolean isEnableRollBackAction() {
        return enableRollBackAction;
    }

    public void setEnableRollBackAction(Boolean enableRollBackAction) {
        this.enableRollBackAction = enableRollBackAction;
    }

    public String getApprovalTemplateId() {
        return approvalTemplateId;
    }

    public void setApprovalTemplateId(String approvalTemplateId) {
        this.approvalTemplateId = approvalTemplateId;
    }

    public String getApprovalTemplateTemplateKey() {
        return approvalTemplateTemplateKey;
    }

    public void setApprovalTemplateTemplateKey(String approvalTemplateTemplateKey) {
        this.approvalTemplateTemplateKey = approvalTemplateTemplateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        ApprovalTemplateDataDTO approvalTemplateDataDTO = (ApprovalTemplateDataDTO) o;
        if (approvalTemplateDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), approvalTemplateDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApprovalTemplateDataDTO{" +
            "id=" + getId() +
            ", approverDisplayName='" + approverDisplayName + "'" +
            ", approverId='" + getApproverId() + "'" +
            ", approverTitle='" + getApproverTitle() + "'" +
            ", approverSeq=" + getApproverSeq() +
            ", approveAction='" + getApproveAction() + "'" +
            ", rejectAction='" + getRejectAction() + "'" +
            ", rollBackAction='" + getRollBackAction() + "'" +
            ", enableRollBackAction='" + isEnableRollBackAction() + "'" +
            ", approvalTemplate=" + getApprovalTemplateId() +
            ", approvalTemplate='" + getApprovalTemplateTemplateKey() + "'" +
            "}";
    }
}
