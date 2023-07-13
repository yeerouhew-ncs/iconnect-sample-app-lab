import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  RoleService,
  RolePopupService,
  RoleAssignPopupService,
  RoleComponent,
  RoleCreateDialogComponent,
  RolePopupComponent,
  RoleDeleteDialogComponent,
  RoleDeletePopupComponent,
  RoleEditComponent,
  RoleAssignUsersDialogComponent,
  RoleAssignUsersPopupComponent,
  RoleAssignFuncsDialogComponent,
  RoleAssignFuncsPopupComponent,
  RoleAssignGroupsDialogComponent,
  RoleAssignGroupsPopupComponent,
  roleRoute,
  rolePopupRoute,
  RoleResolvePagingParams
} from './';

const ENTITY_STATES = [...roleRoute, ...rolePopupRoute];

@NgModule({
  imports: [AutoCompleteModule, IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RoleComponent,
    RoleCreateDialogComponent,
    RolePopupComponent,
    RoleDeleteDialogComponent,
    RoleDeletePopupComponent,
    RoleEditComponent,
    RoleAssignUsersDialogComponent,
    RoleAssignUsersPopupComponent,
    RoleAssignFuncsDialogComponent,
    RoleAssignFuncsPopupComponent,
    RoleAssignGroupsDialogComponent,
    RoleAssignGroupsPopupComponent
  ],
  entryComponents: [
    RoleComponent,
    RoleCreateDialogComponent,
    RolePopupComponent,
    RoleDeleteDialogComponent,
    RoleDeletePopupComponent,
    RoleEditComponent,
    RoleAssignUsersDialogComponent,
    RoleAssignUsersPopupComponent,
    RoleAssignFuncsDialogComponent,
    RoleAssignFuncsPopupComponent,
    RoleAssignGroupsDialogComponent,
    RoleAssignGroupsPopupComponent
  ],
  providers: [RoleService, RolePopupService, RoleAssignPopupService, RoleResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcRoleModule {}
