package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;

/**
 * A GeneralApprovalRequest.
 */
@Entity
@Table(name = ApprovalTables.GENERAL_APPROVAL_REQUEST_TABLE)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GeneralApprovalRequestForm implements ApprovalAwareForm {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEMPLATE_KEY", nullable = false)
    private String templateKey;

    //Adding @lob to this field will lead to "ArrayIndexOutOfBound" exception in findOne method for MySql
    @Column(name = "DESCRIPTION", length=4000)
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="REQUEST_ID")
    private ApprovalRequestEntity approvalRequest = new ApprovalRequestEntity();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApprovalRequestEntity getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(ApprovalRequestEntity approvalRequest) {
		this.approvalRequest = approvalRequest;
	}
}
