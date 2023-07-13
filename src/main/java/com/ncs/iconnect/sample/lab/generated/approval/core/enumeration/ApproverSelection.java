package com.ncs.iconnect.sample.lab.generated.approval.core.enumeration;

public enum ApproverSelection {
    //Both approval steps, and approver id are fixed by process owner, and applicant can not change
    FIXED,
    //Approval step fixed by process owner, applicant can change approver id, but can not change approval step
    FIXED_STEP,
    //Both approval steps, and approver id are not fixed, applicant can change at runtime
    FLEXIBLE
}
