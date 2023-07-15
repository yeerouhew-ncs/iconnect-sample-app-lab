import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule)
      }
    ])
  ]
})
export class IconnectSampleAppLabEntityModule {}
