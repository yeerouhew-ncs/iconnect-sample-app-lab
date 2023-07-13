import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Application } from './application.model';
import { ApplicationPopupService } from './application-popup.service';
import { ApplicationService } from './application.service';

@Component({
  selector: 'ic-application-dialog',
  templateUrl: './application-dialog.component.html'
})
export class ApplicationDialogComponent implements OnInit {
  application: Application = new Application();
  isSaving?: boolean;

  constructor(
    public activeModal: NgbActiveModal,
    private applicationService: ApplicationService,
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

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(
      this.application.appId ? this.applicationService.update(this.application) : this.applicationService.create(this.application)
    );
  }

  private subscribeToSaveResponse(result: Observable<Application>): void {
    result.subscribe(
      (res: Application) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Application): void {
    this.eventManager.broadcast({ name: 'applicationListModification', content: 'OK' });
    this.isSaving = false;
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

  backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-application-popup',
  template: ''
})
export class ApplicationPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private applicationPopupService: ApplicationPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['id']) {
        this.applicationPopupService.open(ApplicationDialogComponent as Component, params['id']);
      } else {
        this.applicationPopupService.open(ApplicationDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
