package com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper;

import org.mapstruct.Mapper;

import com.ncs.iconnect.sample.lab.generated.service.mapper.EntityMapper;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.Approver;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApproverDTO;

/**
 * Mapper for the entity Approver and its DTO ApproverDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApproverMapper extends EntityMapper<ApproverDTO, Approver> {

    default Approver fromId(Long id) {
        if (id == null) {
            return null;
        }
        Approver approver = new Approver();
        approver.setId(id);
        return approver;
    }
}
