import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ApprovalRequest } from './approval-request.model';
import { ApprovalRequestPopupService } from './approval-request-popup.service';
import { ApprovalRequestService } from './approval-request.service';

@Component({
  selector: 'ic-approval-request-delete-dialog',
  templateUrl: './approval-request-delete-dialog.component.html'
})
export class ApprovalRequestDeleteDialogComponent {
  approvalRequest: ApprovalRequest;

  constructor(
    private approvalRequestService: ApprovalRequestService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    window.history.back();
  }

  confirmDelete(id: number): void {
    this.approvalRequestService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'approvalRequestDeleted',
        content: id
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  private backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['/approval-request']);
    }, 0);
  }
}

@Component({
  selector: 'ic-approval-request-delete-popup',
  template: ''
})
export class ApprovalRequestDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private approvalRequestPopupService: ApprovalRequestPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.approvalRequestPopupService.open(ApprovalRequestDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
