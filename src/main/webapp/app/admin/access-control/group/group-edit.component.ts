import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Group } from './group.model';
import { GroupService } from './group.service';
import { Subject } from '../user/user.model';
import { Resource } from '../resource/resource.model';

@Component({
  selector: 'ic-group',
  templateUrl: './group-edit.component.html'
})
export class GroupEditComponent implements OnInit, OnDestroy {
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
  groupId?: string;
  group: Group = new Group();
  parentGroups: Group[] = [];
  assignedUsers: Subject[] = [];
  assignedRoles: Resource[] = [];
  selectedAllUsers?: boolean;
  selectedUsers: Subject[] = [];
  selectedAllRoles?: boolean;
  selectedRoles: Resource[] = [];
  subscription?: Subscription;

  constructor(
    private groupService: GroupService,
    private alertService: JhiAlertService,
    private route: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      this.groupId = params['id'];
    });

    // get group
    this.load(this.groupId);

    // get parent groups
    this.getParentGroups();

    // get all assigned users
    this.getAllAssignedUsers(this.groupId);

    // get all assigned roles
    this.getAllAssignedRoles(this.groupId);

    this.registerChangeInAssignedUsers();
    this.registerChangeInAssignedRoles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  load(id: any): void {
    this.groupService.find(id).subscribe(group => {
      this.group = group;
    });
  }

  getParentGroups(): void {
    this.groupService
      .findAll({
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.parentGroups = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  getAllAssignedUsers(groupId: any): void {
    this.groupService.findAllAssignedUsers(groupId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedUsers = res.body;
        this.selectedAllUsers = false;
        this.selectedUsers = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  getAllAssignedRoles(groupId: any): void {
    this.groupService.findAllAssignedRoles(groupId).subscribe(
      (res: HttpResponse<any>) => {
        this.assignedRoles = res.body;
        this.selectedAllRoles = false;
        this.selectedRoles = [];
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.groupService.update(this.group));
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

  assignedUsersLength(): any {
    return this.assignedUsers ? this.assignedUsers.length : 0;
  }

  assignedRolesLength(): any {
    return this.assignedRoles ? this.assignedRoles.length : 0;
  }

  unAssignUsers(): void {
    if (this.selectedUsers != null && this.selectedUsers.length > 0) {
      this.groupService.unAssignUsers(this.groupId, this.selectedUsers).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedUserListModification', content: 'OK' });
      });
    }
  }

  unAssignRoles(): void {
    if (this.selectedRoles != null && this.selectedRoles.length > 0) {
      this.groupService.unAssignRoles(this.groupId, this.selectedRoles).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
      });
    }
  }

  registerChangeInAssignedUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedUserListModification', () => this.getAllAssignedUsers(this.group.groupId));
  }

  registerChangeInAssignedRoles(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedRoleListModification', () => this.getAllAssignedRoles(this.group.groupId));
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
