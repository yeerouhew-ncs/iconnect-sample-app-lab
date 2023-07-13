import { Component, OnInit, AfterViewInit, Renderer2, ElementRef } from '@angular/core';
import { PasswordResetInitService } from './password-reset-init.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'ic-password-reset-init',
  templateUrl: './password-reset-init.component.html'
})
export class PasswordResetInitComponent implements OnInit, AfterViewInit {
  isFirstLogin: boolean;
  doNotMatch: string;
  error: string;
  errorEmailNotExists: string;
  resetAccount: any;
  success: string;
  private subscription: Subscription;
  modalRef: NgbModalRef;

  constructor(
    private passwordResetInitService: PasswordResetInitService,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.resetAccount = {
      username: '',
      oldPassword: '',
      newPassword: '',
      newPassword2: '',
      recallQuestion: '',
      recallAnswer: ''
    };
    this.subscription = this.route.params.subscribe(params => {
      this.resetAccount.username = params['username'];
      this.isFirstLogin = params['isFirstLogin'];
    });
  }

  ngAfterViewInit(): void {
    this.renderer.selectRootElement('#oldPwd').focus();
  }

  requestReset(): void {
    this.error = null;
    if (this.resetAccount.newPassword !== this.resetAccount.newPassword2) {
      this.success = null;
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;

      this.passwordResetInitService.save(this.resetAccount).subscribe(
        response => {
          this.success = response.msg;
        },
        error => {
          this.success = null;
          this.error = error.headers.get('error');
        }
      );
    }
  }

  login(): void {
    this.router.navigate(['']);
  }
}
