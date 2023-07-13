import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { MockLanguageService } from './helpers/mock-language.service';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { MockAccountService } from './helpers/mock-account.service';
import { MockActivatedRoute, MockRouter } from './helpers/mock-route.service';
import { MockActiveModal } from './helpers/mock-active-modal.service';
import { MockEventManager } from './helpers/mock-event-manager.service';
import { MockIcMenuService } from './helpers/mock-ic-menu.service';
import { IcMenuService } from 'app/layouts/sidebar/menu/services/menu-service/menu.service';
import { MockUserIdleService } from './helpers/mock-user-idle.service';
import { UserIdleService } from 'app/shared/login/user-idle.service';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { MockSessionService } from './helpers/mock-session.service';

@NgModule({
  providers: [
    DatePipe,
    JhiDataUtils,
    JhiDateUtils,
    JhiParseLinks,
    {
      provide: JhiLanguageService,
      useClass: MockLanguageService
    },
    {
      provide: JhiEventManager,
      useClass: MockEventManager
    },
    {
      provide: NgbActiveModal,
      useClass: MockActiveModal
    },
    {
      provide: ActivatedRoute,
      useValue: new MockActivatedRoute({ id: 123 })
    },
    {
      provide: Router,
      useClass: MockRouter
    },
    {
      provide: AccountService,
      useClass: MockAccountService
    },
    {
      provide: LoginModalService,
      useValue: null
    },
    {
      provide: JhiAlertService,
      useValue: null
    },
    {
      provide: NgbModal,
      useValue: null
    },
    {
      provide: IcMenuService,
      useClass: MockIcMenuService
    },
    {
      provide: UserIdleService,
      useClass: MockUserIdleService
    },
    {
      provide: LocalStorageService,
      useClass: MockSessionService
    },
    {
      provide: SessionStorageService,
      useClass: MockSessionService
    }
  ],
  imports: [HttpClientTestingModule]
})
export class IconnectSampleAppLabTestModule {}
