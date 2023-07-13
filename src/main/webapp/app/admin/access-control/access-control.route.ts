import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { applicationRoute, functionRoute, groupRoute, resourceRoute, roleRoute, userRoute, userPopupRoute } from './';

export const ACM_ROUTES = [
  ...applicationRoute,
  ...functionRoute,
  ...groupRoute,
  ...resourceRoute,
  ...roleRoute,
  ...userPopupRoute,
  ...userRoute
];

export const acmRoutes: Routes = [
  {
    path: '',
    canActivate: [UserRouteAccessService],
    children: ACM_ROUTES
  }
];
