package com.ncs.iconnect.sample.lab.generated.approval.core.service.dto;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.io.Serializable;

public class ApprovalHistoryItemDTO implements Comparable<ApprovalHistoryItemDTO>,Serializable {

    protected static final long serialVersionUID = 1L;
    
    private Long id;

    private String actUserId;

    private String actUserDisplayName;

    private String actionName;

    private String comment;

    private ApprovalStatus oldRequestStatus;

    private ApprovalStatus newRequestStatus;

    private LocalDate actionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActUserId() {
        return actUserId;
    }

    public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
    }

    public String getActUserDisplayName() {
        return actUserDisplayName;
    }

    public void setActUserDisplayName(String actUserDisplayName) {
        this.actUserDisplayName = actUserDisplayName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ApprovalStatus getOldRequestStatus() {
        return oldRequestStatus;
    }

    public void setOldRequestStatus(ApprovalStatus oldRequestStatus) {
        this.oldRequestStatus = oldRequestStatus;
    }

    public ApprovalStatus getNewRequestStatus() {
        return newRequestStatus;
    }

    public void setNewRequestStatus(ApprovalStatus newRequestStatus) {
        this.newRequestStatus = newRequestStatus;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public int compareTo(ApprovalHistoryItemDTO other) {
        if (other == null || other.getId()==null) {
            return 1;
        }

        if(this.getId()==null) return 0;

        return Long.compare(this.getId(), other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        ApprovalHistoryItemDTO that = (ApprovalHistoryItemDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(actUserId, that.actUserId) &&
            Objects.equals(actionName, that.actionName) &&
            Objects.equals(comment, that.comment) &&
            oldRequestStatus == that.oldRequestStatus &&
            newRequestStatus == that.newRequestStatus &&
            Objects.equals(actionDate, that.actionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actUserId, actionName, comment, oldRequestStatus, newRequestStatus, actionDate);
    }
}
