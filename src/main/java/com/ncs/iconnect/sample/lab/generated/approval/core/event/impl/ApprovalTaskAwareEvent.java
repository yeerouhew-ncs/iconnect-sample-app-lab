package com.ncs.iconnect.sample.lab.generated.approval.core.event.impl;

import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventType;

public class ApprovalTaskAwareEvent implements ApprovalEvent {
	private ApprovalEventType eventType;
	private Long requestId;
	private Long approverTaskId;

	public ApprovalTaskAwareEvent(ApprovalEventType eventType, Long requestId, Long approverTaskId) {
		this.eventType = eventType;
		this.requestId = requestId;
		this.approverTaskId = approverTaskId;
	}

	public ApprovalEventType getEventType() {
		return eventType;
	}

	public void setEventType(ApprovalEventType eventType) {
		this.eventType = eventType;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getApproverTaskId() {
		return approverTaskId;
	}

	public void setApproverTaskId(Long approverTaskId) {
		this.approverTaskId = approverTaskId;
	}

	@Override
	public String toString() {
		return "ApprovalTaskAwareEvent [eventType=" + eventType + ", requestId=" + requestId + ", approverTaskId=" + approverTaskId
				+ "]";
	}
}
