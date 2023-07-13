import { Route } from '@angular/router';

import { PasswordResetInitComponent } from './password-reset-init.component';

export const passwordResetInitRoute: Route = {
  path: 'reset/request/:username/:isFirstLogin',
  component: PasswordResetInitComponent,
  data: {
    authorities: [],
    pageTitle: 'password.menu.account.pwd'
  }
};
