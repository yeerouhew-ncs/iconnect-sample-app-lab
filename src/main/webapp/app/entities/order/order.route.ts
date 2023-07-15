import { Routes } from '@angular/router';

import { OrderComponent } from './order.component';

export const orderRoute: Routes = [
  {
    path: '',
    component: OrderComponent,
    data: {
      authorities: [],
      pageTitle: 'global.menu.entities.order'
    }
  }
];
