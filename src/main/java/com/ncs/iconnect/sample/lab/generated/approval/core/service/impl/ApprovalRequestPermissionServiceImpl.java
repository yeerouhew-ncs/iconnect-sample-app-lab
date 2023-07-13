package com.ncs.iconnect.sample.lab.generated.approval.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.RequestPermission;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestPermissionService;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;
import com.ncs.iconnect.sample.lab.generated.security.PermissionValidationException;

@Service
public class ApprovalRequestPermissionServiceImpl implements ApprovalRequestPermissionService {

    private static final String ROLE_APP_ADMIN = "DEF-role-appadmin";

    private final LoginContextHelper loginContextHelper;

    public ApprovalRequestPermissionServiceImpl(LoginContextHelper loginContextHelper) {
        this.loginContextHelper = loginContextHelper;
    }

    @Override
    public boolean hasPermission(String subjectId, ApprovalRequestEntity approvalRequestEntity,
                                 RequestPermission requestPermission) {
        if (RequestPermission.READ == requestPermission) {
            return this.hasReadPermission(subjectId, approvalRequestEntity);
        } else if (RequestPermission.UPDATE == requestPermission) {
            return this.hasUpdatePermission(subjectId, approvalRequestEntity);
        } else if (RequestPermission.REVIEW == requestPermission) {
            return this.hasReviewPermission(subjectId, approvalRequestEntity);
        } else if (RequestPermission.CANCEL == requestPermission) {
            return this.hasCancelPermission(subjectId, approvalRequestEntity);
        } else if (RequestPermission.DELETE == requestPermission) {
            return this.hasDeletePermission(subjectId, approvalRequestEntity);
        }
        return false;
    }

    @Override
    public void validatePermission(String subjectId, ApprovalRequestEntity approvalRequestEntity,
                                   RequestPermission requestPermission) {
        if (!this.hasPermission(subjectId, approvalRequestEntity, requestPermission)) {
            String cause = "Permission " + requestPermission + "  not granted for user " + subjectId + " for request "
                + approvalRequestEntity.getKey();
            throw new PermissionValidationException(cause);
        }
    }

    protected boolean hasUpdatePermission(String subjectId, ApprovalRequestEntity approvalRequestEntity) {

        return isInitiator(subjectId, approvalRequestEntity) || isAdministrator();
    }

    protected boolean hasCancelPermission(String subjectId, ApprovalRequestEntity approvalRequestEntity) {
        return isInitiator(subjectId, approvalRequestEntity) || isAdministrator();
    }

    protected boolean hasDeletePermission(String subjectId, ApprovalRequestEntity approvalRequestEntity) {
        return isDraft(approvalRequestEntity)
            && (isInitiator(subjectId, approvalRequestEntity) || isAdministrator());
    }

    protected boolean isDraft(ApprovalRequestEntity approvalRequestEntity) {
        return ApprovalStatus.DRAFT == approvalRequestEntity.getStatus();
    }

    protected boolean hasReviewPermission(String subjectId, ApprovalRequestEntity approvalRequestEntity) {
       return approvalRequestEntity.getApprovers().stream().anyMatch(approver ->
            ApprovalStatus.PENDING_APPROVAL == approver.getApprovalStatus() && StringUtils.equals(subjectId, approver.getApproverId())
        );
    }

    protected boolean hasReadPermission(String subjectId, ApprovalRequestEntity approvalRequestEntity) {

        if (isInitiator(subjectId, approvalRequestEntity)) {
            return true;
        }

        if (isAssignedApprover(subjectId, approvalRequestEntity)) {
            return true;
        }

        if (isAdministrator()) {
            return true;
        }

        return false;
    }

    protected boolean isInitiator(String subjectId, ApprovalRequestEntity approvalRequestEntity) {
        return StringUtils.equals(subjectId, approvalRequestEntity.getInitiator());
    }

    protected boolean isAssignedApprover(String subjectId, ApprovalRequestEntity approvalRequestEntity) {
        return approvalRequestEntity.getApprovers().stream().anyMatch(
            approver ->(approver.getApprovalStatus()!=null) && StringUtils.equals(subjectId, approver.getApproverId())
        );
    }

    public boolean isAdministrator() {
        return this.loginContextHelper.isLoginUserHasRole(ROLE_APP_ADMIN);
    }
}
