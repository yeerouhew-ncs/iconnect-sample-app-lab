import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IC_PIPES, IC_DIRECTIVES, IC_COMPONENTS, IC_SERVICES } from './ic-components';

// Re export the files
export * from './i18n';
export * from './code-table';
export * from './nric';
export * from './user-picker';

@NgModule({
  imports: [CommonModule],
  declarations: [...IC_PIPES, ...IC_DIRECTIVES, ...IC_COMPONENTS],
  providers: [...IC_SERVICES],
  exports: [...IC_PIPES, ...IC_DIRECTIVES, ...IC_COMPONENTS, CommonModule]
})
export class NgIConnectModule {}
