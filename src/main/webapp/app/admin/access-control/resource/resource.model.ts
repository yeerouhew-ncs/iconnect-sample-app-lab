import { Application } from 'app/admin/access-control/application/application.model';

export class Resource {
  constructor(
    public id?: any,
    public resourceId?: any,
    public resourceName?: string,
    public resourceDesc?: string,
    public resourcePath?: string,
    public resourceType?: string,
    public effectiveFrom?: any,
    public effectiveTo?: any,
    public createdBy?: string,
    public createdDt?: any,
    public updatedBy?: string,
    public updatedDt?: any,
    public application?: Application,
    public group?: any,
    public subject?: any,
    public res2RessForParentResId?: any[],
    public res2RessForResourceId?: any[],
    public resourceByResourceId?: any,
    public resourceByParentResId?: any,
    public assignedResources?: any[],
    public assignedRoles?: any[]
  ) {
    this.application = new Application();
  }
}
