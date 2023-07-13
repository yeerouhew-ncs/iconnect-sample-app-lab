package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;

import org.mapstruct.Mapper;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;

/**
 * Mapper for the entity GeneralApprovalRequest and its DTO GeneralApprovalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeneralApprovalFormMapper extends EntityMapper<GeneralApprovalFormDTO, GeneralApprovalRequestForm> {

    default GeneralApprovalRequestForm fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeneralApprovalRequestForm generalApprovalRequest = new GeneralApprovalRequestForm();
        generalApprovalRequest.setId(id);
        return generalApprovalRequest;
    }
}
