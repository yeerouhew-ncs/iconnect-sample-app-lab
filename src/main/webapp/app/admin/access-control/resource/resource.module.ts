import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import {
  ResourceService,
  ResourcePopupService,
  ResourceAssignPopupService,
  ResourceComponent,
  ResourceCreateDialogComponent,
  ResourcePopupComponent,
  ResourceDeleteDialogComponent,
  ResourceDeletePopupComponent,
  ResourceEditComponent,
  ResourceAssignFunctionsDialogComponent,
  ResourceAssignFunctionsPopupComponent,
  resourceRoute,
  resourcePopupRoute,
  ResourceResolvePagingParams
} from './';

const ENTITY_STATES = [...resourceRoute, ...resourcePopupRoute];

@NgModule({
  imports: [AutoCompleteModule, IconnectSampleAppLabSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ResourceComponent,
    ResourceCreateDialogComponent,
    ResourcePopupComponent,
    ResourceDeleteDialogComponent,
    ResourceDeletePopupComponent,
    ResourceEditComponent,
    ResourceAssignFunctionsDialogComponent,
    ResourceAssignFunctionsPopupComponent
  ],
  entryComponents: [
    ResourceComponent,
    ResourceCreateDialogComponent,
    ResourcePopupComponent,
    ResourceDeleteDialogComponent,
    ResourceDeletePopupComponent,
    ResourceEditComponent,
    ResourceAssignFunctionsDialogComponent,
    ResourceAssignFunctionsPopupComponent
  ],
  providers: [ResourceService, ResourcePopupService, ResourceAssignPopupService, ResourceResolvePagingParams],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcResourceModule {}
