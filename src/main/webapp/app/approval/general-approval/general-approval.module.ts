import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { ApprovalRequestModule } from '../approval-request/approval-request.module';
import { ApprovalTemplateService } from 'app/admin/approval-template';

import {
  GeneralApprovalService,
  GeneralApprovalDetailComponent,
  GeneralApprovalUpdateComponent,
  generalApprovalRoute,
  GeneralApprovalResolvePagingParams
} from '.';

import { FieldsetModule } from 'primeng/fieldset';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextareaModule } from 'primeng/inputtextarea';

const ENTITY_STATES = [...generalApprovalRoute];

@NgModule({
  imports: [
    IconnectSampleAppLabSharedModule,
    FieldsetModule,
    FileUploadModule,
    InputTextareaModule,
    ApprovalRequestModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [GeneralApprovalDetailComponent, GeneralApprovalUpdateComponent],
  entryComponents: [GeneralApprovalUpdateComponent],
  providers: [GeneralApprovalService, GeneralApprovalResolvePagingParams, ApprovalTemplateService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IconnectSampleAppMonolithGeneralApprovalModule {}
