import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Subject } from './user.model';
import { UserService } from './user.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-user',
  templateUrl: './user.component.html'
})
export class UserComponent implements OnInit, OnDestroy {
  currentAccount: any;
  users?: Subject[];
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
  statuses: any[];
  loginTypes: any[];

  constructor(
    private userService: UserService,
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
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
    this.searchCriteria = {
      firstName: '',
      lastName: '',
      loginName: '',
      email: '',
      effectiveDt: '',
      expiryDt: '',
      status: '',
      loginMechanism: ''
    };
    this.statuses = [
      { codeId: 'A', codeDesc: 'Active' },
      { codeId: 'S', codeDesc: 'Suspended' },
      { codeId: 'I', codeDesc: 'Inactive' },
      { codeId: 'R', codeDesc: 'Revoked' },
      { codeId: 'P', codeDesc: 'Pending' },
      { codeId: 'D', codeDesc: 'Deactived' }
    ];
    this.loginTypes = [
      { codeId: 'PASSWORD', codeDesc: 'Password Login' },
      { codeId: 'SINGPASS', codeDesc: 'Singpass Login' },
      { codeId: 'SINGPASS_EASY', codeDesc: 'Singpass With Easy Login' },
      { codeId: 'NETRUST', codeDesc: 'Netrust Login' },
      { codeId: 'KERBEROS', codeDesc: 'Kerberos Login' },
      { codeId: 'CONTAINER', codeDesc: 'Container Login' },
      { codeId: 'LDAP', codeDesc: 'LDAP Login' }
    ];
  }

  loadAll(): void {
    this.userService
      .query({
        firstName: this.searchCriteria.firstName,
        lastName: this.searchCriteria.lastName,
        loginName: this.searchCriteria.loginName,
        email: this.searchCriteria.email,
        effectiveDt: this.searchCriteria.effectiveDt,
        expiryDt: this.searchCriteria.expiryDt,
        status: this.searchCriteria.status,
        loginMechanism: this.searchCriteria.loginMechanism,
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
    this.router.navigate(['/admin/acm/ic-user'], {
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
    this.predicate = 'subjectId';
    this.reverse = false;
    this.loadAll();
  }

  clear(): void {
    this.searchCriteria = {
      firstName: '',
      lastName: '',
      loginName: '',
      email: '',
      effectiveDt: '',
      expiryDt: '',
      status: '',
      loginMechanism: ''
    };
    this.page = 0;
    this.router.navigate([
      '/admin/acm/ic-user',
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
    this.registerChangeInUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: Subject): any {
    return item.subjectId;
  }

  registerChangeInUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('userListModification', () => this.loadAll());
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'subjectId') {
      result.push('subjectId');
    }
    return result;
  }

  private onSuccess(data: any, headers: any): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.users = data;
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
