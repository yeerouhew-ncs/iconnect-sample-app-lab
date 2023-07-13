export class Subject {
  constructor(
    public id?: number,
    public subjectId?: string,
    public firstName?: string,
    public lastName?: string,
    public fullName?: string,
    public email?: string,
    public phoneNum?: string,
    public effectiveDt?: any,
    public expiryDt?: any,
    public status?: string,
    public logicalDel?: number,
    public lastLoginDt?: any,
    public lastLoginIp?: string,
    public createdBy?: string,
    public createdDt?: any,
    public updatedBy?: string,
    public updatedDt?: any,
    public resources?: any[],
    public groups?: any[],
    public subjectLogins?: any[]
  ) {}
}
