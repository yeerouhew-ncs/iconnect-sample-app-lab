import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';

import { MaintainProfileService } from './maintain-profile.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-generate-token',
  templateUrl: './generate-token.component.html'
})
export class GenerateTokenComponent implements OnInit {
  error: string | undefined;
  success: string | undefined;
  currentAccount: any | undefined;
  tokenVM: any;
  accessToken: string | undefined;

  constructor(
    private accountService: AccountService,
    private alertService: JhiAlertService,
    private maintainProfileService: MaintainProfileService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = this.copyAccount(account);
    });
    this.initTokenVM();
  }

  private initTokenVM(): void {
    this.tokenVM = {
      expireDateAsStr: ''
    };
  }

  generatePersonalAccessToken(): void {
    this.maintainProfileService.createAccessToken(this.tokenVM).subscribe(
      response => {
        this.accessToken = response['id_token'];
        this.onSaveSuccess();
      },
      (error: any) => {
        this.onSaveError(error);
      }
    );
  }

  copyAccount(account: any): any {
    return {
      email: account.email,
      firstName: account.firstName,
      lastName: account.lastName
    };
  }

  private onSaveSuccess(): void {
    this.initTokenVM();
    // this.alertService.success('profile.messages.token.success', null, null);
    this.alertService.success('profile.messages.token.success', null);
  }

  private onSaveError(error: any): void {
    try {
      error.json();
    } catch (exception) {
      error.message = error.text();
    }
    this.onError(error);
  }

  private onError(error: any): void {
    if (error.message) {
      // this.alertService.error(error.message, null, null);
      this.alertService.error(error.message, null);
    } else {
      // this.alertService.error('profile.messages.token.fail', null, null);
      this.alertService.error('profile.messages.token.fail', null);
    }
  }
}
