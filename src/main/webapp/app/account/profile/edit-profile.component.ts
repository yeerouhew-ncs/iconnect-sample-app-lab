import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { MaintainProfileService } from './maintain-profile.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-edit-profile',
  templateUrl: './edit-profile.component.html'
})
export class EditProfileComponent implements OnInit {
  currentAccount: any | undefined;
  originalAccount: any;

  constructor(
    private accountService: AccountService,
    private alertService: JhiAlertService,
    private maintainProfileService: MaintainProfileService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.originalAccount = this.copyAccount(account);
      this.currentAccount = this.copyAccount(account);
    });
  }

  save(): void {
    if (this.isObjectEqual(this.currentAccount, this.originalAccount)) {
      // this.alertService.warning('profile.messages.warning.nochanges', null, null);
      this.alertService.warning('profile.messages.warning.nochanges', null);
      return;
    }
    this.maintainProfileService.save(this.currentAccount).subscribe(
      () => {
        this.onSaveSuccess();
      },
      (error: any) => {
        this.onSaveError(error);
      }
    );
  }

  private onSaveSuccess(): void {
    this.accountService.identity(true).subscribe(account => {
      this.originalAccount = this.copyAccount(account);
      this.currentAccount = this.copyAccount(account);
    });
    // this.alertService.success('profile.messages.success', null, null);
    this.alertService.success('profile.messages.success', null);
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
    // this.alertService.error(error.message, null, null);
    this.alertService.error(error.message, null);
  }

  copyAccount(account: any): any {
    return {
      email: account.email,
      firstName: account.firstName,
      lastName: account.lastName
    };
  }

  isObjectEqual(a: any, b: any): boolean {
    const aProps = Object.getOwnPropertyNames(a);
    const bProps = Object.getOwnPropertyNames(b);

    if (aProps.length !== bProps.length) {
      return false;
    }

    for (let i = 0; i < aProps.length; i++) {
      const propName = aProps[i];
      if (a[propName] !== b[propName]) {
        return false;
      }
    }
    return true;
  }

  gotoProfile(): void {
    this.router.navigate(['account/profile']);
  }
}
