package com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity ApprovalTemplateData and its DTO ApprovalTemplateDataDTO.
 */
@Mapper(componentModel = "spring", uses = {ApprovalTemplateMapper.class})
public interface ApprovalTemplateDataMapper extends EntityMapper<ApprovalTemplateDataDTO, ApprovalTemplateData> {

    @Mapping(source = "approvalTemplate.id", target = "approvalTemplateId")
    @Mapping(source = "approvalTemplate.templateKey", target = "approvalTemplateTemplateKey")
    ApprovalTemplateDataDTO toDto(ApprovalTemplateData approvalTemplateData);

    @Mapping(source = "approvalTemplateId", target = "approvalTemplate")
    ApprovalTemplateData toEntity(ApprovalTemplateDataDTO approvalTemplateDataDTO);

    default ApprovalTemplateData fromId(String id) {
        if (id == null) {
            return null;
        }
        ApprovalTemplateData approvalTemplateData = new ApprovalTemplateData();
        approvalTemplateData.setId(id);
        return approvalTemplateData;
    }
}
