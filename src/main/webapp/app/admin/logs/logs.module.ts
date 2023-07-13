import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';

import { LogsComponent } from './logs.component';

import { logsRoute } from './logs.route';

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, RouterModule.forChild([logsRoute])],
  declarations: [LogsComponent]
})
export class LogsModule {}
