package com.ncs.iconnect.sample.lab.generated.approval.customerrequest;

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
import com.ncs.iconnect.sample.lab.generated.approval.DisplayNameUpdater;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.BadRequestAlertException;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestService;
import org.springframework.web.context.request.WebRequest;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CustomerRequest.
 */
@RestController
@RequestMapping("/api/approval")
public class CustomerRequestResource {
	
	private static final MultiInstanceType MULTI_INSTANCE_TYPE = MultiInstanceType.PARALLEL;


	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRequestResource.class);

	private static final String ENTITY_NAME = "customerRequest";
	private static final String ENTITY_NAME_APPROVAL = "customerRequest";
	private final DisplayNameUpdater displayNameUpdater;
	private final ApprovalRequestService approvalRequestService;
	private final CustomerRequestMapper customerRequestMapper;

	private final CustomerRequestService customerRequestService;
	
	public CustomerRequestResource(ApprovalRequestService approvalRequestService,
			DisplayNameUpdater displayNameUpdater,
			CustomerRequestMapper customerRequestMapper, 
			CustomerRequestService customerRequestService) {
		this.approvalRequestService = approvalRequestService;
		this.displayNameUpdater = displayNameUpdater;
		this.customerRequestMapper = customerRequestMapper;
		this.customerRequestService = customerRequestService;
	}

	/**
	 * POST /customer-requests : Create a new customerRequest.
	 *
	 * @param customerRequestDTO
	 *            the customerRequestDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new customerRequestDTO, or with status 400 (Bad Request) if the
	 *         customerRequest has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/customer-requests")
	public ResponseEntity<CustomerRequestDTO> createCustomerRequest(
			@Valid @RequestBody CustomerRequestDTO customerRequestDTO) throws URISyntaxException {
				LOGGER.debug("REST request to save CustomerRequest : {}", customerRequestDTO);
		if (customerRequestDTO.getId() != null) {
			throw new BadRequestAlertException("A new customerRequest cannot already have an ID", ENTITY_NAME,
					"idexists");
		}

		//Set default request summary to typeKey
		if(StringUtils.isEmpty(customerRequestDTO.getApprovalRequest().getSummary())) {
			customerRequestDTO.getApprovalRequest().setSummary(customerRequestDTO.getTypeKey());
		}
		CustomerRequest entity = customerRequestMapper.toEntity(customerRequestDTO);
		entity.getApprovalRequest().setMultiInstanceType(MULTI_INSTANCE_TYPE);
		CustomerRequest customerRequest = (CustomerRequest) approvalRequestService
				.save(entity, StringUtils.capitalize(ENTITY_NAME_APPROVAL));
		CustomerRequestDTO result = customerRequestService.convertToDTO(customerRequest);

		return ResponseEntity.created(new URI("/api/customer-requests/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}
	

	/**
	 * PUT /customer-requests : Updates an existing customerRequest.
	 *
	 * @param customerRequestDTO
	 *            the customerRequestDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         customerRequestDTO, or with status 400 (Bad Request) if the
	 *         customerRequestDTO is not valid, or with status 500 (Internal
	 *         Server Error) if the customerRequestDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/customer-requests")
	public ResponseEntity<CustomerRequestDTO> updateCustomerRequest(
			@Valid @RequestBody CustomerRequestDTO customerRequestDTO) throws URISyntaxException {
				LOGGER.debug("REST request to update CustomerRequest : {}", customerRequestDTO);
		if (customerRequestDTO.getId() == null) {
			return createCustomerRequest(customerRequestDTO);
		}

		CustomerRequest requestToSave = customerRequestMapper.toEntity(customerRequestDTO);

		CustomerRequest customerRequest = (CustomerRequest) approvalRequestService
				.save(requestToSave, StringUtils.capitalize(ENTITY_NAME_APPROVAL));
		CustomerRequestDTO result = customerRequestService.convertToDTO(customerRequest);

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	
	/**
	 * GET /customer-requests/:id : get the "id" customerRequest.
	 *
	 * @param id
	 *            the id of the customerRequestDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         customerRequestDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/customer-requests/{id}")
	public ResponseEntity<CustomerRequestDTO> getCustomerRequest(@PathVariable Long id) {
		LOGGER.debug("REST request to get CustomerRequest : {}", id);
		CustomerRequestDTO result = this.customerRequestService.findOne(id);
		this.displayNameUpdater.updateApprovalRequestUserDisplayName(result.getApprovalRequest());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

	/**
	 * DELETE /customer-requests/:id : delete the "id" customerRequest.
	 *
	 * @param id
	 *            the id of the customerRequestDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/customer-requests/{id}")
	public ResponseEntity<Void> deleteCustomerRequest(@PathVariable Long id) {
		LOGGER.debug("REST request to delete CustomerRequest : {}", id);
		this.customerRequestService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.setDisallowedFields(new String[]{});
    }
}
