import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subject } from '../user/user.model';
import { FunctionService } from './function.service';
import { FunctionAssignPopupService } from './function-assign-popup.service';

@Component({
  selector: 'ic-function-assign-resources-dialog',
  templateUrl: './function-assign-resources-dialog.component.html'
})
export class FunctionAssignResourcesDialogComponent implements OnInit {
  isSaving?: boolean;
  applicationId?: string;
  resourceId?: string;
  searchedResources: Subject[] = [];
  chooseedResources: Subject[] = [];

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
    if (this.chooseedResources != null && this.chooseedResources.length > 0) {
      this.functionService.assignResources(this.resourceId, this.chooseedResources).subscribe(() => {
        this.activeModal.dismiss('cancel');
        this.eventManager.broadcast({ name: 'assignedResourceListModification', content: 'OK' });
        this.backToPrevious();
      });
    }
  }

  searchUnAssignResources(event: any): void {
    this.functionService
      .searchUnAssignedResources({
        applicationId: this.applicationId,
        resourceId: this.resourceId,
        resourceName: event.query,
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.searchedResources = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-function/' + this.resourceId]);
    }, 0);
  }
}

@Component({
  selector: 'ic-function-assign-resources-popup',
  template: ''
})
export class FunctionAssignResourcesPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private functionAssignPopupService: FunctionAssignPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['applicationId'] && params['resourceId']) {
        this.functionAssignPopupService.open(
          FunctionAssignResourcesDialogComponent as Component,
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
