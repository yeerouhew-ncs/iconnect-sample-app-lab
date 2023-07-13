import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserComponent } from './user.component';
import { UserPopupComponent } from './user-create-dialog.component';
import { UserEditComponent } from './user-edit.component';
import { UserAssignGroupsPopupComponent } from './user-assign-groups-dialog.component';
import { UserAssignRolesPopupComponent } from './user-assign-roles-dialog.component';
import { UserResetpwdPopupComponent } from './user-resetpwd-dialog.component';

@Injectable()
export class UserResolvePagingParams implements Resolve<any> {
  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot): any {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'subjectId,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const userRoute: Routes = [
  {
    path: 'ic-user',
    component: UserComponent,
    resolve: {
      pagingParams: UserResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-user/:id',
    component: UserEditComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userPopupRoute: Routes = [
  {
    path: 'ic-user-new',
    component: UserPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-user/assign/roles/:id',
    component: UserAssignRolesPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-user/assign/groups/:id',
    component: UserAssignGroupsPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-user-resetpwd/:id/reset/:userid',
    component: UserResetpwdPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.user.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
