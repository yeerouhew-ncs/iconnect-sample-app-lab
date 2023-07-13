package com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity ApprovalTemplate and its DTO ApprovalTemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApprovalTemplateMapper extends EntityMapper<ApprovalTemplateRTDTO, ApprovalTemplate> {


    @Mapping(target = "approvalTemplateData", ignore = true)
    ApprovalTemplate toEntity(ApprovalTemplateRTDTO approvalTemplateNoAuditDTO);

    default ApprovalTemplate fromId(String id) {
        if (id == null) {
            return null;
        }
        ApprovalTemplate approvalTemplate = new ApprovalTemplate();
        approvalTemplate.setId(id);
        return approvalTemplate;
    }
}
