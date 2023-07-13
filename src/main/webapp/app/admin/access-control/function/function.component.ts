import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FunctionService } from './function.service';
import { Application } from '../application/application.model';

@Component({
  selector: 'ic-function',
  templateUrl: './function.component.html'
})
export class FunctionComponent implements OnInit, OnDestroy {
  functions: Resource[] = [];
  error: any;
  success: any;
  eventSubscriber?: Subscription;
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
  applications: Application[] = [];

  constructor(
    private functionService: FunctionService,
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
      this.predicate = data['pagingParams'].predicate;
    });
    this.searchCriteria = {
      applicationId: '',
      functionName: ''
    };
  }

  loadAll(): void {
    this.functionService
      .query({
        applicationId: this.searchCriteria.applicationId,
        functionName: this.searchCriteria.functionName,
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
    this.router.navigate(['/admin/acm/ic-function'], {
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
    this.predicate = 'resourceId';
    this.reverse = false;
    this.loadAll();
  }

  clear(): void {
    this.searchCriteria = {
      applicationId: '',
      functionName: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/acm/ic-function',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();

    this.functionService.findAllApplication().subscribe(
      (res: HttpResponse<any>) => {
        this.applications = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );

    this.registerChangeInFunctions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: Resource): any {
    return item.id;
  }

  trackApplicationById(index: number, item: Application): any {
    return item.id;
  }

  registerChangeInFunctions(): void {
    this.eventSubscriber = this.eventManager.subscribe('functionListModification', () => this.loadAll());
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'resourceId') {
      result.push('resourceId');
    }
    return result;
  }

  private onSuccess(data: any, headers: any): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.functions = data;
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
