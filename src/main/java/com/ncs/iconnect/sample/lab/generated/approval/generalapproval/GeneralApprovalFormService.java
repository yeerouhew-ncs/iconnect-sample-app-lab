package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.RequestPermission;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestEntityRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestPermissionService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestMapper;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;

@Service
public class GeneralApprovalFormService {
	private final Logger log = LoggerFactory.getLogger(GeneralApprovalFormService.class);
	
	private final GeneralApprovalFormMapper generalApprovalMapper;
	private final ApprovalRequestMapper approvalRequestMapper;
	private final GeneralApprovalFormRepository generalApprovalRequestRepository;
	private final ApprovalRequestPermissionService approvalRequestPermissionService;
	private final LoginContextHelper loginContextHelper;
	
	public GeneralApprovalFormService(
			GeneralApprovalFormMapper generalApprovalMapper, ApprovalRequestMapper approvalRequestMapper,
			ApprovalRequestEntityRepository approvalRequestRepository,
			GeneralApprovalFormRepository generalApprovalRequestRepository,
			ApprovalRequestPermissionService approvalRequestPermissionService,
			LoginContextHelper loginContextHelper) {
		this.generalApprovalMapper = generalApprovalMapper;
		this.approvalRequestMapper = approvalRequestMapper;
		this.generalApprovalRequestRepository = generalApprovalRequestRepository;
		this.approvalRequestPermissionService = approvalRequestPermissionService;
		this.loginContextHelper = loginContextHelper;
	}

	public void delete(Long id) {
		GeneralApprovalRequestForm entity = this.generalApprovalRequestRepository.findById(id).get();
		if(entity.getApprovalRequest()!=null) {
			this.approvalRequestPermissionService.validatePermission(loginContextHelper.getCurrentUserUUID(), entity.getApprovalRequest(), RequestPermission.DELETE);
		} else {
			log.info("enitty or request entity no found for {}", id);
		}
		this.generalApprovalRequestRepository.deleteById(id);
	}

	public GeneralApprovalFormDTO findOne(Long id) {
		GeneralApprovalRequestForm entity = this.generalApprovalRequestRepository.findById(id).get();
		
		if(entity.getApprovalRequest()!=null) {
			this.approvalRequestPermissionService.validatePermission(loginContextHelper.getCurrentUserUUID(), entity.getApprovalRequest(), RequestPermission.READ);
		} else {
			log.info("enitty or request entity no found for {}", id);
		}
		return convertToDTO(entity);
	}

    public GeneralApprovalFormDTO convertToDTO(GeneralApprovalRequestForm generalApprovalRequest) {
		GeneralApprovalFormDTO result = generalApprovalMapper.toDto(generalApprovalRequest);
		ApprovalRequestDTO approvalRequestDTO = this.approvalRequestMapper.toDto(generalApprovalRequest.getApprovalRequest());
		result.setApprovalRequest(approvalRequestDTO);
		return result;
	}
}
