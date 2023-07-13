import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Subject } from './user.model';
import { UserPopupService } from './user-popup.service';
import { UserService } from './user.service';
import { SubjectLogin } from './user-login.model';

@Component({
  selector: 'ic-user-create-dialog',
  templateUrl: './user-create-dialog.component.html'
})
export class UserCreateDialogComponent implements OnInit {
  user: Subject = new Subject();
  userLogin: SubjectLogin;
  userLogins: SubjectLogin[];
  isSaving?: boolean;
  effectiveDtDp: any;
  expiryDtDp: any;
  lastLoginDtDp: any;
  createdDtDp: any;
  updatedDtDp: any;
  statuses: any[];
  loginTypes: any[];

  constructor(
    public activeModal: NgbActiveModal,
    private userService: UserService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {
    this.userLogin = new SubjectLogin();
    this.userLogins = [];
    this.statuses = [
      { codeId: 'A', codeDesc: 'Active' },
      { codeId: 'S', codeDesc: 'Suspended' },
      { codeId: 'I', codeDesc: 'Inactive' },
      { codeId: 'R', codeDesc: 'Revoked' },
      { codeId: 'P', codeDesc: 'Pending' },
      { codeId: 'D', codeDesc: 'Deactived' }
    ];
    this.loginTypes = [
      { codeId: 'PASSWORD', codeDesc: 'Password' },
      { codeId: 'SINGPASS', codeDesc: 'Singpass' },
      { codeId: 'CORPPASS', codeDesc: 'CorpPass' },
      { codeId: 'ADFS', codeDesc: 'ADFS' },
      { codeId: 'LDAP', codeDesc: 'LDAP' }
    ];
  }

  ngOnInit(): void {
    this.isSaving = false;
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  save(): void {
    this.isSaving = true;
    if (this.userLogin) {
      this.userLogins.push(this.userLogin);
      this.user.subjectLogins = this.userLogins;
    }
    this.subscribeToSaveResponse(this.userService.create(this.user));
  }

  showLoginId(): boolean {
    return true;
  }

  private subscribeToSaveResponse(result: Observable<Subject>): void {
    result.subscribe(
      (res: Subject) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Subject): void {
    this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
    this.isSaving = false;
    this.activeModal.dismiss(result);
    this.backToPrevious();
  }

  private onSaveError(error: any): void {
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    const dom = document.getElementById('appDialogErrorMsg');
    if (dom) {
      dom.innerHTML = appDialogErrorMsg;
      dom.style.display = 'block';
    }
  }

  private backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-user-popup',
  template: ''
})
export class UserPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private userPopupService: UserPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(() => {
      this.userPopupService.open(UserCreateDialogComponent as Component);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
