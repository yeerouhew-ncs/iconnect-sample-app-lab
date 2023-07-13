package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ApprovalTemplateData.
 */
@Entity
@Table(name = "iflow_approval_template_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class ApprovalTemplateData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "approver_display_name")
    private String approverDisplayName;

    @Column(name = "approver_title")
    private String approverTitle;

    @NotNull
    @Column(name = "approver_seq", nullable = false)
    private Integer approverSeq;

    @ManyToOne
    @JsonIgnoreProperties("approvalTemplateData")
    private ApprovalTemplate approvalTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApproverId() {
        return approverId;
    }

    public ApprovalTemplateData approverId(String approverId) {
        this.approverId = approverId;
        return this;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApproverDisplayName() {
        return approverDisplayName;
    }

    public ApprovalTemplateData approverDisplayName(String approverDisplayName) {
        this.approverDisplayName = approverDisplayName;
        return this;
    }

    public void setApproverDisplayName(String approverDisplayName) {
        this.approverDisplayName = approverDisplayName;
    }

    public String getApproverTitle() {
        return approverTitle;
    }

    public ApprovalTemplateData approverTitle(String approverTitle) {
        this.approverTitle = approverTitle;
        return this;
    }

    public void setApproverTitle(String approverTitle) {
        this.approverTitle = approverTitle;
    }

    public Integer getApproverSeq() {
        return approverSeq;
    }

    public ApprovalTemplateData approverSeq(Integer approverSeq) {
        this.approverSeq = approverSeq;
        return this;
    }

    public void setApproverSeq(Integer approverSeq) {
        this.approverSeq = approverSeq;
    }

    public ApprovalTemplate getApprovalTemplate() {
        return approvalTemplate;
    }

    public ApprovalTemplateData approvalTemplate(ApprovalTemplate approvalTemplate) {
        this.approvalTemplate = approvalTemplate;
        return this;
    }

    public void setApprovalTemplate(ApprovalTemplate approvalTemplate) {
        this.approvalTemplate = approvalTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        ApprovalTemplateData approvalTemplateData = (ApprovalTemplateData) o;
        if (approvalTemplateData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), approvalTemplateData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApprovalTemplateData{" +
            "id=" + getId() +
            ", approverId='" + getApproverId() + "'" +
            ", approverDisplayName='" + getApproverDisplayName() + "'" +
            ", approverTitle='" + getApproverTitle() + "'" +
            ", approverSeq=" + getApproverSeq() +
            "}";
    }
}
