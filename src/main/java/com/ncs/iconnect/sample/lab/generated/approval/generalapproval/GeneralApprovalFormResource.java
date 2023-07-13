package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import com.ncs.iconnect.sample.lab.generated.approval.DisplayNameUpdater;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.BadRequestAlertException;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestService;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing GeneralApprovalRequest.
 */
@RestController
@RequestMapping("/api")
public class GeneralApprovalFormResource {

	private final Logger log = LoggerFactory.getLogger(GeneralApprovalFormResource.class);

	private static final String ENTITY_NAME = "generalApproval";
	private final ApprovalRequestService approvalRequestService;
	private final DisplayNameUpdater displayNameUpdater;
	private final GeneralApprovalFormMapper generalApprovalMapper;

	private final GeneralApprovalFormService generalApprovalRequestService;
	
	public GeneralApprovalFormResource(ApprovalRequestService approvalRequestService,
                                       DisplayNameUpdater displayNameUpdater, GeneralApprovalFormMapper generalApprovalMapper,
                                       GeneralApprovalFormService generalApprovalRequestService) {
		this.approvalRequestService = approvalRequestService;
        this.displayNameUpdater = displayNameUpdater;
        this.generalApprovalMapper = generalApprovalMapper;
		this.generalApprovalRequestService = generalApprovalRequestService;
	}

	/**
	 * POST /approval/general-approvals : Create a new generalApproval.
	 *
	 * @param generalApprovalDTO
	 *            the generalApprovalDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new generalApprovalDTO, or with status 400 (Bad Request) if the
	 *         generalApproval has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/approval/general-approvals")
	public ResponseEntity<GeneralApprovalFormDTO> createGeneralApproval(
			@Valid @RequestBody GeneralApprovalFormDTO generalApprovalDTO) throws URISyntaxException {
		log.debug("REST request to save GeneralApprovalRequest : {}", generalApprovalDTO);
		if (generalApprovalDTO.getId() != null) {
			throw new BadRequestAlertException("A new generalApproval cannot already have an ID", ENTITY_NAME,
					"idexists");
		}

		GeneralApprovalRequestForm entity = generalApprovalMapper.toEntity(generalApprovalDTO);
		entity.getApprovalRequest().setMultiInstanceType(MultiInstanceType.SEQUENTIAL);
		GeneralApprovalRequestForm generalApprovalRequest = (GeneralApprovalRequestForm) approvalRequestService
				.save(entity, StringUtils.capitalize(ENTITY_NAME));
		GeneralApprovalFormDTO result = generalApprovalRequestService.convertToDTO(generalApprovalRequest);

		return ResponseEntity.created(new URI("/api/approval/general-approvals/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlertNoAppName(ENTITY_NAME, result.getId().toString())).body(result);
	}
	

	/**
	 * PUT /approval/general-approvals : Updates an existing generalApproval.
	 *
	 * @param generalApprovalDTO
	 *            the generalApprovalDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         generalApprovalDTO, or with status 400 (Bad Request) if the
	 *         generalApprovalDTO is not valid, or with status 500 (Internal
	 *         Server Error) if the generalApprovalDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/approval/general-approvals")
	public ResponseEntity<GeneralApprovalFormDTO> updateGeneralApproval(
			@Valid @RequestBody GeneralApprovalFormDTO generalApprovalDTO) throws URISyntaxException {
		log.debug("REST request to update GeneralApprovalRequest : {}", generalApprovalDTO);
		if (generalApprovalDTO.getId() == null) {
			return createGeneralApproval(generalApprovalDTO);
		}

		GeneralApprovalRequestForm requestToSave = generalApprovalMapper.toEntity(generalApprovalDTO);

		GeneralApprovalRequestForm generalApprovalRequest = (GeneralApprovalRequestForm) approvalRequestService
				.save(requestToSave, StringUtils.capitalize(ENTITY_NAME));
		GeneralApprovalFormDTO result = generalApprovalRequestService.convertToDTO(generalApprovalRequest);

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlertNoAppName(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	
	/**
	 * GET /approval/general-approvals/:id : get the "id" generalApproval.
	 *
	 * @param id
	 *            the id of the generalApprovalDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         generalApprovalDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/approval/general-approvals/{id}")
	public ResponseEntity<GeneralApprovalFormDTO> getGeneralApproval(@PathVariable Long id) {
		log.debug("REST request to get GeneralApprovalRequest : {}", id);
		GeneralApprovalFormDTO result = this.generalApprovalRequestService.findOne(id);
		this.displayNameUpdater.updateApprovalRequestUserDisplayName(result.getApprovalRequest());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

	/**
	 * DELETE /approval/general-approvals/:id : delete the "id" generalApproval.
	 *
	 * @param id
	 *            the id of the generalApprovalDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/approval/general-approvals/{id}")
	public ResponseEntity<Void> deleteGeneralApproval(@PathVariable Long id) {
		log.debug("REST request to delete GeneralApprovalRequest : {}", id);
		this.generalApprovalRequestService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlertNoAppName(ENTITY_NAME, id.toString())).build();
	}

	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.setDisallowedFields(new String[]{});
    }
}
