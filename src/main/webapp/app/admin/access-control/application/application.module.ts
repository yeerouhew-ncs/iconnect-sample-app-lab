import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  ApplicationService,
  ApplicationPopupService,
  ApplicationComponent,
  ApplicationDialogComponent,
  ApplicationPopupComponent,
  applicationRoute,
  applicationPopupRoute,
  ApplicationResolvePagingParams
} from './';

const ENTITY_STATES = [...applicationRoute, ...applicationPopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ApplicationComponent, ApplicationDialogComponent, ApplicationPopupComponent],
  entryComponents: [ApplicationComponent, ApplicationDialogComponent, ApplicationPopupComponent],
  providers: [ApplicationService, ApplicationPopupService, ApplicationResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcApplicationModule {}
