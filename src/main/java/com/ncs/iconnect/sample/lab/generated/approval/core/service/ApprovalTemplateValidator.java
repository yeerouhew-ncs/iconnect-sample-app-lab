package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import org.springframework.stereotype.Service;

@Service
public class ApprovalTemplateValidator {

    public void validate(ApprovalTemplate approvalTemplate){
        validateApprovalSetting(approvalTemplate);
        validateRejectSetting(approvalTemplate);
    }

    private void validateApprovalSetting(ApprovalTemplate approvalTemplate) {
        if(MultiInstanceType.SEQUENTIAL==approvalTemplate.getMultiInstanceType()
            && ApprovalBehavior.FIRST_APPROVAL==approvalTemplate.getApprovalBehavior()) {
            throw new UnsupportedOperationException("ApprovalBehavior.FIRST_APPROVAL is not supported for SEQUENTIAL Approval Workflow!");
        }
    }

    private void validateRejectSetting(ApprovalTemplate approvalTemplate) {
        if(MultiInstanceType.PARALLEL==approvalTemplate.getMultiInstanceType()
            && approvalTemplate.isEnableRejectStep()) {
            throw new UnsupportedOperationException("Reject to Step is not supported for PARALLEL Approval Workflow!");
        }
    }
}
