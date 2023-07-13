import { Injectable, Component } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { TimeOutDialogComponent } from './time-out-dialog.component';
import { UserIdleDialogComponent } from './user-idle-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class UserIdleService {
  private ngbModalRef: NgbModalRef;
  private idleModalRef: NgbModalRef;
  sessionTimeoutMin: number;
  sessionTimeoutIdle: any = 300;
  constructor(
    private modalService: NgbModal,
    private idle: Idle,
    private accountService: AccountService,
    private loginService: LoginService
  ) {
    this.ngbModalRef = null;
    this.idleModalRef = null;
  }

  setUserIdle(): void {
    if (!this.idle.isRunning() && !this.idle.isIdling()) {
      this.setSessionTimoutMin();
    } else {
      this.resetUserIdle();
    }
  }

  private openUserIdle(component: Component): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }
      // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
      setTimeout(() => {
        this.ngbModalRef = this.userIdleModalRef(component);
        resolve(this.ngbModalRef);
      }, 0);
    });
  }

  private userIdleModalRef(component: Component): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.result.then(
      () => {
        this.ngbModalRef = null;
      },
      () => {
        this.ngbModalRef = null;
      }
    );
    return modalRef;
  }

  private resetUserIdle(): void {
    this.idle.watch();
  }

  private setSessionTimoutMin(): void {
    this.accountService.getSessionTimeoutMin().subscribe((res: HttpResponse<any>) => {
      this.sessionTimeoutMin = +res.body[0];
      this.sessionTimeoutIdle = +res.body[1];
      this.idle.setIdle(this.sessionTimeoutMin - this.sessionTimeoutIdle);
      this.idle.setTimeout(this.sessionTimeoutIdle);
      this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
      this.idle.onTimeout.subscribe(() => {
        if (this.idleModalRef != null) {
          this.idleModalRef.dismiss('cancel');
        }
        this.openUserIdle(TimeOutDialogComponent as Component).then(() => {
          this.loginService.logout();
        });
      });
      this.idle.onTimeoutWarning.subscribe(countdown => {
        if (countdown === this.sessionTimeoutIdle) {
          this.openUserIdle(UserIdleDialogComponent as Component).then(ngbModalRef => {
            this.idleModalRef = ngbModalRef;
          });
        }
      });
      this.resetUserIdle();
    });
  }
}
