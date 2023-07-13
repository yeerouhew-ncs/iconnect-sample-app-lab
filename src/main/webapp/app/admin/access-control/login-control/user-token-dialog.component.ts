import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { UserToken } from 'app/admin/access-control/login-control/user-token.model';
import { UserTokenPopupService } from 'app/admin/access-control/login-control/user-token-popup.service';
import { UserTokenService } from 'app/admin/access-control/login-control/user-token.service';

@Component({
  selector: 'ic-user-token-dialog',
  templateUrl: './user-token-dialog.component.html'
})
export class UserTokenDialogComponent implements OnInit {
  userToken: UserToken = new UserToken();

  constructor(
    public activeModal: NgbActiveModal,
    private userTokenService: UserTokenService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: any): void {
    this.userTokenService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'userTokenListModification',
        content: 'Logout an User'
      });
    });
    this.activeModal.dismiss(true);
    this.backToPrevious();
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-user-token']);
    }, 0);
  }
}

@Component({
  selector: 'ic-application-popup',
  template: ''
})
export class UserTokenPopupComponent implements OnInit, OnDestroy {
  routeSub: any;
  constructor(private route: ActivatedRoute, private userTokenPopupService: UserTokenPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.userTokenPopupService.open(UserTokenDialogComponent as Component, params['id']);
      } else {
        this.userTokenPopupService.open(UserTokenDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
