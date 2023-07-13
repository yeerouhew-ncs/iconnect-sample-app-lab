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
  selector: 'ic-group-assign-users-dialog',
  templateUrl: './group-assign-users-dialog.component.html'
})
export class GroupAssignUsersDialogComponent implements OnInit {
  group: Group = new Group();
  isSaving?: boolean;
  groupId?: string;
  searchedUsers: Subject[] = [];
  chooseedUsers: Subject[] = [];
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
    this.backToPrevious();
  }

  assign(): void {
    this.isSaving = true;
    if (this.chooseedUsers != null && this.chooseedUsers.length > 0) {
      this.groupService.assignUsers(this.groupId, this.chooseedUsers).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedUserListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignUsers(event: any): void {
    this.groupService
      .searchUnAssignedUsers({
        groupId: this.groupId,
        firstName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedUsers = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  private backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate([this.previousUrl]);
    }, 0);
  }
}

@Component({
  selector: 'ic-group-assign-users-popup',
  template: ''
})
export class GroupAssignUsersPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private groupAssignPopupService: GroupAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.groupAssignPopupService.open(GroupAssignUsersDialogComponent as Component, params['id']);
      } else {
        this.groupAssignPopupService.open(GroupAssignUsersDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
