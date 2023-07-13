import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { RoleService } from './role.service';
import { RoleAssignPopupService } from './role-assign-popup.service';

@Component({
  selector: 'ic-role-assign-funcs-dialog',
  templateUrl: './role-assign-funcs-dialog.component.html'
})
export class RoleAssignFuncsDialogComponent implements OnInit {
  isSaving?: boolean;
  resourceId?: string;
  searchedFuncs: Resource[] = [];
  chooseedFuncs: Resource[] = [];

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
    if (this.chooseedFuncs != null && this.chooseedFuncs.length > 0) {
      this.roleService.assignFuncs(this.resourceId, this.chooseedFuncs).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedFuncListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignFuncs(event: any): void {
    this.roleService
      .searchUnAssignedFuncs({
        resourceId: this.resourceId,
        roleName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedFuncs = res.body;
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
  selector: 'ic-role-assign-funcs-popup',
  template: ''
})
export class RoleAssignFuncsPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private roleAssignPopupService: RoleAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.roleAssignPopupService.open(RoleAssignFuncsDialogComponent as Component, params['id']);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
