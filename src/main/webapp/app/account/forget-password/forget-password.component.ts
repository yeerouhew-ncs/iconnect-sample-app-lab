import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { ForgetPasswordService } from './forget-password.service';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'ic-forget-password',
  templateUrl: './forget-password.component.html'
})
export class ForgetPasswordComponent implements OnInit, AfterViewInit {
  hasRecallQuestion: boolean | undefined;
  needSetRecallQuestion: boolean | undefined;
  error: string | null = null;
  badRequestError: any;
  success: string | null = null;
  resetPwdRequest: any;
  @ViewChild('username', { static: false })
  username?: ElementRef;
  recallAns: string | undefined;
  modalRef: NgbModalRef | undefined;

  constructor(private forgetPasswordService: ForgetPasswordService, private router: Router) {}

  ngOnInit(): void {
    this.resetPwdRequest = {
      email: '',
      recallQuestion: ''
    };
    this.hasRecallQuestion = false;
  }

  ngAfterViewInit(): void {
    if (this.username) {
      this.username.nativeElement.focus();
    }
  }

  getRecallQuestion(): void {
    this.needSetRecallQuestion = false;
    this.forgetPasswordService.getRecalQuestion({ username: this.username }).subscribe(
      responseData => {
        this.resetPwdRequest = responseData;
        this.hasRecallQuestion = true;
      },
      error => {
        this.error = error.error;
        if (this.error) {
          if (this.error.includes('question')) {
            this.needSetRecallQuestion = true;
          }
        }
      }
    );
  }

  requestReset(): void {
    this.forgetPasswordService
      .save({
        username: this.username,
        recallAns: this.recallAns
      })
      .subscribe(
        response => {
          this.error = null;
          this.success = response.msg;
        },
        error => {
          this.success = null;
          if (error.status === 400) {
            this.badRequestError = error;
          } else {
            this.error = error.headers.get('error');
          }
        }
      );
  }

  setRecallQuestion(): void {
    this.router.navigate(['account/password/recall'], {
      queryParams: {
        username: this.username
      }
    });
  }

  goHome(): void {
    this.router.navigate(['']);
  }
}
