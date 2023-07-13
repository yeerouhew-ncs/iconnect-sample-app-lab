import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { FunctionService } from './function.service';
import { Group } from '../group/group.model';

@Component({
  selector: 'ic-function-edit',
  templateUrl: './function-edit.component.html'
})
export class FunctionEditComponent implements OnInit, OnDestroy {
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
  func: Resource = new Resource();

  assignedResources: Resource[] = [];
  assignedRoles: Resource[] = [];

  selectedAllResources?: boolean;
  selectedResources: Resource[] = [];

  selectedAllRoles?: boolean;
  selectedRoles: Resource[] = [];

  subscription?: Subscription;

  constructor(private functionService: FunctionService, private route: ActivatedRoute, private eventManager: JhiEventManager) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.subscription = this.route.params.subscribe(params => {
      this.resourceId = params['id'];
    });

    // get function object
    this.load(this.resourceId);

    // register assign changed
    this.registerChangeInAssignedResources();
    this.registerChangeInAssignedRoles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  load(id: any): void {
    this.functionService.find(id).subscribe(func => {
      this.func = func;

      // get assigned resources and roles
      this.getAllAssignedResources();
      this.getAllAssignedRoles();
    });
  }

  getAllAssignedResources(): void {
    this.selectedAllResources = false;
    this.selectedResources = [];
    this.assignedResources = [];
    if (this.func.res2RessForParentResId) {
      for (let i = 0; i < this.func.res2RessForParentResId.length; i++) {
        const resource: Resource = this.func.res2RessForParentResId[i];
        if (resource.resourceByParentResId) {
          this.assignedResources.push(resource.resourceByParentResId);
        }
      }
    }
  }

  getAllAssignedRoles(): void {
    this.selectedAllRoles = false;
    this.selectedRoles = [];
    this.assignedRoles = [];
    if (this.func.res2RessForResourceId) {
      for (let i = 0; i < this.func.res2RessForResourceId.length; i++) {
        const role: Resource = this.func.res2RessForResourceId[i];
        if (role.resourceByParentResId) {
          this.assignedRoles.push(role.resourceByParentResId);
        }
      }
    }
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.functionService.update(this.func));
  }

  selectAllResources(checked: any): void {
    this.selectedResources = [];
    this.selectedAllResources = checked;
    if (checked) {
      for (let i = 0; i < this.assignedResources.length; i++) {
        this.selectedResources.push(this.assignedResources[i]);
      }
    }
  }

  selectOneResource(checked: any, resource: any): void {
    if (checked) {
      if (this.selectedResources == null) {
        this.selectedResources = [];
      }
      this.selectedResources.push(resource);
    } else {
      const index = this.selectedResources.indexOf(resource);
      if (index >= 0) {
        this.selectedResources.splice(index, 1);
      }
      if (this.selectedResources === null || this.selectedResources.length === 0) {
        this.selectedAllResources = false;
      }
    }
  }

  selectAllRoles(checked: any): void {
    this.selectedRoles = [];
    this.selectedAllRoles = checked;
    if (checked) {
      for (let i = 0; i < this.assignedRoles.length; i++) {
        this.selectedRoles.push(this.assignedRoles[i]);
      }
    }
  }

  selectOneRole(checked: any, role: any): void {
    if (checked) {
      if (this.selectedRoles == null) {
        this.selectedRoles = [];
      }
      this.selectedRoles.push(role);
    } else {
      const index = this.selectedRoles.indexOf(role);
      if (index >= 0) {
        this.selectedRoles.splice(index, 1);
      }
      if (this.selectedRoles === null || this.selectedRoles.length === 0) {
        this.selectedAllRoles = false;
      }
    }
  }

  assignedResourcesLength(): any {
    return this.assignedResources ? this.assignedResources.length : 0;
  }

  assignedRolesLength(): any {
    return this.assignedRoles ? this.assignedRoles.length : 0;
  }

  unAssignResources(): void {
    if (this.selectedResources != null && this.selectedResources.length > 0) {
      this.functionService.unAssignResources(this.resourceId, this.selectedResources).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedResourceListModification', content: 'OK' });
      });
    }
  }

  unAssignRoles(): void {
    if (this.selectedRoles != null && this.selectedRoles.length > 0) {
      this.functionService.unAssignRoles(this.resourceId, this.selectedRoles).subscribe(() => {
        this.eventManager.broadcast({ name: 'assignedRoleListModification', content: 'OK' });
      });
    }
  }

  registerChangeInAssignedResources(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedResourceListModification', () => this.load(this.resourceId));
  }

  registerChangeInAssignedRoles(): void {
    this.eventSubscriber = this.eventManager.subscribe('assignedRoleListModification', () => this.load(this.resourceId));
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
}
