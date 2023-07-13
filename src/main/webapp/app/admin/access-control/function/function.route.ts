import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { FunctionComponent } from './function.component';
import { FunctionPopupComponent } from './function-create-dialog.component';
import { FunctionDeletePopupComponent } from './function-delete-dialog.component';
import { FunctionEditComponent } from './function-edit.component';
import { FunctionAssignResourcesPopupComponent } from './function-assign-resources-dialog.component';
import { FunctionAssignRolesPopupComponent } from './function-assign-roles-dialog.component';

@Injectable()
export class FunctionResolvePagingParams implements Resolve<any> {
  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot): any {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'resourceId,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const functionRoute: Routes = [
  {
    path: 'ic-function',
    component: FunctionComponent,
    resolve: {
      pagingParams: FunctionResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-function/:id',
    component: FunctionEditComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const functionPopupRoute: Routes = [
  {
    path: 'ic-function-new',
    component: FunctionPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-function/:id/delete',
    component: FunctionDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-function/assign/resources/:applicationId/:resourceId',
    component: FunctionAssignResourcesPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-function/assign/roles/:applicationId/:resourceId',
    component: FunctionAssignRolesPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.function.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
