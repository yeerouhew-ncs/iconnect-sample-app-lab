package com.ncs.iconnect.sample.lab.generated.approval.customerrequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.RequestPermission;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestPermissionService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestMapper;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;

/**
 * Service Implementation for managing CustomerRequest.
 */
@Service
@Transactional
public class CustomerRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRequestService.class);

    private final CustomerRequestRepository customerRequestRepository;

    private final CustomerRequestMapper customerRequestMapper;

    private final ApprovalRequestMapper approvalRequestMapper;

    private final ApprovalRequestPermissionService approvalRequestPermissionService;

    private final LoginContextHelper loginContextHelper;

    public CustomerRequestService(CustomerRequestRepository customerRequestRepository, CustomerRequestMapper customerRequestMapper, 
            ApprovalRequestMapper approvalRequestMapper,
            ApprovalRequestPermissionService approvalRequestPermissionService,
            LoginContextHelper loginContextHelper) {
        this.customerRequestRepository = customerRequestRepository;
        this.customerRequestMapper = customerRequestMapper;
        this.approvalRequestMapper = approvalRequestMapper;
        this.approvalRequestPermissionService = approvalRequestPermissionService;
        this.loginContextHelper = loginContextHelper;
    }

    /**
     * Get one customerRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CustomerRequestDTO findOne(Long id) {
        LOGGER.debug("Request to get CustomerRequest : {}", id);
        CustomerRequest customerRequest = this.customerRequestRepository.findById(id).get();
        validatePermission(customerRequest, RequestPermission.READ);
		return convertToDTO(customerRequest);
    }

    /**
     * Delete the customerRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        LOGGER.debug("Request to delete CustomerRequest : {}", id);
        CustomerRequest customerRequest = this.customerRequestRepository.findById(id).get();
        validatePermission(customerRequest, RequestPermission.DELETE);
        
        customerRequestRepository.deleteById(id);
    }
 
    private void validatePermission(CustomerRequest customerRequest, RequestPermission requestPermission){
		if((null!=customerRequest) && (customerRequest.getApprovalRequest()!=null)) {
			this.approvalRequestPermissionService.validatePermission(loginContextHelper.getCurrentUserUUID(), customerRequest.getApprovalRequest(), requestPermission);
		}
    }
    
    public CustomerRequestDTO convertToDTO(CustomerRequest customerRequest) {
        CustomerRequestDTO result = this.customerRequestMapper.toDto(customerRequest);
        
        ApprovalRequestDTO approvalRequestDTO = this.approvalRequestMapper.toDto(customerRequest.getApprovalRequest());
		result.setApprovalRequest(approvalRequestDTO);
        return result;
	}
}
