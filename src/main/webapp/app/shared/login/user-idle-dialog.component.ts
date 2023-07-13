import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'ic-user-idle-dialog',
  templateUrl: './user-idle-dialog.component.html'
})
export class UserIdleDialogComponent implements OnInit {
  idleMinites: string;
  constructor(public activeModal: NgbActiveModal, private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.getSessionTimeoutMin().subscribe((res: HttpResponse<any>) => {
      const idleTime = parseInt(+res.body[1] / 60 + '', 0);
      if (idleTime === 0) {
        this.idleMinites = '1';
      } else {
        this.idleMinites = idleTime + '';
      }
    });
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
  }
}
