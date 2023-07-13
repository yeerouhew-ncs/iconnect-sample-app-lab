package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ApprovalTemplate entity to be used at runtime,
 * It should only expose minumum info of approval template data to prevent user from changing Approval Behavior
 */
public class ApprovalTemplateRTDTO implements Serializable {

    private String id;

    private String processOwner;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]\\w{3,255}$")
    private String requestTypeKey;

    @NotNull
    private String templateKey;

    @NotNull
    private ApproverSelection approverSelection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessOwner() {
        return processOwner;
    }

    public void setProcessOwner(String processOwner) {
        this.processOwner = processOwner;
    }

    public String getRequestTypeKey() {
        return requestTypeKey;
    }

    public void setRequestTypeKey(String requestTypeKey) {
        this.requestTypeKey = requestTypeKey;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public ApproverSelection getApproverSelection() {
        return approverSelection;
    }

    public void setApproverSelection(ApproverSelection approverSelection) {
        this.approverSelection = approverSelection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        ApprovalTemplateRTDTO approvalTemplateNoAuditDTO = (ApprovalTemplateRTDTO) o;
        if (approvalTemplateNoAuditDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), approvalTemplateNoAuditDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApprovalTemplateDTO{" +
            "id=" + getId() +
            ", processOwner='" + getProcessOwner() + "'" +
            ", requestTypeKey='" + getRequestTypeKey() + "'" +
            ", templateKey='" + getTemplateKey() + "'" +
            ", approverSelection='" + getApproverSelection() + "'" +
            "}";
    }
}
