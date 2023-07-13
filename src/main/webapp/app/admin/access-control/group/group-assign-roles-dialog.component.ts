import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Group } from './group.model';
import { GroupService } from './group.service';
import { Subject } from '../user/user.model';
import { GroupAssignPopupService } from './group-assign-popup.service';

@Component({
  selector: 'ic-group-assign-roles-dialog',
  templateUrl: './group-assign-roles-dialog.component.html'
})
export class GroupAssignRolesDialogComponent implements OnInit {
  group: Group = new Group();
  isSaving?: boolean;
  groupId?: string;
  searchedRoles: Subject[] = [];
  chooseedRoles: Subject[] = [];
  previousUrl?: string;

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private groupService: GroupService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.previousUrl = 'admin/acm/ic-group/' + this.groupId;
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToprevious();
  }

  assign(): void {
    this.isSaving = true;
    if (this.chooseedRoles != null && this.chooseedRoles.length > 0) {
      this.groupService.assignRoles(this.groupId, this.chooseedRoles).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
        this.backToprevious();
      });
    }
  }

  searchUnAssignRoles(event: any): void {
    this.groupService
      .searchUnAssignedRoles({
        groupId: this.groupId,
        roleName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedRoles = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  private backToprevious(): void {
    setTimeout(() => {
      this.router.navigate([this.previousUrl]);
    }, 0);
  }
}

@Component({
  selector: 'ic-group-popup',
  template: ''
})
export class GroupAssignRolesPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private groupAssignPopupService: GroupAssignPopupService) {}

  ngOnInit(): any {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.groupAssignPopupService.open(GroupAssignRolesDialogComponent as Component, params['id']);
      } else {
        this.groupAssignPopupService.open(GroupAssignRolesDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): any {
    this.routeSub.unsubscribe();
  }
}
