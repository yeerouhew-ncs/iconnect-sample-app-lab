package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import java.io.Serializable;

public class ApprovalRequestAttachmentDTO implements Serializable{

	protected static final long serialVersionUID = 1L;

	private Long id;
	private String approvalRequestFieldKey;
	private String fileName;
	private String fileType;
	private Long fileSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApprovalRequestFieldKey() {
		return approvalRequestFieldKey;
	}

	public void setApprovalRequestFieldKey(String approvalRequestFieldKey) {
		this.approvalRequestFieldKey = approvalRequestFieldKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
