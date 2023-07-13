import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CodeManageComponent } from './code-manage.component';
import { CodeDeletePopupComponent } from './code-delete-dialog.component';

@Injectable()
export class CodeResolvePagingParams implements Resolve<any> {
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

export const codeRoute: Routes = [
  {
    path: ':codeTypePk',
    component: CodeManageComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const codePopupRoute: Routes = [
  {
    path: ':codeTypePk/:codeId/delete',
    component: CodeDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'codeAdmin.code.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
