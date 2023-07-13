package com.ncs.iconnect.sample.lab.generated.approval.core.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.RequestPermission;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestAttachment;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestAttachmentRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestEntityRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestAttachmentService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestPermissionService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.AttachmentStore;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestAttachmentMapper;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;

@Service
public class ApprovalRequestAttachmentServiceImpl implements ApprovalRequestAttachmentService {

	private final ApprovalRequestAttachmentRepository approvalRequestAttachmentRepository;
	private final ApprovalRequestAttachmentMapper attachmentMapper;
	private final AttachmentStore attachmentStore;
	private final ApprovalRequestEntityRepository approvalRequestRepository;
	private final ApprovalRequestPermissionService approvalRequestPermissionService;
	private final LoginContextHelper loginContextHelper;
	
	public ApprovalRequestAttachmentServiceImpl(ApprovalRequestAttachmentRepository approvalRequestAttachmentRepository,
			ApprovalRequestEntityRepository approvalRequestRepository, ApprovalRequestAttachmentMapper attachmentMapper,
			AttachmentStore attachmentStore, ApprovalRequestPermissionService approvalRequestPermissionService,
			LoginContextHelper loginContextHelper) {
		this.approvalRequestAttachmentRepository = approvalRequestAttachmentRepository;
		this.approvalRequestRepository = approvalRequestRepository;
		this.attachmentMapper = attachmentMapper;
		this.attachmentStore = attachmentStore;
		this.approvalRequestPermissionService = approvalRequestPermissionService;
		this.loginContextHelper = loginContextHelper;
	}

	public List<ApprovalRequestAttachmentDTO> uploadAttachment(Long approvalRequestId, MultipartFile files[])
			throws IOException {

		List<ApprovalRequestAttachmentDTO> attachmentDTOs = new ArrayList<>();
		
		for(MultipartFile file: files) {
			ApprovalRequestAttachmentDTO attachmentDTO = this.uploadAttachment(approvalRequestId, file);
			attachmentDTOs.add(attachmentDTO);
		}
		
		return attachmentDTOs;
	}
	
	private ApprovalRequestAttachmentDTO uploadAttachment(Long approvalRequestId, MultipartFile file)
			throws IOException {

		approvalRequestPermissionService.validatePermission(
				loginContextHelper.getCurrentUserUUID(),
				approvalRequestRepository.findById(approvalRequestId).get(), RequestPermission.UPDATE);
		ApprovalRequestAttachment attachment = this.getAttachment(approvalRequestId, file);
		ApprovalRequestAttachment resultAttachment = this.approvalRequestAttachmentRepository.save(attachment);
		ApprovalRequestAttachmentDTO attachmentDTO = this.attachmentMapper.toDto(resultAttachment);
		this.attachmentStore.saveAttachemnt(attachmentDTO, file);
		return attachmentDTO;
	}

	public void deleteAttachment(Long attachmentId) {
		ApprovalRequestEntity approvalRequestEntity = this.approvalRequestAttachmentRepository.findById(attachmentId).get()
				.getRequest();
		approvalRequestPermissionService.validatePermission(
				loginContextHelper.getCurrentUserUUID(), approvalRequestEntity,
				RequestPermission.UPDATE);
		this.approvalRequestAttachmentRepository.deleteById(attachmentId);
	}

	public ApprovalRequestAttachmentDTO findById(Long attachmentId) {
		ApprovalRequestAttachment resultAttachment = this.approvalRequestAttachmentRepository.findById(attachmentId).get();
		return this.attachmentMapper.toDto(resultAttachment);
	}

	private ApprovalRequestAttachment getAttachment(Long approvalRequestId, MultipartFile file) {
		ApprovalRequestAttachment attachment = new ApprovalRequestAttachment();
		attachment.setFileName(file.getOriginalFilename());
		attachment.setFileSize(file.getSize());
		attachment.setFileType(file.getContentType());
		attachment.setRequest(this.approvalRequestRepository.findById(approvalRequestId).get());
        attachment.setUploadedDate(LocalDate.now());
		return attachment;
	}

	public List<ApprovalRequestAttachmentDTO> findByApprovalRequestId(Long requestId) {
		return this.attachmentMapper.toDto(this.approvalRequestAttachmentRepository.findByApprovalRequestId(requestId));
	}
}
