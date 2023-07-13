package com.ncs.iconnect.sample.lab.generated.approval.core.domain;

import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalTables;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.io.Serializable;

@Entity
@Table(name = ApprovalTables.APPROVAL_HISTORY)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApprovalHistoryItem implements Comparable<ApprovalHistoryItem>,Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false, insertable = false, updatable = false)
    private ApprovalRequestEntity request;

    @NotNull
    @Column(name = "ACT_USER_ID")
    private String actUserId;

    @NotNull
    @Column(name = "ACTION_NAME")
    private String actionName;

    @Column(name = "COMMENT")
    @Lob
    private String comment;

    @NotNull
    @Column(name = "OLD_REQ_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus oldRequestStatus;

    @NotNull
    @Column(name = "NEW_REQ_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus newRequestStatus;

    @NotNull
    @Column(name = "ACTION_DATE")
    private LocalDate actionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalRequestEntity getRequest() {
        return request;
    }

    public void setRequest(ApprovalRequestEntity request) {
        this.request = request;
    }

    public String getActUserId() {
        return actUserId;
    }

    public void setActUserId(String actUserId) {
        this.actUserId = actUserId;
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

    @Override
    public int compareTo(ApprovalHistoryItem other) {
        if (other == null || other.getId() == null) {
            return 1;
        }

        if (this.getId() == null) return 0;

        return Long.compare(this.getId(), other.getId());
    }
}
