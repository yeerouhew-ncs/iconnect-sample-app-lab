package com.ncs.iconnect.sample.lab.generated.approval.core.event;

import java.util.List;

public interface ApprovalEventDispatcher {

	  /**
	   * Dispatches the given event to any listeners that are registered.
	   * 
	   * @param event
	   *          event to dispatch.
	   */
	  void dispatchEvent(ApprovalEvent event);

    /**
     * Dispatches the given events to any listeners that are registered.
     *
     * @param events
     *          events to dispatch.
     */
    void dispatchEvents(List<ApprovalEvent> events);

    void addEventListener(ApprovalEventListener approvalEventListener);
}
