import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from './resource.model';
import { Application } from '../application';
import { ResourceService } from './resource.service';
import { ResourcePopupService } from './resource-popup.service';

@Component({
  selector: 'ic-resource-create-dialog',
  templateUrl: './resource-create-dialog.component.html'
})
export class ResourceCreateDialogComponent implements OnInit {
  resource: Resource = new Resource();
  isSaving?: boolean;
  applications: Application[] = [];
  resourceTypes: any[];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private resourceService: ResourceService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {
    this.resourceTypes = [
      { codeId: 'URI', codeDesc: 'URI' },
      { codeId: 'WEBURI', codeDesc: 'WEBURI' }
    ];
  }

  ngOnInit(): void {
    this.isSaving = false;
    this.resourceService.findAllApplication().subscribe(
      (res: HttpResponse<any>) => {
        this.applications = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.resourceService.create(this.resource));
  }

  private subscribeToSaveResponse(result: Observable<Resource>): void {
    result.subscribe(
      (res: Resource) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Resource): void {
    this.isSaving = false;
    this.eventManager.broadcast({ name: 'resourceListModification', content: 'OK' });
    this.activeModal.dismiss(result);
    this.backToPrevious();
  }

  private onSaveError(error: any): void {
    this.isSaving = false;
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    const dom = document.getElementById('appDialogErrorMsg');
    if (dom) {
      dom.innerHTML = appDialogErrorMsg;
      dom.style.display = 'block';
    }
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-resource-popup',
  template: ''
})
export class ResourcePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private resourcePopupService: ResourcePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(() => {
      this.resourcePopupService.open(ResourceCreateDialogComponent as Component);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
