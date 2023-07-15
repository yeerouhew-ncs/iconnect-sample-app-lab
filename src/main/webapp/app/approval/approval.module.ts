import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ApprovalRequestModule } from './approval-request/approval-request.module';
import { IconnectSampleAppMonolithGeneralApprovalModule } from './general-approval/general-approval.module';
import { NgIConnectModule } from 'app/ng-iconnect';
import { IconnectSampleAppLabCustomerRequestModule } from './customer-request/customer-request.module';
/* iConnect-needle-add-approval-module-import - iConnect will add approval modules imports here */

@NgModule({
  imports: [
    ApprovalRequestModule,
    NgIConnectModule,
    IconnectSampleAppMonolithGeneralApprovalModule,
    IconnectSampleAppLabCustomerRequestModule
    /* iConnect-needle-add-approval-module - iConnect will add approval modules here */
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  exports: [ApprovalRequestModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IconnectSampleAppMonolithApprovalModule {}
