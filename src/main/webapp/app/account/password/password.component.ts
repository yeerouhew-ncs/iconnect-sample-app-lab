import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { PasswordService } from './password.service';

@Component({
  selector: 'ic-password',
  templateUrl: './password.component.html'
})
export class PasswordComponent implements OnInit {
  doNotMatch = false;
  recallAnswerRequired = false;
  error = false;
  success = false;
  account$?: Observable<Account | null>;
  passwordForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newPassword: [
      '',
      [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50),
        Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z$@$!%*?&].{8,}')
      ]
    ],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    recallQuestion: ['', [Validators.minLength(4), Validators.maxLength(50)]],
    recallAnswer: ['', [Validators.minLength(4), Validators.maxLength(50)]]
  });

  constructor(
    private passwordService: PasswordService,
    private accountService: AccountService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.account$ = this.accountService.identity();
  }

  changePassword(): void {
    this.error = false;
    this.success = false;
    this.doNotMatch = false;

    if (this.passwordForm.get(['newPassword'])!.value !== this.passwordForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else if (this.passwordForm.get(['recallQuestion'])!.value !== '' && this.passwordForm.get(['recallAnswer'])!.value === '') {
      this.recallAnswerRequired = true;
    } else {
      this.passwordService.save(this.createAccountForm()).subscribe(
        () => (this.success = true),
        () => (this.error = true)
      );
    }
  }

  private createAccountForm(): any {
    return {
      oldPassword: this.passwordForm.get(['currentPassword'])!.value,
      newPassword: this.passwordForm.get(['newPassword'])!.value,
      newPassword2: this.passwordForm.get(['confirmPassword'])!.value,
      recallQuestion: this.passwordForm.get(['recallQuestion'])!.value,
      recallAnswer: this.passwordForm.get(['recallAnswer'])!.value
    };
  }

  cancel(): void {
    this.router.navigate(['account/profile']);
  }
}
