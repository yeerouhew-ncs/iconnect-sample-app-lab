import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { JhiLanguageService } from 'ng-jhipster';

import {
  ApprovalTemplateComponent,
  ApprovalTemplateDataComponent,
  ApprovalTemplateDialogComponent,
  ApprovalTemplateDetailComponent,
  ApprovalTemplateDeletePopupComponent,
  ApprovalTemplatePopupComponent,
  ApprovalTemplateDeleteDialogComponent,
  approvalTemplateRoute,
  approvalTemplatePopupRoute,
  ApprovalTemplateService,
  ApprovalTemplateDataService,
  ApprovalTemplatePopupService,
  ApprovalTemplatesolvePagingParams
} from 'app/admin/approval-template';

const ENTITY_STATES = [...approvalTemplateRoute, ...approvalTemplatePopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, DialogModule, AutoCompleteModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ApprovalTemplateComponent,
    ApprovalTemplateDataComponent,
    ApprovalTemplateDialogComponent,
    ApprovalTemplateDetailComponent,
    ApprovalTemplatePopupComponent,
    ApprovalTemplateDeletePopupComponent,
    ApprovalTemplateDeleteDialogComponent
  ],
  entryComponents: [
    ApprovalTemplateComponent,
    ApprovalTemplateDataComponent,
    ApprovalTemplateDialogComponent,
    ApprovalTemplateDetailComponent,
    ApprovalTemplatePopupComponent,
    ApprovalTemplateDeletePopupComponent,
    ApprovalTemplateDeleteDialogComponent
  ],
  providers: [
    ApprovalTemplateService,
    ApprovalTemplateDataService,
    ApprovalTemplatePopupService,
    ApprovalTemplatesolvePagingParams,
    { provide: JhiLanguageService, useClass: JhiLanguageService }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcApprovalTemplateModule {}
