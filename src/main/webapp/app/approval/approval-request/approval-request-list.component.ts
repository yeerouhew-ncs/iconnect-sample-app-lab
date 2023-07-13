import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ApprovalRequest } from './approval-request.model';
import { ApprovalRequestService } from './approval-request.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-approval-request-list',
  templateUrl: './approval-request-list.component.html'
})
export class ApprovalRequestListComponent implements OnInit, OnDestroy {
  currentAccount: any;
  approvalRequests: ApprovalRequest[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  @Input() extendedRequestTypeKey: string;
  @Input() extendedRequestPath: string;
  constructor(
    private approvalRequestService: ApprovalRequestService,
    private parseLinks: JhiParseLinks,
    private jhiAlertService: JhiAlertService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll(): void {
    if (!this.extendedRequestTypeKey) {
      this.extendedRequestTypeKey = 'All';
    }
    this.approvalRequestService
      .query({
        requestTypeKey: this.extendedRequestTypeKey,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ApprovalRequest[]>) => this.onSuccess(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit(): void {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
  }

  ngOnDestroy(): void {}

  trackId(index: number, item: ApprovalRequest): any {
    return item.id;
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(data, headers): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.approvalRequests = data;
  }
  private onError(error): void {
    this.jhiAlertService.error(error.message, null, null);
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['/approval-request'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  /**
   * Resolve actual request REST path
   * If parameter is passed, return given parameter 'extendedRequestPath'
   * If parameter 'extendedRequestPath' is not set, try to resolve from Request Key, e.g. 'GeneralApproval-1' path resolve to 'general-approval'
   * @param requestKey: key of Request
   */
  resolveFormPath(requestKey: string): any {
    if (this.extendedRequestPath) {
      return this.extendedRequestPath;
    } else {
      const typeKey = this.resolveFormType(requestKey);

      // Separate camel case with '-',  e.g. 'GeneralApproval' to '-General-Approval'
      const restPath = typeKey.replace(/([A-Z])/g, function($1): any {
        return '-' + $1.toLowerCase();
      });
      // Remove leading '-'
      return restPath.toLowerCase().substring(1, restPath.length);
    }
  }

  resolveFormType(requestKey: string): any {
    const index = requestKey.indexOf('-');
    return requestKey.substring(0, index);
  }

  resolveFormId(requestKey: string): any {
    const index = requestKey.indexOf('-');
    return requestKey.substring(index + 1);
  }
}
