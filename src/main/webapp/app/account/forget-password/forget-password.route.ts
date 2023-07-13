import { Route } from '@angular/router';
import { ForgetPasswordComponent } from './forget-password.component';

export const forgetPasswordRoute: Route = {
  path: 'password/request',
  component: ForgetPasswordComponent,
  data: {
    authorities: [],
    pageTitle: 'password.forget.title'
  }
};
