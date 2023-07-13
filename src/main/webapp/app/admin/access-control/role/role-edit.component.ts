import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subject } from '../user/user.model';
import { Resource } from '../resource/resource.model';
import { RoleService } from './role.service';
import { Group } from '../group/group.model';

@Component({
  selector: 'ic-role-edit',
  templateUrl: './role-edit.component.html'
})
export class RoleEditComponent implements OnInit, OnDestroy {
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
  resourceId?: any;
  role: Resource = new Resource();

  assignedUsers: Subject[] = [];
  assignedFuncs: Resource[] = [];
  assignedGroups: Group[] = [];

  selectedAllUsers?: boolean;
  selectedUsers: Subject[] = [];

  selectedAllFuncs?: boolean;
  selectedFuncs: Resource[] = [];

  selectedAllGroups?: boolean;
  selectedGroups: Group[] = [];

  subscription?: Subscription;

  constructor(
    private roleService: RoleService,
    private alertService: JhiAlertService,
    private route: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      this.resourceId = params['id'];
    });

    // get role object
    this.load(this.resourceId);

    // get all assigned users, functions, groups
    this.getAllAssignedUsers(this.resourceId);
    this.getAllAssignedFuncs(this.resourceId);
    this.getAllAssignedGroups(this.resourceId);

    // register change
    this.registerChangeInAssignedUsers();
    this.registerChangeInAssignedFuncs();
    this.registerChangeInAssignedGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  load(id: any): void {
    this.roleService.find(id).subscribe(role => {
      this.role = role;
    });
  }

  getAllAssignedUsers(resourceId: any): void {
    this.roleService.findAllAssignedUsers(resourceId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedUsers = res.body;
        this.selectedAllUsers = false;
        this.selectedUsers = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  getAllAssignedFuncs(resourceId: any): void {
    this.roleService.findAllAssignedFuncs(resourceId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedFuncs = res.body;
        this.selectedAllFuncs = false;
        this.selectedFuncs = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  getAllAssignedGroups(resourceId: any): void {
    this.roleService.findAllAssignedGroups(resourceId).subscribe(
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
    this.subscribeToSaveResponse(this.roleService.update(this.role));
  }

  lowerCaseRoleId(): void {
    if (this.role.resourceId) {
      this.role.resourceId = this.role.resourceId.toLowerCase();
    }
  }

  selectAllUsers(checked: any): void {
    this.selectedUsers = [];
    this.selectedAllUsers = checked;
    if (checked) {
      for (let i = 0; i < this.assignedUsers.length; i++) {
        this.selectedUsers.push(this.assignedUsers[i]);
      }
    }
  }

  selectOneUser(checked: any, subject: any): void {
    if (checked) {
      if (this.selectedUsers == null) {
        this.selectedUsers = [];
      }
      this.selectedUsers.push(subject);
    } else {
      const index = this.selectedUsers.indexOf(subject);
      if (index >= 0) {
        this.selectedUsers.splice(index, 1);
      }
      if (this.selectedUsers === null || this.selectedUsers.length === 0) {
        this.selectedAllUsers = false;
      }
    }
  }

  selectAllFuncs(checked: any): void {
    this.selectedFuncs = [];
    this.selectedAllFuncs = checked;
    if (checked) {
      for (let i = 0; i < this.assignedFuncs.length; i++) {
        this.selectedFuncs.push(this.assignedFuncs[i]);
      }
    }
  }

  selectOneFunc(checked: any, resource: any): void {
    if (checked) {
      if (this.selectedFuncs == null) {
        this.selectedFuncs = [];
      }
      this.selectedFuncs.push(resource);
    } else {
      const index = this.selectedFuncs.indexOf(resource);
      if (index >= 0) {
        this.selectedFuncs.splice(index, 1);
      }
      if (this.selectedFuncs === null || this.selectedFuncs.length === 0) {
        this.selectedAllFuncs = false;
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

  assignedUsersLength(): any {
    return this.assignedUsers ? this.assignedUsers.length : 0;
  }

  assignedFuncsLength(): any {
    return this.assignedFuncs ? this.assignedFuncs.length : 0;
  }

  assignedGroupsLength(): any {
    return this.assignedGroups ? this.assignedGroups.length : 0;
  }

  unAssignUsers(): void {
    if (this.selectedUsers != null && this.selectedUsers.length > 0) {
      this.roleService.unAssignUsers(this.resourceId, this.selectedUsers).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedUserListModification', content: 'OK' });
      });
    }
  }

  unAssignFuncs(): void {
    if (this.selectedFuncs != null && this.selectedFuncs.length > 0) {
      this.roleService.unAssignFuncs(this.resourceId, this.selectedFuncs).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedFuncListModification', content: 'OK' });
      });
    }
  }

  unAssignGroups(): void {
    if (this.selectedGroups != null && this.selectedGroups.length > 0) {
      this.roleService.unAssignGroups(this.resourceId, this.selectedGroups).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedGroupListModification', content: 'OK' });
      });
    }
  }

  registerChangeInAssignedUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedUserListModification', () =>
      this.getAllAssignedUsers(this.role.resourceId)
    );
  }

  registerChangeInAssignedFuncs(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedFuncListModification', () =>
      this.getAllAssignedFuncs(this.role.resourceId)
    );
  }

  registerChangeInAssignedGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedGroupListModification', () =>
      this.getAllAssignedGroups(this.role.resourceId)
    );
  }

  private subscribeToSaveResponse(result: Observable<Group>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
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
