import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  UserService,
  UserPopupService,
  UserAssignPopupService,
  UserResetpwdPopupService,
  UserComponent,
  UserCreateDialogComponent,
  UserPopupComponent,
  UserEditComponent,
  UserAssignRolesDialogComponent,
  UserAssignRolesPopupComponent,
  UserAssignGroupsDialogComponent,
  UserAssignGroupsPopupComponent,
  UserResetpwdDialogComponent,
  UserResetpwdPopupComponent,
  userRoute,
  userPopupRoute,
  UserResolvePagingParams
} from './';

const ENTITY_STATES = [...userRoute, ...userPopupRoute];

@NgModule({
  imports: [AutoCompleteModule, IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserComponent,
    UserCreateDialogComponent,
    UserPopupComponent,
    UserEditComponent,
    UserAssignRolesDialogComponent,
    UserAssignRolesPopupComponent,
    UserAssignGroupsDialogComponent,
    UserAssignGroupsPopupComponent,
    UserResetpwdDialogComponent,
    UserResetpwdPopupComponent
  ],
  entryComponents: [
    UserComponent,
    UserCreateDialogComponent,
    UserPopupComponent,
    UserEditComponent,
    UserAssignRolesDialogComponent,
    UserAssignRolesPopupComponent,
    UserAssignGroupsDialogComponent,
    UserAssignGroupsPopupComponent,
    UserResetpwdDialogComponent,
    UserResetpwdPopupComponent
  ],
  providers: [UserService, UserPopupService, UserAssignPopupService, UserResetpwdPopupService, UserResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcUserModule {}
