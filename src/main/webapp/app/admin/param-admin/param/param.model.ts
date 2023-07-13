export class ParamModel {
  constructor(
    public id?: {
      appId?: string;
      paramKey?: string;
      rowKey?: string;
    },
    public appId?: any,
    public paramKey?: any,
    public effectiveDate?: any,
    public expireDate?: any,
    public paramDesc?: string,
    public paramType?: string,
    public paramTypeDesc?: string,
    public paramValue?: string,
    public paramMap?: any,
    public paramList?: any,
    public hasId?: boolean
  ) {}
}

export class SearchParamModel {
  constructor(
    public appId?: any,
    public paramKey?: any,
    public paramDesc?: string,
    public effectiveDateAsStr?: any,
    public expireDateAsStr?: any
  ) {}
}
