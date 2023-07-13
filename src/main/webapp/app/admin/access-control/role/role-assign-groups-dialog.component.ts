import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { RoleService } from './role.service';
import { RoleAssignPopupService } from './role-assign-popup.service';
import { Group } from '../group/group.model';

@Component({
  selector: 'ic-role-assign-groups-dialog',
  templateUrl: './role-assign-groups-dialog.component.html'
})
export class RoleAssignGroupsDialogComponent implements OnInit {
  isSaving?: boolean;
  resourceId?: string;
  searchedGroups: Group[] = [];
  chooseedGroups: Group[] = [];

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
    if (this.chooseedGroups != null && this.chooseedGroups.length > 0) {
      this.roleService.assignGroups(this.resourceId, this.chooseedGroups).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedGroupListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignGroups(event: any): void {
    this.roleService
      .searchUnAssignedGroups({
        resourceId: this.resourceId,
        groupName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedGroups = res.body;
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
  selector: 'ic-role-assign-groups-popup',
  template: ''
})
export class RoleAssignGroupsPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private roleAssignPopupService: RoleAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.roleAssignPopupService.open(RoleAssignGroupsDialogComponent as Component, params['id']);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
