import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { PasswordStrengthBarComponent } from './password/password-strength-bar.component';
import { RegisterComponent } from './register/register.component';
import { ActivateComponent } from './activate/activate.component';
import { PasswordComponent } from './password/password.component';
import { PasswordResetInitComponent } from './password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset/finish/password-reset-finish.component';
import { SettingsComponent } from './settings/settings.component';
import { accountState } from './account.route';
import { ViewProfileComponent } from 'app/account/profile/view-profile.component';
import { GenerateTokenComponent } from 'app/account/profile/generate-token.component';
import { EditProfileComponent } from 'app/account/profile/edit-profile.component';
import { MaintainProfileComponent } from 'app/account/profile/maintain-profile.component';
import { ForgetPasswordComponent } from 'app/account/forget-password/forget-password.component';
import { SetRecallQuestionComponent } from 'app/account/set-recall-question/set-recall-question.component';

import { MaintainProfileService } from 'app/account/profile/maintain-profile.service';
import { ForgetPasswordService } from 'app/account/forget-password/forget-password.service';
import { SetRecallQuestionService } from 'app/account/set-recall-question/set-recall-question.service';

import { ClipboardModule } from 'ngx-clipboard';

@NgModule({
  imports: [IconnectSampleAppLabSharedModule, ClipboardModule, RouterModule.forChild(accountState)],
  declarations: [
    ActivateComponent,
    RegisterComponent,
    PasswordComponent,
    PasswordStrengthBarComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    ViewProfileComponent,
    GenerateTokenComponent,
    EditProfileComponent,
    MaintainProfileComponent,
    ForgetPasswordComponent,
    SetRecallQuestionComponent
  ],
  providers: [MaintainProfileService, ForgetPasswordService, SetRecallQuestionService]
})
export class AccountModule {}
