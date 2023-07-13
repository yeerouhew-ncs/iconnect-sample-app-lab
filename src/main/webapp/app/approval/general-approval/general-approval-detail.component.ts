import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { GeneralApproval } from './general-approval.model';
import { GeneralApprovalService } from './general-approval.service';
import { AccountService } from 'app/core/auth/account.service';
import { ApprovalRequest } from '../approval-request';

@Component({
  selector: 'ic-general-approval-detail',
  templateUrl: './general-approval-detail.component.html'
})
export class GeneralApprovalDetailComponent implements OnInit, OnDestroy {
  generalApproval: GeneralApproval;
  subscription: Subscription;
  eventSubscriber: Subscription;
  currentAccount: any;
  enableEditButton: boolean;
  constructor(
    private eventManager: JhiEventManager,
    private generalApprovalService: GeneralApprovalService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscription = this.route.params.subscribe(params => {
      if (params['id']) {
        this.load(params['id']);
      }
    });

    this.accountService.identity().subscribe(account => {
      this.currentAccount = this.copyAccount(account);
    });

    this.registerChangeInApprovalRequest();
    this.registerApprovalRequestDeleted();
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInApprovalRequest(): void {
    this.eventSubscriber = this.eventManager.subscribe('approvalRequestModification', () => this.load(this.generalApproval.id));
  }

  registerApprovalRequestDeleted(): void {
    this.eventSubscriber = this.eventManager.subscribe('approvalRequestDeleted', () => {
      // setTimeout is a workaround to fix navigation issue after activeModel dismiss
      setTimeout(() => {
        this.router.navigate(['/approval-request']);
      }, 1);
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

  load(id): any {
    if (!id) {
      return;
    }
    this.generalApprovalService.find(id).subscribe((generalApprovalResponse: HttpResponse<GeneralApproval>) => {
      this.generalApproval = generalApprovalResponse.body;
      this.enableEditButton = this.shouldEnableEditButton(this.generalApproval.approvalRequest);
    });
  }

  shouldEnableEditButton(approvalRequest: ApprovalRequest): boolean {
    if (
      (approvalRequest.status === 'DRAFT' || approvalRequest.status === 'REQUEST_FOR_CHANGE') &&
      approvalRequest.initiator === this.currentAccount.subjectId
    ) {
      return true;
    }
    return false;
  }

  editRequest(): void {
    this.router.navigate(['/general-approval-createorupdate', this.generalApproval.id]);
  }

  previousState(): void {
    window.history.back();
  }
}
