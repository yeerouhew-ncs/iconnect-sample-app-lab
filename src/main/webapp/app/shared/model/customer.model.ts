import { IAddress } from 'app/shared/model/address.model';
import { AccountStatus } from 'app/shared/model/enumerations/account-status.model';

export interface ICustomer {
  id?: number;
  name?: string;
  accountStatus?: AccountStatus;
  email?: string;
  telMain?: string;
  description?: string;
  addresses?: IAddress[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public name?: string,
    public accountStatus?: AccountStatus,
    public email?: string,
    public telMain?: string,
    public description?: string,
    public addresses?: IAddress[]
  ) {}
}
