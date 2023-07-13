package com.ncs.iconnect.sample.lab.generated.approval.testdata;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

public class ApprovalTemplateTestData {
    public static ApprovalTemplateRTDTO newApprovalTemplateDTO(){
        ApprovalTemplateRTDTO approvalTemplateNoAuditDTO = new ApprovalTemplateRTDTO();
        approvalTemplateNoAuditDTO.setRequestTypeKey("GeneralApproval");
        approvalTemplateNoAuditDTO.setTemplateKey("DEFAULT");
        return approvalTemplateNoAuditDTO;
    }

    public static ApprovalTemplateDataDTO newApprovalTemplateDataDTO(String templateId, String approverId, Integer approverSeq){
        ApprovalTemplateDataDTO approvalTemplateDataDTO = new ApprovalTemplateDataDTO();
        
        approvalTemplateDataDTO.setApprovalTemplateId(templateId);
        approvalTemplateDataDTO.setApproverId(approverId);
        approvalTemplateDataDTO.setApproverSeq(approverSeq);
        approvalTemplateDataDTO.setApproverTitle("Approver");
        return approvalTemplateDataDTO;
    }

    public static ApprovalTemplate newParallelApprovalTemplate(){
        ApprovalTemplate approvalTemplate = new ApprovalTemplate();
        approvalTemplate.setId(UUID.randomUUID().toString());
        approvalTemplate.setRequestTypeKey("GeneralApproval");
        approvalTemplate.setTemplateKey("PARALLEL_SELECTOR");
        approvalTemplate.setMultiInstanceType(MultiInstanceType.PARALLEL);
        approvalTemplate.setApprovalBehavior(ApprovalBehavior.FIRST_APPROVAL);
        approvalTemplate.setApproverSelection(ApproverSelection.FIXED_STEP);
        approvalTemplate.setEnableRejectAll(Boolean.TRUE);
        approvalTemplate.setEnableRejectStep(Boolean.FALSE);
        approvalTemplate.setEnableRejectToApplicant(Boolean.FALSE);
        approvalTemplate.setCreatedDt(new Timestamp(System.currentTimeMillis()));
        approvalTemplate.setCreatedBy("appadmin");
        return approvalTemplate;
    }

    public static ApprovalTemplate newSequentialApprovalTemplate(){
        ApprovalTemplate approvalTemplate = new ApprovalTemplate();
        approvalTemplate.setId(UUID.randomUUID().toString());
        approvalTemplate.setRequestTypeKey("GeneralApproval");
        approvalTemplate.setTemplateKey("DEFAULT");
        approvalTemplate.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);
        approvalTemplate.setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);
        approvalTemplate.setApproverSelection(ApproverSelection.FIXED_STEP);
        approvalTemplate.setEnableRejectAll(Boolean.TRUE);
        approvalTemplate.setEnableRejectStep(Boolean.TRUE);
        approvalTemplate.setEnableRejectToApplicant(Boolean.TRUE);
        approvalTemplate.setCreatedDt(new Timestamp(System.currentTimeMillis()));
        approvalTemplate.setCreatedBy("appadmin");
        return approvalTemplate;
    }

    public static ApprovalTemplateData newApprovalTemplateData(ApprovalTemplate approvalTemplate, String approverId, Integer approverSeq){
        ApprovalTemplateData approvalTemplateData = new ApprovalTemplateData();
        approvalTemplateData.setApprovalTemplate(approvalTemplate);
        approvalTemplateData.setApproverId(approverId);
        approvalTemplateData.setApproverSeq(approverSeq);
        approvalTemplateData.setApproverTitle("Line Manager");
        return approvalTemplateData;
    }
}
