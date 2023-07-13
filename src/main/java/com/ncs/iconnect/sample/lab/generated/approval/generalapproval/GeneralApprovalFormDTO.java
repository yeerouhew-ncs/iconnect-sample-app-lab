package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalAwareFormDTO;

/**
 * A DTO for the GeneralApprovalRequest entity.
 */
 public class GeneralApprovalFormDTO extends ApprovalAwareFormDTO {

	private static final long serialVersionUID = 1L;
	private String typeKey = "GeneralApproval";

    private String templateKey;

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    private String description;

	public String getTypeKey() {
		return this.typeKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
