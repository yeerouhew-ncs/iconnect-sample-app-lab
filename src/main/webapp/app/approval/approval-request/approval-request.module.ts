import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  ApprovalRequestService,
  ApprovalRequestPopupService,
  ApprovalRequestHeaderComponent,
  ApprovalRequestApproversComponent,
  ApprovalRequestAttachmentComponent,
  ApprovalRequestReviewActionComponent,
  ApprovalRequestListComponent,
  ApprovalRequestDeletePopupComponent,
  ApprovalRequestDeleteDialogComponent,
  ApprovalRequestHistoryComponent,
  approvalRequestRoute,
  approvalRequestPopupRoute,
  ApprovalRequestResolvePagingParams
} from '.';

import { FieldsetModule } from 'primeng/fieldset';
// import { GrowlModule } from 'primeng/growl';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextareaModule } from 'primeng/inputtextarea';
// import { DataListModule } from 'primeng/datalist';
import { DialogModule } from 'primeng/dialog';
import { AutoCompleteModule } from 'primeng/autocomplete';

const ENTITY_STATES = [...approvalRequestRoute, ...approvalRequestPopupRoute];

@NgModule({
  imports: [
    IconnectSampleAppLabSharedModule,
    FieldsetModule,
    // GrowlModule,
    FileUploadModule,
    InputTextareaModule,
    // DataListModule,
    DialogModule,
    AutoCompleteModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    ApprovalRequestHeaderComponent,
    ApprovalRequestApproversComponent,
    ApprovalRequestAttachmentComponent,
    ApprovalRequestReviewActionComponent,
    ApprovalRequestListComponent,
    ApprovalRequestDeletePopupComponent,
    ApprovalRequestDeleteDialogComponent,
    ApprovalRequestHistoryComponent
  ],
  entryComponents: [ApprovalRequestHeaderComponent, ApprovalRequestDeleteDialogComponent],
  providers: [ApprovalRequestService, ApprovalRequestPopupService, ApprovalRequestResolvePagingParams],
  exports: [
    ApprovalRequestHeaderComponent,
    ApprovalRequestApproversComponent,
    ApprovalRequestAttachmentComponent,
    ApprovalRequestReviewActionComponent,
    ApprovalRequestListComponent,
    ApprovalRequestDeletePopupComponent,
    ApprovalRequestDeleteDialogComponent,
    ApprovalRequestHistoryComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApprovalRequestModule {}
