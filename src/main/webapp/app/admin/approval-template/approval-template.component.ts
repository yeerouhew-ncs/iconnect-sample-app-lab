import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ApprovalTemplateService } from 'app/admin/approval-template/approval-template.service';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';

@Component({
  selector: 'ic-approval-template',
  templateUrl: './approval-template.component.html',
  styles: []
})
export class ApprovalTemplateComponent implements OnInit, OnDestroy {
  searchApprovalCriteria = new ApprovalTemplate();
  approvalTemplates: ApprovalTemplate[];
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    private approvalTemplateService: ApprovalTemplateService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = 'createdDt';
    });
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }

  ngOnInit(): void {
    this.load();
    this.registerChangeInApprovalTemplate();
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  load(): void {
    if (!this.searchApprovalCriteria.id) {
      this.searchApprovalCriteria.id = '';
    }
    if (!this.searchApprovalCriteria.requestTypeKey) {
      this.searchApprovalCriteria.requestTypeKey = '';
    }
    this.approvalTemplateService
      .query({
        id: this.searchApprovalCriteria.id,
        requestTypeKey: this.searchApprovalCriteria.requestTypeKey,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<any>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['/admin/ic-approvalTemplate'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.load();
  }

  search(): void {
    this.load();
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    return result;
  }

  trackId(index: number, item: ApprovalTemplate): any {
    return item.id;
  }

  reset(): void {
    this.searchApprovalCriteria = {
      id: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/ic-approvalTemplate',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
      }
    ]);
    this.load();
  }

  private onSuccess(data, headers): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.approvalTemplates = data;
  }

  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }

  registerChangeInApprovalTemplate(): void {
    this.eventSubscriber = this.eventManager.subscribe('approvalTemplateListModification', () => this.load());
  }
}
