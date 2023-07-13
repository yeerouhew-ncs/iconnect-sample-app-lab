import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  GroupService,
  GroupPopupService,
  GroupAssignPopupService,
  GroupComponent,
  GroupCreateDialogComponent,
  GroupPopupComponent,
  GroupEditComponent,
  GroupAssignUsersDialogComponent,
  GroupAssignUsersPopupComponent,
  GroupAssignRolesDialogComponent,
  GroupAssignRolesPopupComponent,
  GroupDeletePopupComponent,
  GroupDeleteDialogComponent,
  groupRoute,
  groupPopupRoute,
  GroupResolvePagingParams
} from './';

const ENTITY_STATES = [...groupRoute, ...groupPopupRoute];

@NgModule({
  imports: [AutoCompleteModule, IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GroupComponent,
    GroupCreateDialogComponent,
    GroupPopupComponent,
    GroupEditComponent,
    GroupAssignUsersDialogComponent,
    GroupAssignUsersPopupComponent,
    GroupAssignRolesDialogComponent,
    GroupAssignRolesPopupComponent,
    GroupDeleteDialogComponent,
    GroupDeletePopupComponent
  ],
  entryComponents: [
    GroupComponent,
    GroupCreateDialogComponent,
    GroupPopupComponent,
    GroupEditComponent,
    GroupAssignUsersDialogComponent,
    GroupAssignUsersPopupComponent,
    GroupAssignRolesDialogComponent,
    GroupAssignRolesPopupComponent,
    GroupDeleteDialogComponent,
    GroupDeletePopupComponent
  ],
  providers: [GroupService, GroupPopupService, GroupAssignPopupService, GroupResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcGroupModule {}
