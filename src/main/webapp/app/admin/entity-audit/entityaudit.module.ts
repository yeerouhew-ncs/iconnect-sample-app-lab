import { CommonModule } from '@angular/common';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { entityAuditRoute, EntityAuditResolvePagingParams } from './entityaudit.route';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuditLogComponent } from './audit-log/audit-log.component';
import { AuditLogService } from './audit-log/audit-log.service';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { JhiLanguageService } from 'ng-jhipster';

const LOGADMIN_STATES = [...entityAuditRoute];

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    NgbDatepickerModule,
    IconnectSampleAppLabSharedModule,
    RouterModule.forChild(LOGADMIN_STATES),
    NgbModule
    // NgbModule.forRoot(),
  ],
  declarations: [AuditLogComponent],
  entryComponents: [],
  providers: [AuditLogService, EntityAuditResolvePagingParams, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityAuditModule {}
