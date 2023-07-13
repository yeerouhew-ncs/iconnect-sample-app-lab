import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { UserService } from './user.service';
import { UserAssignPopupService } from './user-assign-popup.service';

@Component({
  selector: 'ic-user-assign-roles-dialog',
  templateUrl: './user-assign-roles-dialog.component.html'
})
export class UserAssignRolesDialogComponent implements OnInit {
  isSaving?: boolean;

  subjectId?: string;
  searchedRoles: Resource[] = [];
  chooseedRoles: Resource[] = [];
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
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  assign(): void {
    this.isSaving = true;
    if (this.chooseedRoles != null && this.chooseedRoles.length > 0) {
      this.userService.assignRoles(this.subjectId, this.chooseedRoles).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.backToPrevious();
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
      });
    }
  }

  searchUnAssignRoles(event: any): void {
    this.userService
      .searchUnAssignedRoles({
        subjectId: this.subjectId,
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

  private backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate([this.previousUrl]);
    }, 0);
  }
}

@Component({
  selector: 'ic-user-assign-roles-popup',
  template: ''
})
export class UserAssignRolesPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private userAssignPopupService: UserAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.userAssignPopupService.open(UserAssignRolesDialogComponent as Component, params['id']);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
