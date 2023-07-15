import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';

import { OrderComponent } from './order.component';
import { orderRoute } from './order.route';

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild(orderRoute)],
  declarations: [OrderComponent],
  entryComponents: []
})
export class OrderModule {}
