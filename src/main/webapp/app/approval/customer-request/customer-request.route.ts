import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { CustomerRequestDetailComponent } from './customer-request-detail.component';
import { CustomerRequestUpdateComponent } from './customer-request-update.component';
@Injectable()
export class CustomerRequestResolvePagingParams implements Resolve<any> {
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

export const customerRequestRoute: Routes = [
  {
    path: 'customer-request-createorupdate',
    component: CustomerRequestUpdateComponent,
    resolve: {
      pagingParams: CustomerRequestResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'iconnectSampleAppLabApp.customerRequest.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'customer-request-createorupdate/:id',
    component: CustomerRequestUpdateComponent,
    resolve: {
      pagingParams: CustomerRequestResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'iconnectSampleAppLabApp.customerRequest.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'customer-request/:id',
    component: CustomerRequestDetailComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'iconnectSampleAppLabApp.customerRequest.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
