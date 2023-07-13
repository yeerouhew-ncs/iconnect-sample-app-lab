import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  CodeTypeDeleteDialogComponent,
  CodeTypeDeletePopupComponent,
  CodeTypeComponent,
  CodeTypeMaintainComponent,
  codeTypeRoute,
  codeTypePopupRoute,
  CodeTypeService,
  CodeTypePopupService,
  CodeTypeResolvePagingParams
} from './';
import { JhiLanguageService } from 'ng-jhipster';

const ENTITY_STATES = [...codeTypeRoute, ...codeTypePopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CodeTypeComponent, CodeTypeMaintainComponent, CodeTypeDeleteDialogComponent, CodeTypeDeletePopupComponent],
  entryComponents: [CodeTypeComponent, CodeTypeMaintainComponent, CodeTypeDeleteDialogComponent, CodeTypeDeletePopupComponent],
  providers: [
    CodeTypeService,
    CodeTypePopupService,
    CodeTypeResolvePagingParams,
    { provide: JhiLanguageService, useClass: JhiLanguageService }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CodeTypeModule {}
