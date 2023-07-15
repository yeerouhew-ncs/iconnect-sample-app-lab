import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { ApprovalRequestModule } from 'app/approval/approval-request/approval-request.module';

import {
  CustomerRequestService,
  CustomerRequestDetailComponent,
  CustomerRequestUpdateComponent,
  customerRequestRoute,
  CustomerRequestResolvePagingParams
} from '.';

import { FieldsetModule } from 'primeng/fieldset';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextareaModule } from 'primeng/inputtextarea';

const ENTITY_STATES = [...customerRequestRoute];

@NgModule({
  imports: [
    IconnectSampleAppLabSharedModule,
    FieldsetModule,
    FileUploadModule,
    InputTextareaModule,
    ApprovalRequestModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [CustomerRequestDetailComponent, CustomerRequestUpdateComponent],
  entryComponents: [CustomerRequestUpdateComponent],
  providers: [CustomerRequestService, CustomerRequestResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IconnectSampleAppLabCustomerRequestModule {}
