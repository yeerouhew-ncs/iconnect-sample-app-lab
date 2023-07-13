import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { JhiLanguageService } from 'ng-jhipster';

import {
  ParamComponent,
  ParamDialogComponent,
  ParamDetailComponent,
  ParamDeletePopupComponent,
  ParamPopupComponent,
  ParamDeleteDialogComponent,
  paramRoute,
  paramPopupRoute,
  ParamService,
  ParamPopupService,
  ParamResolvePagingParams
} from './';

const ENTITY_STATES = [...paramRoute, ...paramPopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ParamComponent,
    ParamDialogComponent,
    ParamDetailComponent,
    ParamPopupComponent,
    ParamDeletePopupComponent,
    ParamDeleteDialogComponent
  ],
  entryComponents: [
    ParamComponent,
    ParamDialogComponent,
    ParamDetailComponent,
    ParamPopupComponent,
    ParamDeletePopupComponent,
    ParamDeleteDialogComponent
  ],
  providers: [ParamService, ParamPopupService, ParamResolvePagingParams, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParamModule {}
