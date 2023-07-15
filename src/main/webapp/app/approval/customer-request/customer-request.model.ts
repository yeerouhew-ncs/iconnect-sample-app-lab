import { ApprovalRequest } from 'app/approval/approval-request';

export const enum AccountStatus {
  ACTIVE = 'ACTIVE',
  DEACTIVE = 'DEACTIVE',
  SUSPENDED = 'SUSPENDED',
  EXPIRED = 'EXPIRED'
}

export interface ICustomerRequest {
  approvalRequest?: ApprovalRequest;
  id?: number;
  name?: string;
  accountStatus?: AccountStatus;
  email?: string;
  telMain?: string;
  description?: string;
}

export class CustomerRequest implements ICustomerRequest {
  constructor(
    public id?: number,
    public name?: string,
    public accountStatus?: AccountStatus,
    public email?: string,
    public telMain?: string,
    public description?: string,
    public approvalRequest = new ApprovalRequest()
  ) {}
}
