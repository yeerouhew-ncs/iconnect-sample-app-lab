package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalTemplateDataService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for retriving ApprovalTemplateData at runtime.
 */
@RestController
@RequestMapping("/api")
public class ApprovalTemplateDataRTResource {

    private static final String ENTITY_NAME = "approvalTemplateData";
    private final Logger log = LoggerFactory.getLogger(ApprovalTemplateDataRTResource.class);
    private final ApprovalTemplateDataService approvalTemplateDataService;


    public ApprovalTemplateDataRTResource(ApprovalTemplateDataService approvalTemplateDataService) {
        this.approvalTemplateDataService = approvalTemplateDataService;
    }

    /**
     * GET  /approval/approval-template-data : get approvalTemplateData based on given criteria.
     *
     * @param templateId:   Type key of approval request, e.g. "OrderRequest"
     * @return the ResponseEntity with status 200 (OK) and the list of approvalTemplateData in body
     */
    @GetMapping("/approval/approval-template-datas:by-template-id/{templateId}")
    public ResponseEntity<List<ApprovalTemplateDataDTO>> getByTemplateId(@PathVariable String templateId) {
        log.debug("REST request to get a page of ApprovalTemplateData");

        List<ApprovalTemplateDataDTO> approvalTemplateDataDTOs = approvalTemplateDataService.findByTemplateId(templateId);
        return new ResponseEntity<>(approvalTemplateDataDTOs, new HttpHeaders(), HttpStatus.OK);
    }
}
