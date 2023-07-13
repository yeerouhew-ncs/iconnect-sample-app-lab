import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { ResourceService } from './resource.service';
import { Group } from '../group/group.model';
import { Application } from '../application/application.model';

@Component({
  selector: 'ic-resource-edit',
  templateUrl: './resource-edit.component.html'
})
export class ResourceEditComponent implements OnInit, OnDestroy {
  currentAccount: any;
  error: any;
  success: any;
  eventSubscriber?: Subscription;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  reverse: any;

  isSaving?: boolean;
  resourceId?: any;
  resource: Resource = new Resource();

  assignedFunctions: Resource[] = [];
  selectedAllFunctions?: boolean;
  selectedFunctions: Resource[] = [];

  applications: Application[] = [];
  resourceTypes: any[];

  subscription?: Subscription;

  constructor(
    private resourceService: ResourceService,
    private alertService: JhiAlertService,
    private route: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {
    this.resourceTypes = [
      { codeId: 'URI', codeDesc: 'URI' },
      { codeId: 'WEBURI', codeDesc: 'WEBURI' }
    ];
  }

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      this.resourceId = params['id'];
    });

    this.resourceService.findAllApplication().subscribe(
      (res: HttpResponse<any>) => {
        this.applications = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );

    // get resource object
    this.load(this.resourceId);

    // register assign changed
    this.registerChangeInAssignedFunctions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  load(id: any): void {
    this.resourceService.find(id).subscribe(resource => {
      this.resource = resource;
      // get assigned resources and roles
      this.getAllAssignedFunctions();
    });
  }

  getAllAssignedFunctions(): void {
    this.selectedAllFunctions = false;
    this.selectedFunctions = [];
    this.assignedFunctions = [];
    if (this.resource.res2RessForResourceId) {
      for (let i = 0; i < this.resource.res2RessForResourceId.length; i++) {
        const resource: Resource = this.resource.res2RessForResourceId[i];
        if (resource.resourceByParentResId) {
          this.assignedFunctions.push(resource.resourceByParentResId);
        }
      }
    }
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.resourceService.update(this.resource));
  }

  selectAllFunctions(checked: any): void {
    this.selectedFunctions = [];
    this.selectedAllFunctions = checked;
    if (checked) {
      for (let i = 0; i < this.assignedFunctions.length; i++) {
        this.selectedFunctions.push(this.assignedFunctions[i]);
      }
    }
  }

  selectOneFunction(checked: any, resource: any): void {
    if (checked) {
      if (this.selectedFunctions == null) {
        this.selectedFunctions = [];
      }
      this.selectedFunctions.push(resource);
    } else {
      const index = this.selectedFunctions.indexOf(resource);
      if (index >= 0) {
        this.selectedFunctions.splice(index, 1);
      }
      if (this.selectedFunctions === null || this.selectedFunctions.length === 0) {
        this.selectedAllFunctions = false;
      }
    }
  }

  assignedFunctionsLength(): any {
    return this.assignedFunctions ? this.assignedFunctions.length : 0;
  }

  unAssignFunctions(): void {
    if (this.selectedFunctions != null && this.selectedFunctions.length > 0) {
      this.resourceService.unAssignFunctions(this.resourceId, this.selectedFunctions).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedFunctionListModification', content: 'OK' });
      });
    }
  }

  registerChangeInAssignedFunctions(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedFunctionListModification', () => this.load(this.resourceId));
  }

  private subscribeToSaveResponse(result: Observable<Group>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
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
}
