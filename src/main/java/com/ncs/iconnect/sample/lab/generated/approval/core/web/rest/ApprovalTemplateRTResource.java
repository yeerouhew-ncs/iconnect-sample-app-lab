package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalTemplateService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;
import com.ncs.iconnect.sample.lab.generated.security.SecurityUtils;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
/**
 * REST controller for retriving ApprovalTemplate at runtime
 */
@RestController
@RequestMapping("/api")
public class ApprovalTemplateRTResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalTemplateRTResource.class);
    private static final String ENTITY_NAME = "approvalTemplate";
    private final ApprovalTemplateService approvalTemplateService;

    public ApprovalTemplateRTResource(ApprovalTemplateService approvalTemplateService) {
        this.approvalTemplateService = approvalTemplateService;
    }

    /**
     * GET  /approval/approval-templates/:id : get the "id" approvalTemplate.
     *
     * @param requestTypeKey:   Type key of approval request, e.g. "OrderRequest"
     * @param templateKey: Selector Key of Approval Template, e.g. "FinanceDepartment"
     * @return the ResponseEntity with status 200 (OK) and with body the approvalTemplateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/approval/approval-templates:by-selector/{requestTypeKey}/{templateKey}")
    public ResponseEntity<ApprovalTemplateRTDTO> getApprovalTemplateDataByTemplateKey (@PathVariable String requestTypeKey, @PathVariable String templateKey) {
        log.debug("REST request to get getApprovalTemplateDataByTemplateKey : {}", requestTypeKey, templateKey);
        Optional<ApprovalTemplateRTDTO> approvalTemplateDTO = approvalTemplateService.findByRequestTypeAndTemplateKey(requestTypeKey, templateKey);
        return new ResponseEntity<ApprovalTemplateRTDTO>(approvalTemplateDTO.isPresent()?approvalTemplateDTO.get():null, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     *GET  /approvalTemplate : get all the ApprovalTemplate.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of approvalTemplates in body
     */
    @GetMapping("/approvalTemplates")
    public ResponseEntity<List<ApprovalTemplate>> getApprovalTemplate(@ApiParam Pageable pageable,@ApiParam String id,@ApiParam String requestTypeKey) {
        log.debug("REST request to get all approvalTemplates");
        Page<ApprovalTemplate> page = approvalTemplateService.findApprovalTemplates(pageable,id,requestTypeKey);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/approvalTemplate");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /ApprovalTemplate/:id : get the "id" approvalTemplate.
     *
     * @param id the id of the ApprovalTemplate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ApprovalTemplate, or with status 404 (Not Found)
     * @throws GeneralSecurityException 
     */
    @GetMapping("/approvalTemplates/{id}")
    public ResponseEntity<ApprovalTemplate> getApprovalTemplateById(@PathVariable String id)  {
        log.debug("REST request to get ApprovalTemplate : {}", id);
        ApprovalTemplate approvalTemplate = approvalTemplateService.findApprovalTemplateById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(approvalTemplate));
    }
    
	 /**
     * POST  /approvalTemplates : Create a new approvalTemplate.
     *
     * @param approvalTemplate the approvalTemplate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalTemplate, or with status 400 (Bad Request) if the projectEnum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/approvalTemplates")
    public ResponseEntity<ApprovalTemplateRTDTO> createApprovalTemplate(@Valid @RequestBody ApprovalTemplate approvalTemplate) throws URISyntaxException{
    	log.debug("REST request to save approvalTemplate : {}", approvalTemplate);
    	if (approvalTemplate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new approvalTemplate cannot save already have an ID")).body(null);
        }
    	approvalTemplate.setCreatedBy(getCurrentUserName());
    	approvalTemplate.setProcessOwner(getCurrentUserName());
    	approvalTemplate.setCreatedDt(new Timestamp(new Date().getTime()));
    	ApprovalTemplateRTDTO result;
    	try {
    		result=approvalTemplateService.save(approvalTemplate);
		} catch (IllegalArgumentException e) {
			log.error("Create ApprovalTemplate exception trace: {}", e);
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "illegal", e.getMessage())).body(null);
		}catch (UnsupportedOperationException e) {
			log.error("Create ApprovalTemplate exception trace: {}", e);
			if ("Reject to Step is not supported for PARALLEL Approval Workflow!".equals(e.getMessage())) {
	            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "unsupportReject", e.getMessage())).body(null);
			}else {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "unsupportFirstApproval", e.getMessage())).body(null);
			}
		}
    	return ResponseEntity.created(new URI("/api/approvalTemplates/" + result.getId())).headers(HeaderUtil.createAlert("approvalTemplate.created", result.getId().toString())).body(result);
    }
    
    /**
     * PUT  /ApprovalTemplate : Updates an existing approvalTemplate.
     *
     * @param approvalTemplate the approvalTemplate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalTemplates,
     * or with status 400 (Bad Request) if the approvalTemplate is not valid,
     * or with status 500 (Internal Server Error) if the approvalTemplate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/approvalTemplates")
    public ResponseEntity<ApprovalTemplateRTDTO> updateApprovalTemplate(@Valid @RequestBody ApprovalTemplate approvalTemplate) throws URISyntaxException {
        log.debug("REST request to update approvalTemplate : {}", approvalTemplate);
        if (approvalTemplate.getId() == null) {
            return createApprovalTemplate(approvalTemplate);
        }
        approvalTemplate.setUpdatedDt(new Timestamp(new Date().getTime()));
        approvalTemplate.setUpdatedBy(getCurrentUserName());
        
         Set<ApprovalTemplateData> approvers=approvalTemplate.getApprovalTemplateData();
         for (ApprovalTemplateData approvalTemplateData : approvers) {
        	 approvalTemplateData.setApprovalTemplate(approvalTemplate);
		}
        ApprovalTemplateRTDTO result = approvalTemplateService.save(approvalTemplate);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("approvalTemplate.updated", approvalTemplate.getId().toString())).body(result);
    }
    
    /**
     * DELETE  /approvalTemplates/:id : delete the "id" approvalTemplate.
     *
     * @param id the id of the approvalTemplate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/approvalTemplates/{id}")
    public ResponseEntity<Void> deleteApprovalTemplate(@PathVariable String id) {
        log.debug("REST request to delete ApprovalTemplate : {}", id);
        approvalTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("approvalTemplate.deleted", id)).body(null);
    }
    
    private String getCurrentUserName() {
        Optional<String> currentUser=SecurityUtils.getCurrentUserLogin();
        String currentUserName=currentUser.isPresent()?currentUser.get():"ANONYMOUS/USER";
    	return currentUserName.substring(currentUserName.indexOf("/")+1, currentUserName.length());
    }
    
    @InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
