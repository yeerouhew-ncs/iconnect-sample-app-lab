import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { UserToken } from 'app/admin/access-control/login-control/user-token.model';
import { UserTokenService } from 'app/admin/access-control/login-control/user-token.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-user-token-login',
  templateUrl: './user-token.component.html'
})
export class UserTokenComponent implements OnInit, OnDestroy {
  currentAccount: any;
  userTokens: UserToken[] | undefined;
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
    private userTokenService: UserTokenService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = 'desc';
      this.predicate = 'createdDate';
    });
    this.searchCriteria = {
      loginId: ''
    };
  }

  loadAll(): void {
    this.userTokenService
      .query({
        loginId: this.searchCriteria.loginId,
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
    this.router.navigate(['/admin/acm/ic-user-token'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
      }
    });
    this.loadAll();
  }

  search(): void {
    this.links = {
      last: 0
    };
    this.page = 0;
    this.predicate = 'createdDate';
    this.reverse = false;
    this.loadAll();
  }

  clear(): void {
    this.searchCriteria = {
      loginId: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/acm/ic-user-token',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUserTokens();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: UserToken): any {
    return item.id;
  }

  registerChangeInUserTokens(): void {
    this.eventSubscriber = this.eventManager.subscribe('userTokenListModification', () => this.loadAll());
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.predicate !== 'createdDate') {
      result.push('createdDate');
    }
    return result;
  }

  private onSuccess(data: any, headers: any): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.userTokens = data;
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
