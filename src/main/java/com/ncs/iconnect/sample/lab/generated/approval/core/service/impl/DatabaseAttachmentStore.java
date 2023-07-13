package com.ncs.iconnect.sample.lab.generated.approval.core.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.AttachmentDbData;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.AttachmentDbDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.AttachmentStore;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;

@Service
public class DatabaseAttachmentStore implements AttachmentStore {

	private final AttachmentDbDataRepository attachmentDbDataRepository;

	public DatabaseAttachmentStore(AttachmentDbDataRepository attachmentDbDataRepository) {
		this.attachmentDbDataRepository = attachmentDbDataRepository;
	}

	@Override
	public void saveAttachemnt(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO, MultipartFile multipartFile)
			throws IOException {

		Optional<AttachmentDbData> attachmentDbDataOption = this.attachmentDbDataRepository
				.findById(approvalRequestAttachmentDTO.getId());

		AttachmentDbData attachmentDbData = attachmentDbDataOption.orElseGet(() -> {
			AttachmentDbData newAttachmentDbData = new AttachmentDbData();
			newAttachmentDbData.setAttachmentId(approvalRequestAttachmentDTO.getId());
			return newAttachmentDbData;
		});

		attachmentDbData.setData(multipartFile.getBytes());

		this.attachmentDbDataRepository.save(attachmentDbData);

	}

	@Override
	public Resource getAttachment(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO) throws IOException {
		Optional<AttachmentDbData> attachmentDbDataOption = this.attachmentDbDataRepository
				.findById(approvalRequestAttachmentDTO.getId());
		
		AttachmentDbData attachmentDbData = attachmentDbDataOption.orElseGet(()-> {
			return null;
		});
		return new ByteArrayResource(attachmentDbData.getData());
	}

}
