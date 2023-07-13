import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CodeTypeComponent } from './code-type.component';
import { CodeTypeMaintainComponent } from './code-type-maintain.component';
import { CodeTypeDeletePopupComponent } from './code-type-delete-dialog.component';

@Injectable()
export class CodeTypeResolvePagingParams implements Resolve<any> {
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

export const codeTypeRoute: Routes = [
  {
    path: '',
    component: CodeTypeComponent,
    resolve: {
      pagingParams: CodeTypeResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-codetype-new',
    component: CodeTypeMaintainComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id',
    component: CodeTypeMaintainComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const codeTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CodeTypeDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.codeType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
