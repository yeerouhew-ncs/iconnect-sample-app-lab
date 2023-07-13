import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subject } from '../user/user.model';
import { ResourceService } from './resource.service';
import { ResourceAssignPopupService } from './resource-assign-popup.service';

@Component({
  selector: 'ic-resource-assign-functions-dialog',
  templateUrl: './resource-assign-functions-dialog.component.html'
})
export class ResourceAssignFunctionsDialogComponent implements OnInit {
  isSaving?: boolean;
  applicationId?: string;
  resourceId?: string;
  searchedFunctions: Subject[] = [];
  chooseedFunctions: Subject[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private resourceService: ResourceService,
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
    if (this.chooseedFunctions != null && this.chooseedFunctions.length > 0) {
      this.resourceService.assignFunctions(this.resourceId, this.chooseedFunctions).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedFunctionListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignFunctions(event: any): void {
    this.resourceService
      .searchUnAssignedFunctions({
        applicationId: this.applicationId,
        resourceId: this.resourceId,
        resourceName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedFunctions = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-resource/' + this.resourceId]);
    }, 0);
  }
}

@Component({
  selector: 'ic-resource-assign-resources-popup',
  template: ''
})
export class ResourceAssignFunctionsPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private resourceAssignPopupService: ResourceAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['applicationId'] && params['resourceId']) {
        this.resourceAssignPopupService.open(
          ResourceAssignFunctionsDialogComponent as Component,
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
