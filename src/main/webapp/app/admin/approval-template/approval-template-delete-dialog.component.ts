import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ApprovalTemplateService } from 'app/admin/approval-template/approval-template.service';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { ApprovalTemplatePopupService } from 'app/admin/approval-template/approval-template-popup.service';

@Component({
  selector: 'ic-approval-template-delete-dialog',
  templateUrl: './approval-template-delete-dialog.component.html'
})
export class ApprovalTemplateDeleteDialogComponent {
  approvalTemplate: ApprovalTemplate;

  constructor(
    private approvalTemplateService: ApprovalTemplateService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: string): void {
    this.approvalTemplateService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'approvalTemplateListModification',
        content: 'Deleted a approvalTemplate.'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/ic-approvalTemplate/']);
    }, 0);
  }
}

@Component({
  selector: 'ic-approval-template-delete-popup',
  template: ''
})
export class ApprovalTemplateDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private approvalTemplatePopupService: ApprovalTemplatePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.approvalTemplatePopupService.open(ApprovalTemplateDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
