import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { UserService } from './user.service';
import { UserResetpwdPopupService } from './user-resetpwd-popup.service';

@Component({
  selector: 'ic-user-resetpwd-dialog',
  templateUrl: './user-resetpwd-dialog.component.html'
})
export class UserResetpwdDialogComponent implements OnInit {
  loginName?: string;
  userId?: string;
  previousUrl?: string;
  constructor(
    private userService: UserService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = this.router.url.substring(this.router.url.lastIndexOf('/') + 1);
    this.previousUrl = 'admin/acm/ic-user/' + this.userId;
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmResetPwd(loginName: any): void {
    this.userService.resetPassword(loginName).subscribe(() => {
      this.eventManager.broadcast({
        name: 'resourceListModification',
        content: 'Reset a User Password'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  private backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate([this.previousUrl]);
    }, 0);
  }
}

@Component({
  selector: 'ic-user-resetpwd-popup',
  template: ''
})
export class UserResetpwdPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private userResetpwdPopupService: UserResetpwdPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.userResetpwdPopupService.open(UserResetpwdDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
