import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subject } from '../user/user.model';
import { RoleService } from './role.service';
import { RoleAssignPopupService } from './role-assign-popup.service';

@Component({
  selector: 'ic-role-assign-users-dialog',
  templateUrl: './role-assign-users-dialog.component.html'
})
export class RoleAssignUsersDialogComponent implements OnInit {
  isSaving?: boolean;

  resourceId?: string;
  searchedUsers: Subject[] = [];
  chooseedUsers: Subject[] = [];
  previousUrl?: string;

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private roleService: RoleService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  assign(): void {
    this.isSaving = true;
    if (this.chooseedUsers != null && this.chooseedUsers.length > 0) {
      this.roleService.assignUsers(this.resourceId, this.chooseedUsers).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedUserListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignUsers(event: any): void {
    this.roleService
      .searchUnAssignedUsers({
        resourceId: this.resourceId,
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
      this.router.navigate(['admin/acm/ic-role/' + this.resourceId]);
    }, 0);
  }
}

@Component({
  selector: 'ic-role-assign-users-popup',
  template: ''
})
export class RoleAssignUsersPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private roleAssignPopupService: RoleAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.roleAssignPopupService.open(RoleAssignUsersDialogComponent as Component, params['id']);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
