import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ApprovalTemplateService } from 'app/admin/approval-template/approval-template.service';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { ApprovalTemplatePopupService } from 'app/admin/approval-template/approval-template-popup.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'ic-approval-template-dialog',
  templateUrl: './approval-template-dialog.component.html',
  styles: [
    `
      .errMsgs {
        margin-left: auto;
      }
    `
  ]
})
export class ApprovalTemplateDialogComponent implements OnInit, OnDestroy {
  approvalTemplate: ApprovalTemplate;
  errorBody: any;

  constructor(
    private approvalTemplateService: ApprovalTemplateService,
    private alertService: JhiAlertService,
    private eventManager: JhiEventManager,
    public activeModal: NgbActiveModal,
    private router: Router
  ) {
    this.approvalTemplate = new ApprovalTemplate();
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  save(): void {
    if (this.approvalTemplate.id) {
      this.subscribeToSaveResponse(this.approvalTemplateService.update(this.approvalTemplate));
    } else {
      this.subscribeToSaveResponse(this.approvalTemplateService.create(this.approvalTemplate));
    }
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  private subscribeToSaveResponse(result: Observable<ApprovalTemplate>): void {
    result.subscribe(
      (res: ApprovalTemplate) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: ApprovalTemplate): void {
    this.eventManager.broadcast({ name: 'approvalTemplateListModification', content: 'OK' });
    this.activeModal.dismiss(result);
    this.backToPrevious();
  }

  private onSaveError(error): void {
    try {
      this.errorBody = error.body;
    } catch (exception) {
      error.message = error.text();
    }
    this.onError(error);
  }
  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }

  backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-approval-template-popup',
  template: ''
})
export class ApprovalTemplatePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private approvalTemplatePopupService: ApprovalTemplatePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.approvalTemplatePopupService.open(ApprovalTemplateDialogComponent as Component, params['id']);
      } else {
        this.approvalTemplatePopupService.open(ApprovalTemplateDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
