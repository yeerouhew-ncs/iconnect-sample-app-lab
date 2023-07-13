package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;

/**
 * Entity for attachment meta info, but it does not contain actual attachment content
 *
 */
@Entity
@Table(name = ApprovalTables.ATTACHMENT)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApprovalRequestAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "APPROVAL_REQUEST_FIELD_KEY")
	private String approvalRequestFieldKey = "DEFAULT";

    @NotNull
	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_TYPE")
	private String fileType;

	@Column(name = "FILE_SIZE")
	private Long fileSize;

    @NotNull
    @Column(name = "UPLOADED_DATE")
    private LocalDate uploadedDate;

	@ManyToOne
	private ApprovalRequestEntity request;

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

	public ApprovalRequestEntity getRequest() {
		return request;
	}

	public void setRequest(ApprovalRequestEntity request) {
		this.request = request;
	}

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
