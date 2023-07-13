import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ApplicationComponent } from './application.component';
import { ApplicationPopupComponent } from './application-dialog.component';

@Injectable()
export class ApplicationResolvePagingParams implements Resolve<any> {
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

export const applicationRoute: Routes = [
  {
    path: 'ic-application',
    component: ApplicationComponent,
    resolve: {
      pagingParams: ApplicationResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const applicationPopupRoute: Routes = [
  {
    path: 'ic-application-new',
    component: ApplicationPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-application/:id',
    component: ApplicationPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
