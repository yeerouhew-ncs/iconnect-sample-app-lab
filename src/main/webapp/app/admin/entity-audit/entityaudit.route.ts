import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { AuditLogComponent } from './audit-log/audit-log.component';

@Injectable()
export class EntityAuditResolvePagingParams implements Resolve<any> {
  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot): any {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const entityAuditRoute: Routes = [
  {
    path: '',
    component: AuditLogComponent,
    resolve: {
      pagingParams: EntityAuditResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'entityAudit.log.main'
    }
  }
];
