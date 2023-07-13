package com.ncs.iconnect.sample.lab.generated.approval.core.event.impl;

import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventType;

public class ApprovalRequetEvent implements ApprovalEvent {

	private ApprovalEventType eventType;
	private Long requestId;

	public ApprovalRequetEvent(ApprovalEventType eventType, Long requestId) {
		this.eventType = eventType;
		this.requestId = requestId;
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

	@Override
	public String toString() {
		return "ApprovalRequetEvent [eventType=" + eventType + ", requestId=" + requestId + "]";
	}

}
