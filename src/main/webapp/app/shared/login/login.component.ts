import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { AES, mode, pad, enc, lib } from 'crypto-js';
import { LoginService } from 'app/core/login/login.service';
import { UserIdleService } from './user-idle.service';

@Component({
  selector: 'ic-login-modal',
  templateUrl: './login.component.html'
})
export class LoginModalComponent implements AfterViewInit {
  @ViewChild('username', { static: false })
  username?: ElementRef;
  authenticationMsg: string;
  authenticationError = false;

  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });

  constructor(
    private loginService: LoginService,
    private router: Router,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private userIdleService: UserIdleService
  ) {}

  ngAfterViewInit(): void {
    if (this.username) {
      this.username.nativeElement.focus();
    }
  }

  cancel(): void {
    this.authenticationError = false;
    this.authenticationMsg = null;
    this.loginForm.patchValue({
      username: '',
      password: ''
    });
    this.activeModal.dismiss('cancel');
  }

  login(): void {
    const secretKey = lib.WordArray.random(8).toString();
    const initialValue = lib.WordArray.random(8).toString();
    const clientKey = enc.Utf8.parse(secretKey);
    const clinetIv = enc.Utf8.parse(initialValue);
    const encUserName = AES.encrypt(this.loginForm.get('username')!.value, clientKey, {
      mode: mode.CBC,
      padding: pad.Pkcs7,
      iv: clinetIv
    }).toString();
    const encPassword = AES.encrypt(this.loginForm.get('password')!.value, clientKey, {
      mode: mode.CBC,
      padding: pad.Pkcs7,
      iv: clinetIv
    }).toString();

    this.loginService
      .login({
        username: encUserName,
        password: encPassword,
        rememberMe: this.loginForm.get('rememberMe')!.value,
        key: secretKey,
        iv: initialValue
      })
      .subscribe(
        () => {
          this.userIdleService.setUserIdle();
          this.authenticationError = false;
          this.authenticationMsg = null;
          this.activeModal.close();
          if (
            this.router.url === '/account/register' ||
            this.router.url.startsWith('/account/activate') ||
            this.router.url.startsWith('/account/reset/')
          ) {
            this.router.navigate(['']);
          }
        },
        error => {
          // processing for the first login
          if (error.headers && error.headers.get('errorcode') === '901') {
            this.activeModal.dismiss('to state reset password');
            const username = error.headers.get('username');
            this.router.navigate(['/account/reset', 'request', username, true], error);
          } else {
            this.authenticationError = true;
            this.authenticationMsg = error.error.detail;
          }
        }
      );
  }

  register(): void {
    this.activeModal.dismiss('to state register');
    this.router.navigate(['/account/register']);
  }

  requestResetPassword(): void {
    this.activeModal.dismiss('to state requestReset');
    this.router.navigate(['/account/reset', 'request']);
  }

  goToForgetPwd(): void {
    this.activeModal.dismiss('go to forget password');
    this.router.navigate(['/account/password', 'request']);
  }
}
