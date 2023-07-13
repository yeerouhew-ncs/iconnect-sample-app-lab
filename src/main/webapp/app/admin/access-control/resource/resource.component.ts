import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ResourceService } from './resource.service';
import { Application } from '../application/application.model';

@Component({
  selector: 'ic-resource',
  templateUrl: './resource.component.html'
})
export class ResourceComponent implements OnInit, OnDestroy {
  resources: Resource[] = [];
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
  resourceTypes: any[];

  constructor(
    private resourceService: ResourceService,
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
      resourceName: '',
      resourceType: '',
      resourcePath: ''
    };
    this.resourceTypes = [
      { codeId: 'URI', codeDesc: 'URI' },
      { codeId: 'WEBURI', codeDesc: 'WEBURI' }
    ];
  }

  loadAll(): void {
    this.resourceService
      .query({
        applicationId: this.searchCriteria.applicationId,
        resourceName: this.searchCriteria.resourceName,
        resourceType: this.searchCriteria.resourceType,
        resourcePath: this.searchCriteria.resourcePath,
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
    this.router.navigate(['/admin/acm/ic-resource'], {
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
      resourceName: '',
      resourceType: '',
      resourcePath: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/acm/ic-resource',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();

    this.resourceService.findAllApplication().subscribe(
      (res: HttpResponse<any>) => {
        this.applications = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );

    this.registerChangeInResources();
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

  registerChangeInResources(): void {
    this.eventSubscriber = this.eventManager.subscribe('resourceListModification', () => this.loadAll());
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
    this.resources = data;
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
