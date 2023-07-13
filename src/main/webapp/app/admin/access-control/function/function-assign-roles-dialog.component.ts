import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { FunctionService } from './function.service';
import { FunctionAssignPopupService } from './function-assign-popup.service';

@Component({
  selector: 'ic-function-assign-roles-dialog',
  templateUrl: './function-assign-roles-dialog.component.html'
})
export class FunctionAssignRolesDialogComponent implements OnInit {
  isSaving?: boolean;
  applicationId?: string;
  resourceId?: string;
  searchedRoles: Resource[] = [];
  chooseedRoles: Resource[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private functionService: FunctionService,
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
    if (this.chooseedRoles != null && this.chooseedRoles.length > 0) {
      this.functionService.assignRoles(this.resourceId, this.chooseedRoles).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignRoles(event: any): void {
    this.functionService
      .searchUnAssignedRoles({
        applicationId: this.applicationId,
        resourceId: this.resourceId,
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

  private onError(erro: any): any {
    this.alertService.error(erro.message, null);
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-function/' + this.resourceId]);
    }, 0);
  }
}

@Component({
  selector: 'ic-function-assign-roles-popup',
  template: ''
})
export class FunctionAssignRolesPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private functionAssignPopupService: FunctionAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['applicationId'] && params['resourceId']) {
        this.functionAssignPopupService.open(
          FunctionAssignRolesDialogComponent as Component,
          params['applicationId'],
          params['resourceId']
        );
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
