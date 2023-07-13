import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { GeneralApprovalDetailComponent } from './general-approval-detail.component';
import { GeneralApprovalUpdateComponent } from './general-approval-update.component';

@Injectable()
export class GeneralApprovalResolvePagingParams implements Resolve<any> {
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

export const generalApprovalRoute: Routes = [
  {
    path: 'general-approval-createorupdate',
    component: GeneralApprovalUpdateComponent,
    resolve: {
      pagingParams: GeneralApprovalResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'generalApproval.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'general-approval-createorupdate/:id',
    component: GeneralApprovalUpdateComponent,
    resolve: {
      pagingParams: GeneralApprovalResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'generalApproval.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'general-approval/:id',
    component: GeneralApprovalDetailComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'generalApproval.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
