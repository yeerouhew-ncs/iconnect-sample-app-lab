import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CalendarModule } from 'primeng/calendar';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { CodeTypeService } from '../code-type';
import { JhiLanguageService } from 'ng-jhipster';

import {
  CodeManageComponent,
  CodeDeleteDialogComponent,
  CodeDeletePopupComponent,
  codeRoute,
  codePopupRoute,
  CodeService,
  CodePopupService,
  CodeResolvePagingParams
} from './';

const ENTITY_STATES = [...codeRoute, ...codePopupRoute];

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, AutoCompleteModule, CalendarModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CodeManageComponent, CodeDeleteDialogComponent, CodeDeletePopupComponent],
  entryComponents: [CodeManageComponent, CodeDeleteDialogComponent, CodeDeletePopupComponent],
  providers: [
    CodeService,
    CodePopupService,
    CodeResolvePagingParams,
    CodeTypeService,
    { provide: JhiLanguageService, useClass: JhiLanguageService }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CodeModule {}
