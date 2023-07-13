export class CodeType {
  constructor(
    public codeTypePk?: string,
    public appId?: string,
    public codeTypeId?: string,
    public codeTypeDesc?: string,
    public isExternal?: boolean,
    public codeTypeTable?: string,
    public colCodeTypeId?: string,
    public colCodeId?: string,
    public colCodeDesc?: string,
    public colCodeSeq?: string,
    public colStatus?: string,
    public colEffectiveDate?: string,
    public colExpiryDate?: string,
    public colLocale?: string
  ) {}
}
