import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils, JhiAlertService } from 'ng-jhipster';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { CustomerRequest } from './customer-request.model';
import { CustomerRequestService } from './customer-request.service';
import { AccountService } from 'app/core/auth/account.service';
import { Approver, ApprovalRequest, ApprovalRequestService, TaskAction } from 'app/approval/approval-request';

@Component({
  selector: 'ic-customer-request-createorupdate',
  templateUrl: './customer-request-update.component.html'
})
export class CustomerRequestUpdateComponent implements OnInit, OnDestroy {
  customerRequest: CustomerRequest = new CustomerRequest();
  enableSubmitButton: boolean;
  enableSaveButton: boolean;
  currentAccount: any;
  private subscription: Subscription;
  private eventSubscriber: Subscription;
  formTypeKey = 'CustomerRequest';
  isSaving: boolean;
  constructor(
    private eventManager: JhiEventManager,
    private dataUtils: JhiDataUtils,
    private alertService: JhiAlertService,
    private customerRequestService: CustomerRequestService,
    private approvalRequestService: ApprovalRequestService,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      if (params['id']) {
        this.load(params['id']);
      } else {
        this.approvalRequestService.getApprovalTemplate(this.formTypeKey, '').subscribe(response => {
          const approvalTemplate: ApprovalTemplate = response.body;
          if (approvalTemplate && approvalTemplate.id) {
            this.customerRequest.approvalRequest.templateId = approvalTemplate.id;
            this.customerRequest.approvalRequest.approverSelection = approvalTemplate.approverSelection;
            this.initTemplateApprovers(approvalTemplate.id);
          } else {
            this.customerRequest.approvalRequest.approvers = this.getDefaultAprovers();
          }
        });
      }
      this.accountService.identity().subscribe(account => {
        this.currentAccount = this.copyAccount(account);
      });
    });

    this.registerApprovalRequestDeleted();
  }

  initTemplateApprovers(templateId: string): void {
    this.approvalRequestService.getApprovers(templateId).subscribe((approvers: Approver[]) => {
      if (approvers.length > 0) {
        const approversClone = approvers.map(function(approver): any {
          approver.id = null;
          return approver;
        });
        this.customerRequest.approvalRequest.approvers = approversClone;
      } else {
        this.customerRequest.approvalRequest.approvers = this.getDefaultAprovers();
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
    this.customerRequestService.find(id).subscribe((customerRequestResponse: HttpResponse<CustomerRequest>) => {
      this.customerRequest = customerRequestResponse.body;
      if (this.customerRequest.approvalRequest.approvers.length === 0) {
        this.approvalRequestService
          .getApprovers(this.formTypeKey)
          .subscribe((approvers: any) => (this.customerRequest.approvalRequest.approvers = approvers.data));
      }
      this.enableSubmitButton = this.shouldEnableSubmitButton(this.customerRequest.approvalRequest);
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

  shouldEnableSubmitButton(approvalRequest: ApprovalRequest): any {
    if (
      (approvalRequest.status === 'DRAFT' || approvalRequest.status === 'REQUEST_FOR_CHANGE') &&
      approvalRequest.initiator === this.currentAccount.subjectId
    ) {
      return true;
    }
    return false;
  }

  save(): void {
    if (!this.customerRequest.approvalRequest.approvers || this.customerRequest.approvalRequest.approvers.length === 0) {
      this.alertService.addAlert({ type: 'danger', msg: 'approval.approvalRequest.errorMsg.noApproversSelected' }, []);
      return;
    }

    if (!this.isAllApproverValid()) {
      this.alertService.addAlert({ type: 'danger', msg: 'approval.approvalRequest.errorMsg.invalidApprovers' }, []);
      return;
    }

    if (!this.customerRequest.id) {
      this.customerRequestService.create(this.customerRequest).subscribe((approvalRequestResponse: HttpResponse<CustomerRequest>) => {
        this.customerRequest = approvalRequestResponse.body;
      });
    } else {
      this.customerRequestService.update(this.customerRequest).subscribe((approvalRequestResponse: HttpResponse<CustomerRequest>) => {
        this.customerRequest = approvalRequestResponse.body;
      });
    }
  }

  isAllApproverValid(): any {
    let index = -1;
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const valid = this.customerRequest.approvalRequest.approvers.some(function(approver, i): any {
      if (!approver.approverId || approver.approverId === '') {
        index = i;
        return true;
      }
    });
    if (index < 0) {
      return true;
    }
    return false;
  }

  submitForApproval(): void {
    this.approvalRequestService
      .submitRequest(this.customerRequest.approvalRequest, new TaskAction())
      .subscribe((approvalRequestResponse: HttpResponse<ApprovalRequest>) => {
        this.customerRequest.approvalRequest = approvalRequestResponse.body;
        this.router.navigate(['/customer-request', this.customerRequest.id]);
      });
  }

  previousState(): void {
    window.history.back();
  }

  setFileData(event, entity, field, isImage): void {
    this.dataUtils.setFileData(event, entity, field, isImage);
  }

  byteSize(field): any {
    return this.dataUtils.byteSize(field);
  }
}
