package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalTemplateDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ApprovalTemplateData.
 */
@Service
@Transactional
public class ApprovalTemplateDataService {

    private final Logger log = LoggerFactory.getLogger(ApprovalTemplateDataService.class);

    private final ApprovalTemplateDataRepository approvalTemplateDataRepository;

    private final ApprovalTemplateDataMapper approvalTemplateDataMapper;

    public ApprovalTemplateDataService(ApprovalTemplateDataRepository approvalTemplateDataRepository, ApprovalTemplateDataMapper approvalTemplateDataMapper) {
        this.approvalTemplateDataRepository = approvalTemplateDataRepository;
        this.approvalTemplateDataMapper = approvalTemplateDataMapper;
    }

    /**
     * Save a approvalTemplateData.
     *
     * @param approvalTemplateDataDTO the entity to save
     * @return the persisted entity
     */
    public ApprovalTemplateDataDTO save(ApprovalTemplateDataDTO approvalTemplateDataDTO) {
        log.debug("Request to save ApprovalTemplateData : {}", approvalTemplateDataDTO);
        ApprovalTemplateData approvalTemplateData = approvalTemplateDataMapper.toEntity(approvalTemplateDataDTO);
        approvalTemplateData = approvalTemplateDataRepository.save(approvalTemplateData);
        return approvalTemplateDataMapper.toDto(approvalTemplateData);
    }

    /**
     * Get all the approvalTemplateData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApprovalTemplateDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalTemplateData");
        return approvalTemplateDataRepository.findAll(pageable)
            .map(approvalTemplateDataMapper::toDto);
    }


    /**
     * Get one approvalTemplateData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ApprovalTemplateDataDTO> findOne(String id) {
        log.debug("Request to get ApprovalTemplateData : {}", id);
        return approvalTemplateDataRepository.findById(id)
            .map(approvalTemplateDataMapper::toDto);
    }

    /**
     * Delete the approvalTemplateData by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ApprovalTemplateData : {}", id);
        approvalTemplateDataRepository.deleteById(id);
    }

    /**
     * Get All ApprovalTemplateDataDTO based on given templateId
     * @param templateId: Id of template
     * @return the entity
     */
    public List<ApprovalTemplateDataDTO> findByTemplateId(String templateId) {
        log.debug("Request to  findByTemplateId : {}", templateId);

        List<ApprovalTemplateData>  approvalTemplateDatas = approvalTemplateDataRepository.findByTemplateId(templateId);
        return this.approvalTemplateDataMapper.toDto(approvalTemplateDatas);
    }
}
