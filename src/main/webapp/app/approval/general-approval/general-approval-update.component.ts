import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ApprovalTemplateService } from '../../admin/approval-template/approval-template.service';
import { GeneralApproval } from './general-approval.model';
import { GeneralApprovalService } from './general-approval.service';
import { ApprovalTemplate } from '../../admin/approval-template/approval-template.model';
import { Approver, ApprovalRequest, ApprovalRequestService, TaskAction } from 'app/approval/approval-request';

@Component({
  selector: 'ic-general-approval-createorupdate',
  templateUrl: './general-approval-update.component.html'
})
export class GeneralApprovalUpdateComponent implements OnInit, OnDestroy {
  generalApproval: GeneralApproval = new GeneralApproval();
  enableSubmitButton: boolean;
  enableSaveButton: boolean;
  currentAccount: any;
  private subscription: Subscription;
  private eventSubscriber: Subscription;
  formTypeKey = 'GeneralApproval';
  isSaving: boolean;
  approvalTemplates: ApprovalTemplate[];
  selectedApprovalTemplate: ApprovalTemplate;
  constructor(
    private eventManager: JhiEventManager,
    private alertService: JhiAlertService,
    private generalApprovalService: GeneralApprovalService,
    private approvalRequestService: ApprovalRequestService,
    private route: ActivatedRoute,
    private router: Router,
    private approvalTemplateService: ApprovalTemplateService
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      if (params['id']) {
        this.load(params['id']);
      }
    });
    this.registerApprovalRequestDeleted();
    this.approvalTemplateService.query({ requestTypeKey: 'GeneralApproval' }).subscribe(data => {
      this.approvalTemplates = data.body;
    });
  }

  initTemplateApprovers(templateId: string): void {
    this.approvalRequestService.getApprovers(templateId).subscribe((approvers: Approver[]) => {
      if (approvers.length > 0) {
        const approversClone = approvers.map(function(approver): any {
          approver.id = null;
          return approver;
        });
        this.generalApproval.approvalRequest.approvers = approversClone;
      } else {
        this.generalApproval.approvalRequest.approvers = this.getDefaultAprovers();
      }
    });
  }

  getDefaultAprovers(): any {
    const defaultApprover: Approver = {
      approverId: '',
      approverDisplayName: 'Please select...',
      approverTitle: 'Approver 1',
      approverSeq: 1,
      approvalStatus: 'DRAFT'
    };

    return [defaultApprover];
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerApprovalRequestDeleted(): void {
    this.eventSubscriber = this.eventManager.subscribe('approvalRequestDeleted', () => {
      // setTimeout is a workaround to fix navigation issue after activeModel dismiss
      setTimeout(() => {
        this.router.navigate(['/approval-request']);
      }, 1);
    });
  }

  load(id): void {
    this.generalApprovalService.find(id).subscribe((generalApprovalResponse: HttpResponse<GeneralApproval>) => {
      this.generalApproval = generalApprovalResponse.body;
      if (this.generalApproval.approvalRequest.approvers.length === 0) {
        this.approvalRequestService
          .getApprovers(this.formTypeKey)
          .subscribe((approvers: any) => (this.generalApproval.approvalRequest.approvers = approvers.data));
      }
      this.enableSubmitButton = this.shouldEnableSubmitButton(this.generalApproval.approvalRequest);
    });
  }

  copyAccount(account): any {
    return {
      email: account.email,
      firstName: account.firstName,
      lastName: account.lastName,
      login: account.username,
      loginMethod: account.authMethod,
      subjectId: account.subjectId
    };
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

  save(): void {
    if (!this.generalApproval.approvalRequest.approvers || this.generalApproval.approvalRequest.approvers.length === 0) {
      this.alertService.addAlert({ type: 'danger', msg: 'approval.approvalRequest.errorMsg.noApproversSelected' }, []);
      return;
    }

    if (!this.isAllApproverValid()) {
      this.alertService.addAlert({ type: 'danger', msg: 'approval.approvalRequest.errorMsg.invalidApprovers' }, []);
      return;
    }

    this.isSaving = true;
    if (!this.generalApproval.id) {
      this.generalApprovalService.create(this.generalApproval).subscribe((approvalRequestResponse: HttpResponse<GeneralApproval>) => {
        this.generalApproval = approvalRequestResponse.body;
        this.isSaving = false;
      });
    } else {
      this.generalApprovalService.update(this.generalApproval).subscribe((approvalRequestResponse: HttpResponse<GeneralApproval>) => {
        this.generalApproval = approvalRequestResponse.body;
        this.isSaving = false;
      });
    }
  }

  isAllApproverValid(): boolean {
    let index = -1;
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const valid = this.generalApproval.approvalRequest.approvers.some(function(approver, i): boolean {
      if (!approver.approverId || approver.approverId === '') {
        index = i;
        return true;
      } else {
        return false;
      }
    });
    if (index < 0) {
      return true;
    }
    return false;
  }

  submitForApproval(): void {
    this.isSaving = true;
    this.approvalRequestService
      .submitRequest(this.generalApproval.approvalRequest, new TaskAction())
      .subscribe((approvalRequestResponse: HttpResponse<ApprovalRequest>) => {
        this.generalApproval.approvalRequest = approvalRequestResponse.body;
        this.router.navigate(['/general-approval', this.generalApproval.id]);
        this.isSaving = false;
      });
  }

  previousState(): void {
    window.history.back();
  }

  onSelectTemplateKey(): any {
    this.approvalTemplateService.find(this.generalApproval.templateKey).subscribe(response => {
      const templateKey = response;
      if (templateKey && templateKey.id) {
        this.generalApproval.approvalRequest.templateId = templateKey.id;
        this.generalApproval.approvalRequest.approverSelection = templateKey.approverSelection;
        this.initTemplateApprovers(templateKey.id);
      } else {
        this.generalApproval.approvalRequest.approvers = this.getDefaultAprovers();
      }
    });
  }
}
