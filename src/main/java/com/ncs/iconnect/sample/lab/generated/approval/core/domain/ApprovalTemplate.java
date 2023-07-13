package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Proxy;
import java.io.Serializable;
import com.ncs.itrust5.ss5.domain.AbstractBaseEntityTo;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ApprovalTemplate.
 */
@Entity
@Table(name = "iflow_approval_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
@Proxy(lazy=false)
public class ApprovalTemplate extends AbstractBaseEntityTo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]\\w{3,255}$")
    @Column(name = "request_type_key", nullable = false)
    private String requestTypeKey;

    @NotNull
    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Column(name = "process_owner")
    private String processOwner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "multi_instance_type", nullable = false)
    private MultiInstanceType multiInstanceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_behavior", nullable = false)
    private ApprovalBehavior approvalBehavior;

    @NotNull
    @Column(name = "enable_reject_all", nullable = false)
    private Boolean enableRejectAll;

    @NotNull
    @Column(name = "enable_reject_step", nullable = false)
    private Boolean enableRejectStep;

    @NotNull
    @Column(name = "enable_reject_to_applicant", nullable = false)
    private Boolean enableRejectToApplicant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "approver_selection", nullable = false)
    private ApproverSelection approverSelection;

    @OneToMany(mappedBy = "approvalTemplate",cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ApprovalTemplateData> approvalTemplateData = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestTypeKey() {
        return requestTypeKey;
    }

    public ApprovalTemplate requestTypeKey(String requestTypeKey) {
        this.requestTypeKey = requestTypeKey;
        return this;
    }

    public void setRequestTypeKey(String requestTypeKey) {
        this.requestTypeKey = requestTypeKey;
    }

    public String getProcessOwner() {
        return processOwner;
    }

    public ApprovalTemplate processOwner(String processOwner) {
        this.processOwner = processOwner;
        return this;
    }

    public void setProcessOwner(String processOwner) {
        this.processOwner = processOwner;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public MultiInstanceType getMultiInstanceType() {
        return multiInstanceType;
    }

    public void setMultiInstanceType(MultiInstanceType multiInstanceType) {
        this.multiInstanceType = multiInstanceType;
    }

    public ApprovalTemplate multiInstanceType(MultiInstanceType multiInstanceType) {
        this.multiInstanceType = multiInstanceType;
        return this;
    }

    public ApprovalBehavior getApprovalBehavior() {
        return approvalBehavior;
    }

    public ApprovalTemplate approvalBehavior(ApprovalBehavior approvalBehavior) {
        this.approvalBehavior = approvalBehavior;
        return this;
    }

    public void setApprovalBehavior(ApprovalBehavior approvalBehavior) {
        this.approvalBehavior = approvalBehavior;
    }

    public Boolean isEnableRejectAll() {
        return enableRejectAll;
    }

    public ApprovalTemplate enableRejectAll(Boolean enableRejectAll) {
        this.enableRejectAll = enableRejectAll;
        return this;
    }

    public void setEnableRejectAll(Boolean enableRejectAll) {
        this.enableRejectAll = enableRejectAll;
    }

    public Boolean isEnableRejectStep() {
        return enableRejectStep;
    }

    public ApprovalTemplate enableRejectStep(Boolean enableRejectStep) {
        this.enableRejectStep = enableRejectStep;
        return this;
    }

    public void setEnableRejectStep(Boolean enableRejectStep) {
        this.enableRejectStep = enableRejectStep;
    }

    public Boolean isEnableRejectToApplicant() {
        return enableRejectToApplicant;
    }

    public ApprovalTemplate enableRejectToApplicant(Boolean enableRejectToApplicant) {
        this.enableRejectToApplicant = enableRejectToApplicant;
        return this;
    }

    public void setEnableRejectToApplicant(Boolean enableRejectToApplicant) {
        this.enableRejectToApplicant = enableRejectToApplicant;
    }

    public ApproverSelection getApproverSelection() {
        return approverSelection;
    }

    public ApprovalTemplate approverSelection(ApproverSelection approverSelection) {
        this.approverSelection = approverSelection;
        return this;
    }

    public void setApproverSelection(ApproverSelection approverSelection) {
        this.approverSelection = approverSelection;
    }


    public Set<ApprovalTemplateData> getApprovalTemplateData() {
        return approvalTemplateData;
    }

    public ApprovalTemplate approvalTemplateData(Set<ApprovalTemplateData> approvalTemplateData) {
        this.approvalTemplateData = approvalTemplateData;
        return this;
    }

    public ApprovalTemplate addApprovalTemplateData(ApprovalTemplateData approvalTemplateData) {
        this.approvalTemplateData.add(approvalTemplateData);
        approvalTemplateData.setApprovalTemplate(this);
        return this;
    }

    public ApprovalTemplate removeApprovalTemplateData(ApprovalTemplateData approvalTemplateData) {
        this.approvalTemplateData.remove(approvalTemplateData);
        approvalTemplateData.setApprovalTemplate(null);
        return this;
    }

    public void setApprovalTemplateData(Set<ApprovalTemplateData> approvalTemplateData) {
        this.approvalTemplateData = approvalTemplateData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        ApprovalTemplate approvalTemplate = (ApprovalTemplate) o;
        if (approvalTemplate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), approvalTemplate.getId());
    }

    public ApprovalTemplate templateKey(String defaultTemplateKey) {
        this.templateKey = defaultTemplateKey;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApprovalTemplate{" +
            "id=" + getId() +
            ", requestTypeKey='" + getRequestTypeKey() + "'" +
            ", templateKey='" + this.templateKey + "'" +
            ", processOwner='" + getProcessOwner() + "'" +
            ", approvalBehavior='" + getApprovalBehavior() + "'" +
            ", enableRejectAll='" + isEnableRejectAll() + "'" +
            ", enableRejectStep='" + isEnableRejectStep() + "'" +
            ", enableRejectToApplicant='" + isEnableRejectToApplicant() + "'" +
            ", approverSelection='" + getApproverSelection() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDt='" + getCreatedDt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedDt='" + getUpdatedDt() + "'" +
            "}";
    }
}