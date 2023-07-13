import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { Application } from '../application';
import { Group } from '../group';
import { Subject } from '../user';
import { FunctionService } from './function.service';
import { FunctionPopupService } from './function-popup.service';

@Component({
  selector: 'ic-function-create-dialog',
  templateUrl: './function-create-dialog.component.html'
})
export class FunctionCreateDialogComponent implements OnInit {
  func: Resource = new Resource();
  isSaving?: boolean;
  applications: Application[] = [];
  groups: Group[] = [];
  subjects: Subject[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private functionService: FunctionService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.functionService.findAllApplication().subscribe(
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
    this.subscribeToSaveResponse(this.functionService.create(this.func));
  }

  private subscribeToSaveResponse(result: Observable<Resource>): void {
    result.subscribe(
      (res: Resource) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Resource): void {
    this.isSaving = false;
    this.eventManager.broadcast({ name: 'functionListModification', content: 'OK' });
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

  trackApplicationById(index: number, item: Application): any {
    return item.id;
  }

  trackGroupById(index: number, item: Group): any {
    return item.id;
  }

  trackSubjectById(index: number, item: Subject): any {
    return item.id;
  }

  backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-function-popup',
  template: ''
})
export class FunctionPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private functionPopupService: FunctionPopupService) {}

  ngOnInit(): any {
    this.routeSub = this.route.params.subscribe(() => {
      this.functionPopupService.open(FunctionCreateDialogComponent as Component);
    });
  }

  ngOnDestroy(): any {
    this.routeSub.unsubscribe();
  }
}
