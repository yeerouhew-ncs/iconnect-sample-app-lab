export class Group {
  constructor(
    public id?: number,
    public groupId?: string,
    public groupName?: string,
    public groupDesc?: string,
    public leftIndex?: number,
    public rightIndex?: number,
    public createdBy?: string,
    public createdDt?: any,
    public updatedBy?: string,
    public updatedDt?: any,
    public resources?: any[],
    public subject?: any,
    public subject2Groups?: any[],
    public groupParentId?: string
  ) {}
}
