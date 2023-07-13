package com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper;

import org.mapstruct.Mapper;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ApprovalRequestMapper extends EntityMapper<ApprovalRequestDTO, ApprovalRequestEntity> {

    default ApprovalRequestEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalRequestEntity approvalRequest = new ApprovalRequestEntity();
        approvalRequest.setId(id);
        return approvalRequest;
    }
}