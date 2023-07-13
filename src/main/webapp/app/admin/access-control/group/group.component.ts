import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';
import { Group } from './group.model';
import { GroupService } from './group.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { PaginationConfig } from '../../../blocks/config/uib-pagination.config';

@Component({
  selector: 'ic-group',
  templateUrl: './group.component.html'
})
export class GroupComponent implements OnInit, OnDestroy {
  currentAccount: any;
  groups: Group[] = [];
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

  constructor(
    private groupService: GroupService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
    this.searchCriteria = {
      groupId: '',
      groupName: ''
    };
  }

  loadAll(): void {
    this.groupService
      .query({
        groupId: this.searchCriteria.groupId,
        groupName: this.searchCriteria.groupName,
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
    this.router.navigate(['admin/acm/ic-group'], {
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
    this.predicate = 'groupId';
    this.reverse = false;
    this.loadAll();
  }

  clear(): void {
    this.searchCriteria = {
      groupId: '',
      groupName: ''
    };
    this.page = 0;
    this.router.navigate([
      'admin/acm/ic-group',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }
  ngOnInit(): void {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: Group): any {
    return item.groupId;
  }
  registerChangeInGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('groupListModification', () => this.loadAll());
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'groupId') {
      result.push('groupId');
    }
    return result;
  }

  private onSuccess(data: any, headers: any): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.groups = data;
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
