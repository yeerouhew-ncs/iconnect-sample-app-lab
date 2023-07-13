import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserTokenComponent } from 'app/admin/access-control/login-control/user-token.component';
import { UserTokenPopupComponent } from 'app/admin/access-control/login-control/user-token-dialog.component';

@Injectable()
export class UserTokenResolvePagingParams implements Resolve<any> {
  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot): any {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'appCode,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const userTokenRoute: Routes = [
  {
    path: 'ic-user-token',
    component: UserTokenComponent,
    resolve: {
      pagingParams: UserTokenResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.userToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userTokenPopupRoute: Routes = [
  {
    path: 'ic-user-token-new',
    component: UserTokenPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.userToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-user-token/:id',
    component: UserTokenPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.userToken.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
