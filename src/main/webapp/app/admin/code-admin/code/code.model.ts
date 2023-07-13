import { CodeLocale } from './code-locale.model';
export class Code {
  constructor(
    public id?: string,
    public appId?: string,
    public codeTypePK?: string,
    public codeTypeBZId?: string,
    public codeId?: string,
    public codeDesc?: string,
    public codeDescs?: CodeLocale[],
    public codeSeq?: number,
    public parentCodePk?: string,
    public parentCodeTypePk?: string,
    public status?: string,
    public effectiveDt?: any,
    public expiryDt?: any,
    public locale?: string
  ) {}
}
export class CodeValid {
  constructor(public valid?: boolean, public errorMsg?: string) {}
}
