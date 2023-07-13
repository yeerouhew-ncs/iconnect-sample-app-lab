import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { RoleComponent } from './role.component';
import { RolePopupComponent } from './role-create-dialog.component';
import { RoleDeletePopupComponent } from './role-delete-dialog.component';
import { RoleEditComponent } from './role-edit.component';
import { RoleAssignUsersPopupComponent } from './role-assign-users-dialog.component';
import { RoleAssignFuncsPopupComponent } from './role-assign-funcs-dialog.component';
import { RoleAssignGroupsPopupComponent } from './role-assign-groups-dialog.component';

@Injectable()
export class RoleResolvePagingParams implements Resolve<any> {
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

export const roleRoute: Routes = [
  {
    path: 'ic-role',
    component: RoleComponent,
    resolve: {
      pagingParams: RoleResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-role/:id',
    component: RoleEditComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const rolePopupRoute: Routes = [
  {
    path: 'ic-role-new',
    component: RolePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-role/:id/delete',
    component: RoleDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-role/assign/users/:id',
    component: RoleAssignUsersPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-role/assign/funcs/:id',
    component: RoleAssignFuncsPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-role/assign/groups/:id',
    component: RoleAssignGroupsPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.role.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
