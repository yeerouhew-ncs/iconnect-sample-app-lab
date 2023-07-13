import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { Application } from '../application';
import { Group } from '../group';
import { RoleService } from './role.service';
import { RolePopupService } from './role-popup.service';

@Component({
  selector: 'ic-role-create-dialog',
  templateUrl: './role-create-dialog.component.html'
})
export class RoleCreateDialogComponent implements OnInit {
  role: Resource = new Resource();
  isSaving?: boolean;

  applications: Application[] = [];

  groups: Group[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private roleService: RoleService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.roleService.findAllApplication().subscribe(
      (res: HttpResponse<any>) => {
        this.applications = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  trackApplicationById(index: number, item: Application): any {
    return item.id;
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.roleService.create(this.role));
  }

  lowerCaseRoleId(): void {
    if (this.role.resourceId) {
      this.role.resourceId = this.role.resourceId.toLowerCase();
    }
  }

  private subscribeToSaveResponse(result: Observable<Resource>): void {
    result.subscribe(
      (res: Resource) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Resource): void {
    this.isSaving = false;
    this.eventManager.broadcast({ name: 'roleListModification', content: 'OK' });
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

  private backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-role-popup',
  template: ''
})
export class RolePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private rolePopupService: RolePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(() => {
      this.rolePopupService.open(RoleCreateDialogComponent as Component);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
