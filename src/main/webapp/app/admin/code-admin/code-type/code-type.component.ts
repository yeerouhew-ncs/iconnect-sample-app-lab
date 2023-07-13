import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CodeType } from './code-type.model';
import { CodeTypeService } from './code-type.service';

@Component({
  selector: 'ic-code-type',
  templateUrl: './code-type.component.html'
})
export class CodeTypeComponent implements OnInit, OnDestroy {
  currentAccount: any;
  codeTypes: CodeType[];
  appList: any[];
  error: any;
  success: any;
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
  searchCriteria: any;

  constructor(
    private codeTypeService: CodeTypeService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.searchCriteria = {
      appId: '',
      codeType: '',
      codeTypeDesc: ''
    };
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
  }

  loadAll(): void {
    this.codeTypeService
      .query({
        appId: this.searchCriteria.appId,
        codeType: this.searchCriteria.codeType,
        codeTypeDesc: this.searchCriteria.codeTypeDesc,
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
    this.router.navigate(['/admin/ic-codetype'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  search(): void {
    this.links = {
      last: 0
    };
    this.page = 0;
    this.predicate = 'id';
    this.reverse = false;
    this.loadAll();
  }

  clear(): void {
    this.searchCriteria = {
      appId: '',
      codeType: '',
      codeTypeDesc: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/ic-codetype',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.getAppList();
    this.registerChangeInCodeTypes();
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  getAppList(): void {
    this.codeTypeService.getAppList().subscribe((data: HttpResponse<any>) => {
      this.appList = data.body;
    });
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
    this.codeTypes = data;
  }

  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }

  registerChangeInCodeTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('codeTypeListModification', () => this.loadAll());
  }
}
