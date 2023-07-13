import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IcApplicationModule } from './application/application.module';
import { IcGroupModule } from './group/group.module';
import { IcRoleModule } from './role/role.module';
import { IcFunctionModule } from './function/function.module';
import { IcUserModule } from './user/user.module';
import { IcResourceModule } from './resource/resource.module';
import { RouterModule } from '@angular/router';
import { acmRoutes } from './access-control.route';
import { IcUserTokenModule } from './login-control/user-token.module';
import { JhiLanguageService } from 'ng-jhipster';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  imports: [
    IcApplicationModule,
    IcGroupModule,
    IcRoleModule,
    IcFunctionModule,
    IcResourceModule,
    IcUserModule,
    IcUserTokenModule,
    RouterModule.forChild(acmRoutes)
    /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
  ],
  declarations: [],
  entryComponents: [],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IcAccessControlModule {}
