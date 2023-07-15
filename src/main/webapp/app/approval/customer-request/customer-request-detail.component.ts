import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { CustomerRequest } from './customer-request.model';
import { CustomerRequestService } from './customer-request.service';
import { AccountService } from 'app/core/auth/account.service';
import { ApprovalRequest } from 'app/approval/approval-request';

@Component({
  selector: 'ic-customer-request-detail',
  templateUrl: './customer-request-detail.component.html'
})
export class CustomerRequestDetailComponent implements OnInit, OnDestroy {
  customerRequest: CustomerRequest;
  subscription: Subscription;
  eventSubscriber: Subscription;
  currentAccount: any;
  enableEditButton: boolean;
  constructor(
    private eventManager: JhiEventManager,
    private customerRequestService: CustomerRequestService,
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
    this.eventSubscriber = this.eventManager.subscribe('approvalRequestModification', () => this.load(this.customerRequest.id));
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

  load(id): void {
    if (!id) {
      return;
    }
    this.customerRequestService.find(id).subscribe((customerRequestResponse: HttpResponse<CustomerRequest>) => {
      this.customerRequest = customerRequestResponse.body;
      this.enableEditButton = this.shouldEnableEditButton(this.customerRequest.approvalRequest);
    });
  }

  shouldEnableEditButton(approvalRequest: ApprovalRequest): any {
    if (
      (approvalRequest.status === 'DRAFT' || approvalRequest.status === 'REQUEST_FOR_CHANGE') &&
      approvalRequest.initiator === this.currentAccount.subjectId
    ) {
      return true;
    }
    return false;
  }

  editRequest(): void {
    this.router.navigate(['/customer-request-createorupdate', this.customerRequest.id]);
  }

  previousState(): void {
    window.history.back();
  }
}
