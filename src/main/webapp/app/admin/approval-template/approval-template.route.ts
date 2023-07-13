import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ApprovalTemplateDataComponent } from './approval-template-data.component';
import { ApprovalTemplateComponent } from 'app/admin/approval-template/approval-template.component';
import { ApprovalTemplatePopupComponent } from 'app/admin/approval-template/approval-template-dialog.component';
import { ApprovalTemplateDetailComponent } from 'app/admin/approval-template/approval-template-detail.component';
import { ApprovalTemplateDeletePopupComponent } from 'app/admin/approval-template/approval-template-delete-dialog.component';

@Injectable()
export class ApprovalTemplatesolvePagingParams implements Resolve<any> {
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

export const approvalTemplateRoute: Routes = [
  {
    path: '',
    component: ApprovalTemplateComponent,
    resolve: {
      pagingParams: ApprovalTemplatesolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'ic-approvalTemplateDataView/:id',
    component: ApprovalTemplateDataComponent,
    resolve: {
      pagingParams: ApprovalTemplatesolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.templateData.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'approvalTemplate-new',
    component: ApprovalTemplatePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id',
    component: ApprovalTemplateDetailComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.param.main'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const approvalTemplatePopupRoute: Routes = [
  {
    path: ':id/edit',
    component: ApprovalTemplatePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.param.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/delete',
    component: ApprovalTemplateDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'approvalTemplate.param.main'
    },
    canActivate: [UserRouteAccessService]
  }
];
