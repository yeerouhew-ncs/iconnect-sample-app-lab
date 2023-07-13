package com.ncs.iconnect.sample.lab.generated.approval.core.event;

import static org.junit.jupiter.api.Assertions.*;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.TaskType;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalEventDispatcherImpl;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalRequetEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalEventEmailListner;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalTaskAwareEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ApprovalEventDispatcherTest {

    private ApprovalEventDispatcher approvalEventDispatcher;

    private ApprovalEventEmailListner approvalEventEmailListner = Mockito.mock(ApprovalEventEmailListner.class);

    @BeforeEach
    public void init() {
        approvalEventDispatcher = new ApprovalEventDispatcherImpl(approvalEventEmailListner);
    }

    @Test
    public void shouldDispatchEvent() {
        ApprovalEvent event = new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, 1L);
        approvalEventDispatcher.dispatchEvent(event);
        verify(approvalEventEmailListner, times(1)).onEvent(event);
    }

    @Test
    public void shouldDispatchEvents() {
        ApprovalEvent event1 = new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, 1L);
        ApprovalEvent event2 = new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, 2L);

        approvalEventDispatcher.dispatchEvents(new ArrayList<>(Arrays.asList(event1, event2)));
        verify(approvalEventEmailListner, times(2)).onEvent(any());
        ApprovalRequetEvent approvalRequetEvent = new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, 1L);
        approvalRequetEvent.setEventType(ApprovalEventType.REQUEST_SUBMITTED);
        approvalRequetEvent.setRequestId(1L);
        assertNotNull(approvalRequetEvent.getEventType());
        assertNotNull(approvalRequetEvent.getRequestId());
        approvalRequetEvent.toString();
        assertNotNull(TaskType.Review);
        assertNotNull(TaskType.Submit);

        ApprovalTaskAwareEvent approvalEvent = new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_APPROVED,1L,2L);
        approvalEvent.setEventType(ApprovalEventType.APPROVER_ASSIGNED);
        approvalEvent.setRequestId(3L);
        approvalEvent.setApproverTaskId(4L);
        assertNotNull(approvalEvent.getEventType());
        assertNotNull(approvalEvent.getRequestId());
        assertNotNull(approvalEvent.getApproverTaskId());
        approvalEvent.toString();
    }


    @Test
    public void shouldDispatchEventForNewListener() {
        ApprovalEventListener newApprovalEventListener = Mockito.mock(ApprovalEventListener.class);
        approvalEventDispatcher.addEventListener(newApprovalEventListener);

        ApprovalEvent event = new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, 1L);
        approvalEventDispatcher.dispatchEvent(event);
        verify(approvalEventEmailListner, times(1)).onEvent(event);
        verify(newApprovalEventListener, times(1)).onEvent(event);
    }
}
