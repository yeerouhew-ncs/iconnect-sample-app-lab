import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-maintain-profile',
  templateUrl: './maintain-profile.component.html'
})
export class MaintainProfileComponent implements OnInit {
  error: string | null = null;
  success: string | null = null;
  currentAccount: any;
  activeTabIndex: string;

  constructor(private account: AccountService) {
    this.activeTabIndex = '1';
  }

  ngOnInit(): void {
    this.account.identity().subscribe(account => {
      this.currentAccount = this.copyAccount(account);
    });
  }

  save(): void {
    this.account.save(this.currentAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.account.identity(true).subscribe(account => {
          this.currentAccount = this.copyAccount(account);
        });
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  copyAccount(account: any): any {
    return {
      email: account.email,
      firstName: account.firstName,
      lastName: account.lastName,
      login: account.username,
      authorities: account.authorities,
      groups: account.groups
    };
  }

  switchTab(tabIndex: string): void {
    this.activeTabIndex = tabIndex;
  }
}
