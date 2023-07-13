import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  UserTokenService,
  UserTokenPopupService,
  UserTokenComponent,
  UserTokenDialogComponent,
  UserTokenPopupComponent,
  userTokenRoute,
  userTokenPopupRoute,
  UserTokenResolvePagingParams
} from 'app/admin/access-control/login-control';

const ENTITY_STATES = [...userTokenRoute, ...userTokenPopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [UserTokenComponent, UserTokenDialogComponent, UserTokenPopupComponent],
  entryComponents: [UserTokenComponent, UserTokenDialogComponent, UserTokenPopupComponent],
  providers: [UserTokenService, UserTokenPopupService, UserTokenResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcUserTokenModule {}
