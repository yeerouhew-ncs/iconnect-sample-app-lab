import { NgModule } from '@angular/core';
import { IconnectSampleAppLabSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { UserIdleDialogComponent } from './login/user-idle-dialog.component';
import { TimeOutDialogComponent } from './login/time-out-dialog.component';
import { UserIdleService } from './login/user-idle.service';
import { NgIdleModule } from '@ng-idle/core';

@NgModule({
  imports: [IconnectSampleAppLabSharedLibsModule, NgIdleModule.forRoot()],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    UserIdleDialogComponent,
    TimeOutDialogComponent
  ],
  entryComponents: [LoginModalComponent, UserIdleDialogComponent, TimeOutDialogComponent],
  exports: [
    IconnectSampleAppLabSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective
  ],
  providers: [UserIdleService]
})
export class IconnectSampleAppLabSharedModule {}
