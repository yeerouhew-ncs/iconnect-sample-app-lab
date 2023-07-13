package com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper;

import org.mapstruct.Mapper;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestAttachment;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestAttachmentDTO;

/**
 * Mapper for the entity ApprovalRequestAttachment and its DTO ApprovalRequestAttachmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApprovalRequestAttachmentMapper extends EntityMapper<ApprovalRequestAttachmentDTO, ApprovalRequestAttachment> {

    default ApprovalRequestAttachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalRequestAttachment approvalRequestAttachment = new ApprovalRequestAttachment();
        approvalRequestAttachment.setId(id);
        return approvalRequestAttachment;
    }
}
