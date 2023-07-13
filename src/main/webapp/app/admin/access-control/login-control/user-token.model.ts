export class UserToken {
  constructor(
    public id?: number,
    public loginId?: string,
    public content?: string,
    public expiryDate?: any,
    public createdDate?: any,
    public tokenType?: string,
    public tokenName?: string
  ) {}
}
