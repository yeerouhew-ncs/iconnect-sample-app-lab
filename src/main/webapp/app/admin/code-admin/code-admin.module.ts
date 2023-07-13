import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CodeTypeModule } from './code-type/code-type.module';
import { CodeModule } from './code/code.module';

@NgModule({
  imports: [CodeTypeModule, CodeModule],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcCodeAdminModule {}
