import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ParamComponent } from './param.component';
import { ParamPopupComponent } from './param-dialog.component';
import { ParamDetailComponent } from './param-detail.component';
import { ParamDeletePopupComponent } from './param-delete-dialog.component';

@Injectable()
export class ParamResolvePagingParams implements Resolve<any> {
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

export const paramRoute: Routes = [
  {
    path: '',
    component: ParamComponent,
    resolve: {
      pagingParams: ParamResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paramAdmin.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':appId/:paramKey',
    component: ParamDetailComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paramAdmin.param.main'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const paramPopupRoute: Routes = [
  {
    path: 'param-new',
    component: ParamPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paramAdmin.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':appId/:paramKey/edit',
    component: ParamPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paramAdmin.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':appId/:paramKey/delete',
    component: ParamDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paramAdmin.param.main'
    },
    canActivate: [UserRouteAccessService]
  }
];
