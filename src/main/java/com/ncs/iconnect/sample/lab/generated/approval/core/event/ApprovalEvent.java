package com.ncs.iconnect.sample.lab.generated.approval.core.event;

/**
 * Common interface for approval events
 *
 */
public interface ApprovalEvent {
	 ApprovalEventType getEventType();

	/**
	 * @return approval request id
	 */
	 Long getRequestId();
}