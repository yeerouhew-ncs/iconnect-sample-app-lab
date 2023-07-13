import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { GroupComponent } from './group.component';
import { GroupPopupComponent } from './group-create-dialog.component';
import { GroupEditComponent } from './group-edit.component';
import { GroupAssignUsersPopupComponent } from './group-assign-users-dialog.component';
import { GroupAssignRolesPopupComponent } from './group-assign-roles-dialog.component';
import { GroupDeletePopupComponent } from './group-delete-dialog.component';

@Injectable()
export class GroupResolvePagingParams implements Resolve<any> {
  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot): any {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'groupId,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const groupRoute: Routes = [
  {
    path: 'ic-group',
    component: GroupComponent,
    resolve: {
      pagingParams: GroupResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-group/:id',
    component: GroupEditComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const groupPopupRoute: Routes = [
  {
    path: 'ic-group-new',
    component: GroupPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-group/:id/delete',
    component: GroupDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-group/assign/users/:id',
    component: GroupAssignUsersPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-group/assign/roles/:id',
    component: GroupAssignRolesPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.group.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
