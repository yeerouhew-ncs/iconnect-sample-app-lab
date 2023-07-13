import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { UserService } from './user.service';
import { UserAssignPopupService } from './user-assign-popup.service';
import { Group } from '../group/group.model';

@Component({
  selector: 'ic-user-assign-groups-dialog',
  templateUrl: './user-assign-groups-dialog.component.html'
})
export class UserAssignGroupsDialogComponent implements OnInit {
  isSaving?: boolean;
  subjectId?: string;
  searchedGroups: Group[] = [];
  chooseedGroups: Group[] = [];
  userId?: string;
  previousUrl?: string;
  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private userService: UserService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.userId = this.router.url.substring(this.router.url.lastIndexOf('/') + 1);
    this.previousUrl = 'admin/acm/ic-user/' + this.userId;
  }

  clear(): void {
    this.activeModal.close('cancel');
    this.backToPrevious();
  }

  assign(): void {
    this.isSaving = true;
    if (this.chooseedGroups != null && this.chooseedGroups.length > 0) {
      this.userService.assignGroups(this.subjectId, this.chooseedGroups).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.backToPrevious();
        this.eventManager.broadcast({ name: 'assignedGroupListModification', content: 'OK' });
      });
    }
  }

  searchUnAssignGroups(event: any): void {
    this.userService
      .searchUnAssignedGroups({
        subjectId: this.subjectId,
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
      this.router.navigate([this.previousUrl]);
    }, 0);
  }
}

@Component({
  selector: 'ic-user-assign-groups-popup',
  template: ''
})
export class UserAssignGroupsPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private userAssignPopupService: UserAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.userAssignPopupService.open(UserAssignGroupsDialogComponent as Component, params['id']);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
