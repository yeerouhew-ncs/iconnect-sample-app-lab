package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;

/**
 * Basic entity for approval related attributes, it does not contains approval payload information
 *
 */
@Entity
@Table(name = ApprovalTables.APPROVAL_REQUEST)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApprovalRequestEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name = "REQUEST_KEY", nullable = false)
	private String key;

	//Approval Template Id
    @Column(name = "TEMPLATE_ID")
    private String templateId;

    @NotNull
    @Column(name = "APPROVAL_BEHAVIOR", nullable = false)
    private ApprovalBehavior approvalBehavior;

    @Column(name = "enable_reject_all")
    private Boolean enableRejectAll = Boolean.TRUE;

    //When enabled, allow approver reject to previous approver
    @Column(name = "enable_reject_step")
    private Boolean enableRejectStep = Boolean.FALSE;

    //When enabled, allow approver reject to applicant
    @Column(name = "enable_reject_to_applicant")
    private Boolean enableRejectToApplicant = Boolean.FALSE;

    @NotNull
    @Column(name = "APPROVER_SELECTION", nullable = false)
    private ApproverSelection approverSelection;

	@NotNull
	@Column(name = "SUMMARY", nullable = false)
	private String summary;

	@Column(name = "MULTI_INSTANCE_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private MultiInstanceType multiInstanceType = MultiInstanceType.SEQUENTIAL;

	@Column(name = "APPROVAL_STATUS")
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status = ApprovalStatus.DRAFT;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "CREATED_DATE", nullable = false)
	private LocalDate createdDate;

	@Column(name = "INITIATOR", nullable = false)
	private String initiator;

	@Column(name = "SUBMITTED_DATE")
	private LocalDate submittedDate;

	@Column(name = "UPDATED_DATE")
	private LocalDate updatedDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "COMPLETED_DATE")
	private LocalDate completedDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="REQUEST_ID",nullable=false)
    @SortNatural
	private List<ApprovalHistoryItem> approvalHistoryItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="REQUEST_ID",nullable=false)
    @SortNatural
    private SortedSet<Approver> approvers = new TreeSet<>();
	
	@Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "request")
    private List<ApprovalRequestAttachment> attachments = new ArrayList<>();


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

    public ApproverSelection getApproverSelection() {
        return approverSelection;
    }

    public void setApproverSelection(ApproverSelection approverSelection) {
        this.approverSelection = approverSelection;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public MultiInstanceType getMultiInstanceType() {
		return multiInstanceType;
	}

	public void setMultiInstanceType(MultiInstanceType multiInstanceType) {
		this.multiInstanceType = multiInstanceType;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDate getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(LocalDate completedDate) {
		this.completedDate = completedDate;
	}

	public SortedSet<Approver> getApprovers() {
		return approvers;
	}


	public void setApprovers(SortedSet<Approver> approvers) {
		this.approvers = approvers;
	}

	public List<ApprovalHistoryItem> getApprovalHistoryItems() {
        return approvalHistoryItems;
    }

    public void setApprovalHistoryItems(List<ApprovalHistoryItem> approvalHistoryItems) {
        this.approvalHistoryItems = approvalHistoryItems;
    }

    public List<ApprovalRequestAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ApprovalRequestAttachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "ApprovalRequest [id=" + id + ", summary=" + summary + ", multiInstanceType=" + multiInstanceType
				+ ", status=" + status + ", createdDate=" + createdDate
				+ ", initiator=" + initiator + ", submittedDate=" + submittedDate + ", updatedDate=" + updatedDate
				+ ", updatedBy=" + updatedBy + ", completedDate=" + completedDate + "]";
	}
}
