import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'ic-view-profile',
  templateUrl: './view-profile.component.html'
})
export class ViewProfileComponent implements OnInit {
  error: string | undefined;
  success: string | undefined;
  currentAccount: any;

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = this.copyAccount(account);
    });
  }

  copyAccount(account: any): any {
    return {
      email: account.email,
      firstName: account.firstName,
      lastName: account.lastName,
      login: account.username,
      authorities: account.authorities ? this.serializeArrayList(account.authorities) : '',
      groups: account.groups ? this.serializeArrayList(account.groups) : '',
      loginMethod: account.authMethod
    };
  }

  private serializeArrayList(array: any): any {
    if (array instanceof Array && array.length > 0) {
      if (array.length === 1) {
        return array.toString();
      } else {
        let tempStr = array.toString();
        tempStr = tempStr.replace(',', ', ');
        return tempStr;
      }
    }
    return array;
  }
}
