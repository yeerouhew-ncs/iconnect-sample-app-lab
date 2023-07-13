package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;

/**
 * Attachment store for upload and download attachments
 * Actual storage could be in database, local file system, or Cloud store (e.g. S3)
 */
public interface AttachmentStore {
	void saveAttachemnt(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO, MultipartFile multipartFile)
			throws IOException;

	Resource getAttachment(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO) throws IOException;
}
