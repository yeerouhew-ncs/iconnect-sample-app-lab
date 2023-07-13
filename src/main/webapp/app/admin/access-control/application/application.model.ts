export class Application {
  constructor(
    public id?: number,
    public appId?: string,
    public appCode?: string,
    public appName?: string,
    public appDesc?: string,
    public createdBy?: string,
    public createdDt?: any,
    public updatedBy?: string,
    public updatedDt?: any,
    public resources?: any[]
  ) {}
}
