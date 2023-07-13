import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AuditLogService } from './audit-log.service';
import { saveAs } from 'file-saver';
import { DatePipe } from '@angular/common';
import { SearchLogModel } from './audit-log.model';

@Component({
  selector: 'ic-auditlog',
  templateUrl: './audit-log.component.html',
  styles: [
    `
      th > span {
        font-size: 12px;
      }
    `
  ]
})
export class AuditLogComponent implements OnInit {
  searchAuditLogCriteria = new SearchLogModel();
  auditLogs: any[];
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
  fromDateAsStr: any;
  toDateAsStr: any;
  revisionTypeList = [
    { label: 'Add', value: 'ADD' },
    { label: 'Modify', value: 'MOD' },
    { label: 'Delete', value: 'DEL' }
  ];

  constructor(
    private auditLogService: AuditLogService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private datePipe: DatePipe,
    private router: Router
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
  }

  private convertDate(searchParam: string): string {
    if (searchParam) {
      return this.datePipe.transform(searchParam, 'MM/dd/yyyy');
    }
    return '';
  }

  load(): void {
    this.setNull(this.searchAuditLogCriteria);
    this.auditLogService
      .query({
        fromDateAsStr: this.convertDate(this.searchAuditLogCriteria.fromDateAsStr),
        toDateAsStr: this.convertDate(this.searchAuditLogCriteria.toDateAsStr),
        revision: this.searchAuditLogCriteria.revision,
        userId: this.searchAuditLogCriteria.userId,
        revisionType: this.searchAuditLogCriteria.revisionType,
        funcName: this.searchAuditLogCriteria.funcName,
        tableName: this.searchAuditLogCriteria.tableName,
        businessKey: this.searchAuditLogCriteria.businessKey,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<any>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  sort(): any {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    return result;
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['admin/ic-entityaudit'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
      }
    });
    this.load();
  }

  search(): void {
    this.load();
  }

  reset(): void {
    this.searchAuditLogCriteria = {};
  }

  exportToCSV(): void {
    this.auditLogService
      .exportToCSV({
        fromDateAsStr: this.convertDate(this.searchAuditLogCriteria.fromDateAsStr),
        toDateAsStr: this.convertDate(this.searchAuditLogCriteria.toDateAsStr),
        revision: this.searchAuditLogCriteria.revision,
        userId: this.searchAuditLogCriteria.userId,
        revisionType: this.searchAuditLogCriteria.revisionType,
        funcName: this.searchAuditLogCriteria.funcName,
        tableName: this.searchAuditLogCriteria.tableName,
        businessKey: this.searchAuditLogCriteria.businessKey
      })
      .subscribe((res: HttpResponse<string>) => {
        saveAs(
          new Blob([res.body], { type: 'text/csv;charset=utf-8' }),
          decodeURI(res.headers.get('content-disposition').substring(22) || 'download')
        );
      });
  }

  private onSuccess(data, headers): void {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    data.forEach(auditLog => {
      this.revisionTypeList.forEach(revisionType => {
        if (auditLog.revisionType === revisionType.value) {
          auditLog.revisionTypeDesc = revisionType.label;
        }
      });
    });
    this.auditLogs = data;
  }

  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }

  private setNull(entity: SearchLogModel): void {
    if (!entity.revision) {
      entity.revision = '';
    }
    if (!entity.userId) {
      entity.userId = '';
    }
    if (!entity.revisionType) {
      entity.revisionType = '';
    }
    if (!entity.funcName) {
      entity.funcName = '';
    }
    if (!entity.tableName) {
      entity.tableName = '';
    }
    if (!entity.businessKey) {
      entity.businessKey = '';
    }
  }
}
