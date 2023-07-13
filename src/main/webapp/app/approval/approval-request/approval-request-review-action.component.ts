import { Component, OnInit, AfterViewChecked, ChangeDetectorRef, Input } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { ApprovalRequest, TaskAction, ApprovalRequestService } from '.';

@Component({
  selector: 'ic-approval-request-review-actions',
  templateUrl: './approval-request-review-action.component.html'
})
export class ApprovalRequestReviewActionComponent implements AfterViewChecked, OnInit {
  @Input() approvalRequest: ApprovalRequest;
  @Input() currentAccount: any;
  enableDeleteButton: boolean;
  enableEditButton: boolean;
  enableSubmitButton: boolean;
  enableCancelButton: boolean;
  enableReviewButtons: boolean;
  enableCompleteButton: boolean;
  isSaving: boolean;
  displayReviewDialog: boolean;
  selectedReviewAction: string;
  reviewCommentRequiredMessage: string;
  reivewComment: string;
  constructor(
    private approvalRequestService: ApprovalRequestService,
    private cdRef: ChangeDetectorRef,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.resetReviewDialog();
  }

  ngAfterViewChecked(): void {
    this.isSaving = false;
    this.setButtonEnableStatus();
    this.cdRef.detectChanges();
  }

  rejectRequest(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.rejectRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  rollbackToApplicant(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.sendBackRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  prepareReviewDialog(action): void {
    this.selectedReviewAction = action;
    this.displayReviewDialog = true;
  }

  confirmReviewAction(action): void {
    if ('Reject' === action || '' === action || 'Rollback to Applicant' === action) {
      if (this.reivewComment === '') {
        this.reviewCommentRequiredMessage = 'Comment required for action [' + action + ']!';
        return;
      }
    }

    if ('Submit' === action) {
      this.submitForApproval();
    } else if ('Approve' === action) {
      this.approveRequest();
    } else if ('Reject' === action) {
      this.rejectRequest();
    } else if ('Rollback to Applicant' === action) {
      this.rollbackToApplicant();
    } else if ('Cancel' === action) {
      this.cancelRequest();
    } else if ('Complete' === action) {
      this.completeRequest();
    }
    this.resetReviewDialog();
  }

  submitForApproval(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.submitRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  cancelRequest(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.cancelRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  boreadCastApprovalRequestChagneEvent(): void {
    this.eventManager.broadcast({
      name: 'approvalRequestModification',
      content: 'Approval Request State Changed'
    });
  }

  setButtonEnableStatus(): void {
    this.enableDeleteButton = this.shouldEnableDeleteButton(this.approvalRequest);
    this.enableEditButton = this.shouldEnableEditButton(this.approvalRequest);
    this.enableReviewButtons = this.shouldEnableReviewButton(this.approvalRequest);
    this.enableSubmitButton = this.shouldEnableSubmitButton(this.approvalRequest);
    this.enableCancelButton = this.shouldEnableCancelButton(this.approvalRequest);
    this.enableCompleteButton = this.shouldEnableCompleteButton(this.approvalRequest);
  }

  shouldEnableEditButton(approvalRequest: ApprovalRequest): boolean {
    if (approvalRequest.status === 'DRAFT' && approvalRequest.initiator === this.currentAccount.subjectId) {
      return true;
    }
    return false;
  }

  shouldEnableDeleteButton(approvalRequest: ApprovalRequest): boolean {
    if (approvalRequest.status === 'DRAFT' && approvalRequest.initiator === this.currentAccount.subjectId) {
      return true;
    }
    return false;
  }

  shouldEnableCancelButton(approvalRequest: ApprovalRequest): boolean {
    if (approvalRequest.status === 'PENDING_APPROVAL' && approvalRequest.initiator === this.currentAccount.subjectId) {
      return true;
    }
    return false;
  }

  shouldEnableReviewButton(approvalRequest: ApprovalRequest): boolean {
    if (approvalRequest.status !== 'PENDING_APPROVAL') {
      return false;
    }

    for (const approver of approvalRequest.approvers) {
      if (approver.approvalStatus === 'PENDING_APPROVAL' && approver.approverId === this.currentAccount.subjectId) {
        return true;
      }
    }
    return false;
  }

  shouldEnableSubmitButton(approvalRequest: ApprovalRequest): boolean {
    if (
      (approvalRequest.status === 'DRAFT' || approvalRequest.status === 'REQUEST_FOR_CHANGE') &&
      approvalRequest.initiator === this.currentAccount.subjectId
    ) {
      return true;
    }
    return false;
  }

  shouldEnableCompleteButton(approvalRequest: ApprovalRequest): boolean {
    if (approvalRequest.initiator === this.currentAccount.subjectId) {
      if (
        approvalRequest.status === 'APPROVED' ||
        approvalRequest.status === 'REJECTED' ||
        approvalRequest.status === 'CANCELLED' ||
        approvalRequest.status === 'REQUEST_FOR_CHANGE'
      ) {
        return true;
      }
    }

    return false;
  }

  completeRequest(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.completeRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  approveRequest(): void {
    this.isSaving = true;
    const taskAction = this.getReviewTaskAction();
    this.approvalRequestService.approveRequest(this.approvalRequest, taskAction).subscribe(() => {
      this.boreadCastApprovalRequestChagneEvent();
      this.isSaving = false;
    });
  }

  getReviewTaskAction(): any {
    const taskAction: TaskAction = new TaskAction();
    taskAction.approverInstanceId = this.getReviewTaskId(this.approvalRequest);
    taskAction.comment = this.reivewComment;
    return taskAction;
  }

  getReviewTaskId(approvalRequest: ApprovalRequest): any {
    if (
      approvalRequest.status !== 'CANCELLED' &&
      approvalRequest.status !== 'PENDING_APPROVAL' &&
      approvalRequest.status !== 'APPROVED' &&
      approvalRequest.status !== 'REJECTED' &&
      approvalRequest.status !== 'REQUEST_FOR_CHANGE'
    ) {
      return -1;
    }

    if (
      (approvalRequest.status === 'CANCELLED' ||
        approvalRequest.status === 'APPROVED' ||
        approvalRequest.status === 'REJECTED' ||
        approvalRequest.status === 'REQUEST_FOR_CHANGE') &&
      approvalRequest.initiator === this.currentAccount.subjectId
    ) {
      return approvalRequest.id;
    }

    for (const approver of approvalRequest.approvers) {
      if (approver.approvalStatus === 'PENDING_APPROVAL' && approver.approverId === this.currentAccount.subjectId) {
        return approver.id;
      }
    }

    return -1;
  }

  hideReviewDialog(): void {
    this.resetReviewDialog();
  }

  resetReviewDialog(): void {
    this.displayReviewDialog = false;
    this.selectedReviewAction = '';
    this.reivewComment = '';
    this.reviewCommentRequiredMessage = '';
  }
}
