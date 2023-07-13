import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ResourceComponent } from './resource.component';
import { ResourcePopupComponent } from './resource-create-dialog.component';
import { ResourceDeletePopupComponent } from './resource-delete-dialog.component';
import { ResourceEditComponent } from './resource-edit.component';
import { ResourceAssignFunctionsPopupComponent } from './resource-assign-functions-dialog.component';

@Injectable()
export class ResourceResolvePagingParams implements Resolve<any> {
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

export const resourceRoute: Routes = [
  {
    path: 'ic-resource',
    component: ResourceComponent,
    resolve: {
      pagingParams: ResourceResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.resource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-resource/:id',
    component: ResourceEditComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.resource.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const resourcePopupRoute: Routes = [
  {
    path: 'ic-resource-new',
    component: ResourcePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.resource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-resource/:id/delete',
    component: ResourceDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.resource.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-resource/assign/functions/:applicationId/:resourceId',
    component: ResourceAssignFunctionsPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'acmAdmin.resource.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
