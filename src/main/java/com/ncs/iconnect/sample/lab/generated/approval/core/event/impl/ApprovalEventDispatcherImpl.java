package com.ncs.iconnect.sample.lab.generated.approval.core.event.impl;

import org.springframework.stereotype.Service;

import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventDispatcher;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApprovalEventDispatcherImpl implements ApprovalEventDispatcher {

    private Set<ApprovalEventListener> approvalEventListeners = new HashSet<>();

    public ApprovalEventDispatcherImpl(ApprovalEventEmailListner approvalEventEmailListner) {
        this.approvalEventListeners.add(approvalEventEmailListner);
    }

	@Override
	public void dispatchEvent(ApprovalEvent event) {
	    this.approvalEventListeners.forEach(listener->listener.onEvent(event));
	}

    @Override
    public void dispatchEvents(List<ApprovalEvent> events) {
        events.forEach(this::dispatchEvent);
    }

    @Override
    public void addEventListener(ApprovalEventListener approvalEventListener) {
        this.approvalEventListeners.add(approvalEventListener);
    }

}
