package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;

/**
 * Service that manages attachment
 *
 */
public interface ApprovalRequestAttachmentService {

	List<ApprovalRequestAttachmentDTO> uploadAttachment(Long approvalRequestId, MultipartFile files[]) throws IOException;

	void deleteAttachment(Long attachmentId);

	ApprovalRequestAttachmentDTO findById(Long attachmentId);

	List<ApprovalRequestAttachmentDTO> findByApprovalRequestId(Long requestId);
}
