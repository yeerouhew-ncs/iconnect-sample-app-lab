package com.ncs.iconnect.sample.lab.generated.approval.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.AttachmentStore;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.ValidationUtils;

/**
 * Attachment file store
**/
@ConfigurationProperties
public class FileSytstemAttachmentStore implements AttachmentStore {

	@Value("${iconnect.workflow.attachments.directory:}")
	private String attachmentParentDirectory;

	@Override
	public void saveAttachemnt(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO, MultipartFile multipartFile)
			throws IOException {

		try {
			File file = getFile(approvalRequestAttachmentDTO);
			multipartFile.transferTo(file);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Resource getAttachment(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO) throws IOException {
		FileInputStream inputstream = null;
		InputStreamResource res = null;
		try {
			File file = getFile(approvalRequestAttachmentDTO);
			if(file.exists()){
				inputstream = new FileInputStream(file);
			}
			res = new InputStreamResource(inputstream);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		} finally{
			if(inputstream!=null){
				inputstream.close();
			}
		}
		return res;
	}

	private File getFile(ApprovalRequestAttachmentDTO approvalRequestAttachmentDTO) {
		String dir = ValidationUtils.getInstance().getValidInput(this.attachmentParentDirectory);
		String attachId = null;
		String attachmentName="";
		if(null!=approvalRequestAttachmentDTO&& approvalRequestAttachmentDTO.getId()!=null){
			attachId = ValidationUtils.getInstance().getValidInput(approvalRequestAttachmentDTO.getId().toString());
            attachmentName=approvalRequestAttachmentDTO.getFileName();
		}
		String directoryPath = ValidationUtils.getInstance().getValidInput(dir + File.separator + attachId);
		new File(directoryPath).mkdirs();
		String fileName = ValidationUtils.getInstance().getValidInput(directoryPath + File.separator + attachmentName);
		return new File(fileName);
	}
}
