import { ApprovalRequest } from '../approval-request';

export class GeneralApproval {
  constructor(
    public id?: number,
    public typeKey?: string,
    public templateKey?: string,
    public description?: string,
    public approvalRequest = new ApprovalRequest()
  ) {}
}
