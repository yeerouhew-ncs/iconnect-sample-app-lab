package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestAttachmentService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.AttachmentStore;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.TaskActionDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestMapper;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.ValidationUtils;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ApprovalRequestRequest.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRequestResource {

	private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);

	private static final String ENTITY_NAME = "approvalRequest";
	private static final String ATTACHMENT_NAME = "Attachment";
	private final ApprovalRequestService approvalRequestService;
	private final ApprovalRequestMapper approvalRequestMapper;
	private final ApprovalRequestAttachmentService approvalRequestAttachmentService;
	private final AttachmentStore attachmentStore;

	public ApprovalRequestResource(ApprovalRequestService approvalRequestService,
                                   ApprovalRequestMapper approvalRequestMapper,
                                   ApprovalRequestAttachmentService approvalRequestAttachmentService, AttachmentStore attachmentStore) {
		this.approvalRequestService = approvalRequestService;
		this.approvalRequestMapper = approvalRequestMapper;
		this.approvalRequestAttachmentService = approvalRequestAttachmentService;
		this.attachmentStore = attachmentStore;
    }

	/**
	 * GET approval/approval-requests : get all the approvalRequests.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         approvalRequests in body
	 */
	@GetMapping("/approval/approval-requests")
	public ResponseEntity<List<ApprovalRequestDTO>> findApprovalRequests(String requestTypeKey, Pageable pageable) {
		log.debug("REST request to get a page of ApprovalRequestDTO {}", requestTypeKey);

		Page<ApprovalRequestDTO> page = this.approvalRequestService.findApprovalRequests(requestTypeKey, pageable);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/apiapproval/approval-requests");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@PostMapping("/approval/approval-requests:submit/{id}")
	public ResponseEntity<ApprovalRequestDTO> submitApprovalRequest(@PathVariable Long id, @Valid @RequestBody TaskActionDTO taskActionDTO) {
		log.debug("REST request to submitApprovalRequest  : {} ", id);

		ApprovalRequestDTO result = approvalRequestService.submit(id, taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PostMapping("/approval/approval-requests:approve/{id}")
	public ResponseEntity<ApprovalRequestDTO> approveApprovalRequest(@PathVariable Long id,
			@Valid @RequestBody TaskActionDTO taskActionDTO) {
		log.debug("REST request to approve taskActionDTO : {}", taskActionDTO);

		ApprovalRequestDTO result = approvalRequestService.approve(taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PostMapping("/approval/approval-requests:complete/{id}")
	public ResponseEntity<ApprovalRequestDTO> completeApprovalRequest(@PathVariable Long id,
			@Valid @RequestBody TaskActionDTO taskActionDTO) {
		log.debug("REST request to complete taskActionDTO : {}", taskActionDTO);

		ApprovalRequestDTO result = approvalRequestService.complete(taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PostMapping("/approval/approval-requests:reject/{id}")
	public ResponseEntity<ApprovalRequestDTO> rejectApprovalRequest(@PathVariable Long id,
			@Valid @RequestBody TaskActionDTO taskActionDTO) {
		log.debug("REST request to reject taskActionDTO : {}", taskActionDTO);

		ApprovalRequestDTO result = approvalRequestService.reject(taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PostMapping("/approval/approval-requests:cancel/{id}")
	public ResponseEntity<ApprovalRequestDTO> cancelApprovalRequest(@PathVariable Long id, @Valid @RequestBody TaskActionDTO taskActionDTO) {
		log.debug("REST request to cancel ApprovalRequestRequest : {}", id);

		ApprovalRequestDTO result = approvalRequestService.cancel(id, taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	@PostMapping(path = "/approval/approval-requests/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<ApprovalRequestAttachmentDTO>> addAttachmentsToApprovalRequest(@PathVariable("id") Long approvalRequestId, @RequestParam("fileUploadField") MultipartFile[] files)
    {
		log.debug("REST request to addAttachmentsToApprovalRequest : {}", approvalRequestId);
		boolean uploadChecker = false;
		try {
			uploadChecker = ValidationUtils.getInstance().checkMultipartFiles(files);
            if(!uploadChecker) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ATTACHMENT_NAME, "attachement.upload.error", "Upload file checker error")).body(new ArrayList<>());
            }
			List<ApprovalRequestAttachmentDTO> attachments = this.approvalRequestAttachmentService.uploadAttachment(approvalRequestId, files);
			return ResponseEntity.ok().headers(HeaderUtil.createAlert("approval.approvalRequest.updated", approvalRequestId.toString())).body(attachments);
		} catch (IOException | ValidationException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ATTACHMENT_NAME, "attachement.upload.error", e.getMessage())).body(new ArrayList<>());
		}
	}

	/**
	 * Get all attachments Record of approval request
	 * 
	 * @param approvalRequestId: id of approval request
	 * @return
	 */
	@GetMapping(path = "/approval/approval-requests/{id}/attachments")
	public ResponseEntity<List<ApprovalRequestAttachmentDTO>> findApprovalRequestAttachments(
			@PathVariable("id") Long approvalRequestId) {

		log.debug("REST request to findApprovalRequestAttachments : {}", approvalRequestId);

		List<ApprovalRequestAttachmentDTO> attachments = this.approvalRequestAttachmentService
				.findByApprovalRequestId(approvalRequestId);
		return new ResponseEntity<>(attachments, new HttpHeaders(), HttpStatus.OK);

	}
		
	@GetMapping(path = "/approval/approval-requests/{id}/attachments/{attachmentId}")
	public ResponseEntity<byte[]> downloadAttachment(@PathVariable("id") Long approvalRequestId,
			@PathVariable("attachmentId") Long attachmentId) {

		log.debug("REST request to getAttachments : {}, attachment id: {}", approvalRequestId, attachmentId);

		ApprovalRequestAttachmentDTO attachment = this.approvalRequestAttachmentService.findById(attachmentId);
		Resource resource = null;
		InputStream stream = null;
		try {

			resource = this.attachmentStore.getAttachment(attachment);
			byte[] content = new byte[attachment.getFileSize().intValue()];
			stream = resource.getInputStream();
			IOUtils.read(stream, content);

			return ResponseEntity.ok()
					// Content-Disposition
					.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachment.getId().toString()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + attachment.getFileName())
					// Content-Type
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					// Contet-Length
					.contentLength(attachment.getFileSize()) //
					.body(content);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ATTACHMENT_NAME, "attachement.download.error", e.getMessage()))
					.body("".getBytes());
		} finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					String msg = e.getMessage();
					log.error(msg, e);
				}
			}
		}
	}

	@DeleteMapping(path = "/approval/approval-requests/{approvalRequestId}/attachments/{attachmentId}")
	public ResponseEntity<Long> deleteAttachmentsFromApprovalRequest(
			@PathVariable("approvalRequestId") Long approvalRequestId,
			@PathVariable("attachmentId") Long attachmentId) {

		log.debug("REST request to deleteAttachmentsFromApprovalRequest : {}, {}", approvalRequestId, attachmentId);

		this.approvalRequestAttachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("approval.approvalRequest.updated", approvalRequestId.toString())).body(attachmentId);
	}

	@PostMapping("/approval/approval-requests:rollbackToApplicant/{id}")
	public ResponseEntity<ApprovalRequestDTO> rollbackToApplicant(@PathVariable Long id,
			@Valid @RequestBody TaskActionDTO taskActionDTO) throws URISyntaxException {

		log.debug("REST request to requestChangeForApprovalRequest : {}", taskActionDTO);

		ApprovalRequestDTO result = approvalRequestService.rollbackToApplicant(taskActionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * GET approval/approval-requests/:id : get the "id" approvalRequest.
	 *
	 * @param id
	 *            the id of the approvalRequestDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         approvalRequestDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/approval/approval-requests/{id}")
	public ResponseEntity<ApprovalRequestDTO> getApprovalRequest(@PathVariable Long id) {
		log.debug("REST request to get ApprovalRequestRequest : {}", id);
		ApprovalRequestEntity approvalRequestEntity = this.approvalRequestService.findOne(id);
		ApprovalRequestDTO result = this.approvalRequestMapper.toDto(approvalRequestEntity);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

	@DeleteMapping("/approval/approval-requests/{id}")
	public ResponseEntity<Long> deleteApprovalRequest(@PathVariable Long id) {
		log.debug("REST request to delete ApprovalRequestRequest : {}", id);
		this.approvalRequestService.findOne(id);
		log.warn("REST request to delete Request with Key");
		log.error("Please invoke actual repo to delete request ");
		this.approvalRequestService.deleteOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(id));
	}

	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}

}
