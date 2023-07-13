package com.ncs.iconnect.sample.lab.generated.approval.core.enumeration;

/**
 * Defines all Approval Status
 */
public enum ApprovalStatus {
    //Initial State
    DRAFT,
    //Not assigned, e.g. change from other state to unassigned due to rollback
    UNASSIGNED,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    REQUEST_FOR_CHANGE,
    CANCELLED,
    COMPLETED
}
