package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalTemplateMapper;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validation;
import java.util.Optional;

/**
 * Service Implementation for managing ApprovalTemplate.
 */
@Service
@Transactional
public class ApprovalTemplateService {

    private final Logger log = LoggerFactory.getLogger(ApprovalTemplateService.class);

    private final ApprovalTemplateRepository approvalTemplateRepository;

    private final ApprovalTemplateMapper approvalTemplateMapper;

    private final ApprovalTemplateValidator approvalTemplateValidator;

    public ApprovalTemplateService(ApprovalTemplateRepository approvalTemplateRepository, ApprovalTemplateMapper approvalTemplateMapper, ApprovalTemplateValidator approvalTemplateValidator) {
        this.approvalTemplateRepository = approvalTemplateRepository;
        this.approvalTemplateMapper = approvalTemplateMapper;
        this.approvalTemplateValidator = approvalTemplateValidator;
    }

    /**
     * Save a approvalTemplate.
     *
     * @param approvalTemplate the entity to save
     * @return the persisted entity
     */
    public ApprovalTemplateRTDTO save(ApprovalTemplate approvalTemplate) {
        log.debug("Request to save ApprovalTemplate : {}", approvalTemplate);
        this.approvalTemplateValidator.validate(approvalTemplate);

        //Update Existing approval template
        if(StringUtils.isEmpty(approvalTemplate.getId())){
            validateKeyAndSelectorNotExists(approvalTemplate);
        }
        approvalTemplate.setId(approvalTemplate.getRequestTypeKey()+"-"+approvalTemplate.getTemplateKey());
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(approvalTemplate);
        return approvalTemplateMapper.toDto(savedApprovalTemplate);
    }

    private void validateKeyAndSelectorNotExists(ApprovalTemplate approvalTemplate) {
        boolean existsTemplate = this.approvalTemplateRepository.findByRequestTypeAndTemplate(approvalTemplate.getRequestTypeKey(), approvalTemplate.getTemplateKey()).isPresent();
        if (true==existsTemplate) {
            throw new IllegalArgumentException("ApprovalTemplate already exists with request key " + approvalTemplate.getRequestTypeKey() + " and selector: " + approvalTemplate.getTemplateKey());
		}
    }

    /**
     * Get all the approvalTemplates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApprovalTemplateRTDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalTemplates");
        return approvalTemplateRepository.findAll(pageable)
            .map(approvalTemplateMapper::toDto);
    }


    /**
     * Get one approvalTemplate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ApprovalTemplateRTDTO> findOne(String id) {
        log.debug("Request to get ApprovalTemplate : {}", id);
        return approvalTemplateRepository.findById(id)
            .map(approvalTemplateMapper::toDto);
    }

    /**
     * Delete the approvalTemplate by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ApprovalTemplate : {}", id);
        approvalTemplateRepository.deleteById(id);
    }

    /**
     * Get ApprovalTemplateDTO based on requestTypeKey and templateKey
     * @param requestTypeKey: Type key of approval request, e.g. "OrderRequest"
     * @param templateKey: Selector Key of Approval Template, e.g. "FinanceDepartment"
     * @return the entity
     */
    public Optional<ApprovalTemplateRTDTO> findByRequestTypeAndTemplateKey(String requestTypeKey, String templateKey) {
        log.debug("Request to  findByRequestTypeAndTemplate : {}, {}", requestTypeKey, templateKey);

        String templateKeyDefault = null;
        //If template selector not specified, use default selector
        if(StringUtils.isEmpty(templateKey)) {
            templateKeyDefault = "DEFAULT";
        }else {
        	templateKeyDefault = templateKey;
        }

        String validRequestTypeKey = ValidationUtils.getInstance().getValidInput(requestTypeKey);
        String validTemplateKeyDefault = ValidationUtils.getInstance().getValidInput(templateKeyDefault);
        Optional<ApprovalTemplate>  approvalTemplates = approvalTemplateRepository
        		.findByRequestTypeAndTemplate(validRequestTypeKey, validTemplateKeyDefault);
        return approvalTemplates.map(approvalTemplateMapper::toDto);
    }

    /**
     * Get all the approvalTemplates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApprovalTemplate> findApprovalTemplates(Pageable pageable,String id,String requestTypeKey) {
        log.debug("Request to get all ApprovalTemplates");
        ApprovalTemplate approvalTemplate=new ApprovalTemplate();
        approvalTemplate.setId(id);
        approvalTemplate.setRequestTypeKey(requestTypeKey);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("version")
            .withIgnoreNullValues()
            .withMatcher("id", GenericPropertyMatchers.contains())
            .withMatcher("requestTypeKey", GenericPropertyMatchers.contains());
        Example<ApprovalTemplate> example = Example.of(approvalTemplate, matcher);
        return approvalTemplateRepository.findAll(example,pageable);
    }

    /**
    * Get one approvalTemplate by id.
    *
    * @param id the id of the entity
    * @return the entity
    */
   @Transactional(readOnly = true)
   public ApprovalTemplate findApprovalTemplateById(String id) {
       log.debug("Request to get ApprovalTemplate : {}", id);
       return approvalTemplateRepository.getOne(id);
   }
}
