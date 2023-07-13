package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.RequestPermission;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;

/**
 * Handle Approval Request Permission Issue
 *
 */
public interface ApprovalRequestPermissionService {

	boolean hasPermission(String subjectId, ApprovalRequestEntity approvalRequestEntity, RequestPermission RequestPermission);

	/**
	 * Validate if given subject have  access to given request
	 * @param subjectId: ID of User
	 * @param approvalRequestEntity: Approval Request
	 * @param RequestPermission: Permission
	 * Throws com.ncs.iframe5.component.validator.exception.BusinessValidationException if user do not have permission
	 */
	void validatePermission(String subjectId, ApprovalRequestEntity approvalRequestEntity, RequestPermission RequestPermission);

	/**
	* Test if user is administrator
	* @return true if user is admin, false otherwise
	*/
    boolean isAdministrator();
}
