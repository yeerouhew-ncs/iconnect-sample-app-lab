import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { UserService } from './user.service';
import { Group } from '../group/group.model';
import { Subject } from './user.model';
import { SubjectLogin } from './user-login.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ic-user-edit',
  templateUrl: './user-edit.component.html'
})
export class UserEditComponent implements OnInit, OnDestroy {
  currentAccount: any;
  error: any;
  success: any;
  eventSubscriber?: Subscription;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  reverse: any;

  isSaving?: boolean;
  subjectId: string | undefined;
  user: Subject = new Subject();
  userLogin: SubjectLogin;
  userLogins: SubjectLogin[];

  assignedRoles: Resource[] = [];
  assignedGroups: Group[] = [];

  selectedAllRoles?: boolean;
  selectedRoles: Resource[] = [];

  selectedAllGroups?: boolean;
  selectedGroups: Group[] = [];

  subscription?: Subscription;

  effectiveDtDp: any;
  expiryDtDp: any;
  statuses: any[];
  loginTypes: any[];

  constructor(
    private userService: UserService,
    private alertService: JhiAlertService,
    private route: ActivatedRoute,
    private eventManager: JhiEventManager,
    private datePipe: DatePipe
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
    this.subscription = this.route.params.subscribe(params => {
      this.subjectId = params['id'];
    });

    // get user object
    this.load(this.subjectId);

    // get all assigned roles, groups
    this.getAllAssignedRoles(this.subjectId);
    this.getAllAssignedGroups(this.subjectId);

    // register change
    this.registerChangeInAssignedRoles();
    this.registerChangeInAssignedGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  load(id: any): void {
    this.userService.find(id).subscribe(user => {
      if (user.effectiveDt) {
        user.effectiveDt = this.datePipe.transform(user.effectiveDt, 'yyyy-MM-dd');
      }
      if (user.expiryDt) {
        user.expiryDt = this.datePipe.transform(user.expiryDt, 'yyyy-MM-dd');
      }
      this.user = user;
      this.userLogin = this.user.subjectLogins ? this.user.subjectLogins[0] : new SubjectLogin();
    });
  }

  getAllAssignedRoles(subjectId: any): void {
    this.userService.findAllAssignedRoles(subjectId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedRoles = res.body;
        this.selectedAllRoles = false;
        this.selectedRoles = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  getAllAssignedGroups(subjectId: any): void {
    this.userService.findAllAssignedGroups(subjectId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedGroups = res.body;
        this.selectedAllGroups = false;
        this.selectedGroups = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  save(): void {
    this.isSaving = true;
    if (this.userLogin) {
      this.userLogins = [];
      this.userLogins.push(this.userLogin);
      this.user.subjectLogins = this.userLogins;
    }
    this.subscribeToSaveResponse(this.userService.update(this.user));
  }

  selectAllRoles(checked: any): void {
    this.selectedRoles = [];
    this.selectedAllRoles = checked;
    if (checked) {
      for (let i = 0; i < this.assignedRoles.length; i++) {
        this.selectedRoles.push(this.assignedRoles[i]);
      }
    }
  }

  selectOneRole(checked: any, resource: any): void {
    if (checked) {
      if (this.selectedRoles == null) {
        this.selectedRoles = [];
      }
      this.selectedRoles.push(resource);
    } else {
      const index = this.selectedRoles.indexOf(resource);
      if (index >= 0) {
        this.selectedRoles.splice(index, 1);
      }
      if (this.selectedRoles === null || this.selectedRoles.length === 0) {
        this.selectedAllRoles = false;
      }
    }
  }

  selectAllGroups(checked: any): void {
    this.selectedGroups = [];
    this.selectedAllGroups = checked;
    if (checked) {
      for (let i = 0; i < this.assignedGroups.length; i++) {
        this.selectedGroups.push(this.assignedGroups[i]);
      }
    }
  }

  selectOneGroup(checked: any, group: any): void {
    if (checked) {
      if (this.selectedGroups == null) {
        this.selectedGroups = [];
      }
      this.selectedGroups.push(group);
    } else {
      const index = this.selectedGroups.indexOf(group);
      if (index >= 0) {
        this.selectedGroups.splice(index, 1);
      }
      if (this.selectedGroups === null || this.selectedGroups.length === 0) {
        this.selectedAllGroups = false;
      }
    }
  }

  assignedRolesLength(): any {
    return this.assignedRoles ? this.assignedRoles.length : 0;
  }

  assignedGroupsLength(): any {
    return this.assignedGroups ? this.assignedGroups.length : 0;
  }

  unAssignRoles(): void {
    if (this.selectedRoles != null && this.selectedRoles.length > 0) {
      this.userService.unAssignRoles(this.subjectId, this.selectedRoles).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
      });
    }
  }

  unAssignGroups(): void {
    if (this.selectedGroups != null && this.selectedGroups.length > 0) {
      this.userService.unAssignGroups(this.subjectId, this.selectedGroups).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedGroupListModification', content: 'OK' });
      });
    }
  }

  registerChangeInAssignedRoles(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedRoleListModification', () => this.getAllAssignedRoles(this.user.subjectId));
  }

  registerChangeInAssignedGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedGroupListModification', () =>
      this.getAllAssignedGroups(this.user.subjectId)
    );
  }

  showLoginId(): boolean {
    return true;
  }

  private subscribeToSaveResponse(result: Observable<Group>): void {
    result.subscribe(
      (res: Group) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: any): void {
    this.isSaving = false;
    this.load(result.subjectId);
  }

  private onSaveError(error: any): void {
    this.isSaving = false;
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    const dom = document.getElementById('appDialogErrorMsg');
    if (dom) {
      dom.innerHTML = appDialogErrorMsg;
      dom.style.display = 'block';
    }
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }
}
