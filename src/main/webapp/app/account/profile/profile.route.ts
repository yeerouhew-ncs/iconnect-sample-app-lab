import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { MaintainProfileComponent } from 'app/account/profile/maintain-profile.component';
import { EditProfileComponent } from 'app/account/profile/edit-profile.component';

export const profileRoute: Routes = [
  {
    path: 'profile',
    component: MaintainProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'profile.main'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'profile/edit',
    component: EditProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'profile.main'
    },
    canActivate: [UserRouteAccessService]
  }
];
