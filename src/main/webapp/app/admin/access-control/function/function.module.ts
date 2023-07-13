import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  FunctionService,
  FunctionPopupService,
  FunctionAssignPopupService,
  FunctionComponent,
  FunctionCreateDialogComponent,
  FunctionPopupComponent,
  FunctionDeleteDialogComponent,
  FunctionDeletePopupComponent,
  FunctionEditComponent,
  FunctionAssignResourcesDialogComponent,
  FunctionAssignResourcesPopupComponent,
  FunctionAssignRolesDialogComponent,
  FunctionAssignRolesPopupComponent,
  functionRoute,
  functionPopupRoute,
  FunctionResolvePagingParams
} from './';

const ENTITY_STATES = [...functionRoute, ...functionPopupRoute];

@NgModule({
  imports: [AutoCompleteModule, IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FunctionComponent,
    FunctionCreateDialogComponent,
    FunctionPopupComponent,
    FunctionDeleteDialogComponent,
    FunctionDeletePopupComponent,
    FunctionEditComponent,
    FunctionAssignResourcesDialogComponent,
    FunctionAssignResourcesPopupComponent,
    FunctionAssignRolesDialogComponent,
    FunctionAssignRolesPopupComponent
  ],
  entryComponents: [
    FunctionComponent,
    FunctionCreateDialogComponent,
    FunctionPopupComponent,
    FunctionDeleteDialogComponent,
    FunctionDeletePopupComponent,
    FunctionEditComponent,
    FunctionAssignResourcesDialogComponent,
    FunctionAssignResourcesPopupComponent,
    FunctionAssignRolesDialogComponent,
    FunctionAssignRolesPopupComponent
  ],
  providers: [FunctionService, FunctionPopupService, FunctionAssignPopupService, FunctionResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcFunctionModule {}
