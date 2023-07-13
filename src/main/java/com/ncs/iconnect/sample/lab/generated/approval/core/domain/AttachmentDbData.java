package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;

/**
 * Entity that store actual attachment binary data
 *
 */
@Entity
@Table(name = ApprovalTables.ATTACHMENT_DATA)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AttachmentDbData implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ATTACHMENT_ID")
	private Long attachmentId;

	@Lob
	@Column(name = "ATTACHMENT_DATA")
	private byte[] data;

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
