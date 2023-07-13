import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ParamService } from './param.service';
import { SearchParamModel, ParamModel } from './param.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ic-param',
  templateUrl: './param.component.html',
  styles: []
})
export class ParamComponent implements OnInit, OnDestroy {
  searchParamCriteria = new SearchParamModel();
  appList: any[];
  params: any[];
  codes: any[];
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
  effectiveDateAsStrDp: any;
  expireDateAsStrDp: any;

  constructor(
    private paramService: ParamService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }

  ngOnInit(): void {
    this.load();
    this.getAppList();
    this.registerChangeInParams();
  }

  getAppList(): void {
    this.paramService.getAppList().subscribe((data: any[]) => {
      this.appList = data;
    });
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ParamModel): any {
    return item.id;
  }

  private convertDate(searchParam: string): string {
    if (searchParam) {
      return this.datePipe.transform(searchParam, 'MM/dd/yyyy');
    }
    return '';
  }

  load(): void {
    if (!this.searchParamCriteria.appId) {
      this.searchParamCriteria.appId = '';
    }
    if (!this.searchParamCriteria.paramKey) {
      this.searchParamCriteria.paramKey = '';
    }
    if (!this.searchParamCriteria.paramDesc) {
      this.searchParamCriteria.paramDesc = '';
    }

    this.paramService
      .query({
        effectiveDateAsStr: this.convertDate(this.searchParamCriteria.effectiveDateAsStr),
        expireDateAsStr: this.convertDate(this.searchParamCriteria.expireDateAsStr),
        appId: this.searchParamCriteria.appId,
        paramKey: this.searchParamCriteria.paramKey,
        paramDesc: this.searchParamCriteria.paramDesc,
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
    this.router.navigate(['admin/ic-param'], {
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
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    return result;
  }

  reset(): void {
    this.searchParamCriteria = {};
  }

  private onSuccess(data, headers): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    data.forEach(param => {
      this.paramService.getParamTypes().forEach(paramType => {
        if (param.paramType === paramType.codeId) {
          param.paramTypeDesc = paramType.label;
        }
      });
    });
    this.params = data;
  }

  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }

  registerChangeInParams(): void {
    this.eventSubscriber = this.eventManager.subscribe('paramListModification', () => this.load());
  }
}
