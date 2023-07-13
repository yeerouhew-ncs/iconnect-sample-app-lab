import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ApprovalRequestListComponent } from './approval-request-list.component';
import { ApprovalRequestDeletePopupComponent } from './approval-request-delete-dialog.component';
@Injectable()
export class ApprovalRequestResolvePagingParams implements Resolve<any> {
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

export const approvalRequestRoute: Routes = [
  {
    path: 'approval-request',
    component: ApprovalRequestListComponent,
    resolve: {
      pagingParams: ApprovalRequestResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approval.approvalRequest.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const approvalRequestPopupRoute: Routes = [
  {
    path: 'approval-request/:id/delete',
    component: ApprovalRequestDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approval.approvalRequest.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
