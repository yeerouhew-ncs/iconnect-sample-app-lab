export interface IApprover {
  id?: number;
  approverId?: string;
  approverDisplayName?: string;
  approverTitle?: string;
  approverSeq?: number;
  actualApprover?: string;
  approvalStatus?: string;
  taskAssignedDate?: string;
  taskCompletionDate?: string;
}
export class Approver implements IApprover {
  constructor(
    public id?: number,
    public approverId?: string,
    public approverDisplayName?: string,
    public approverTitle?: string,
    public approverSeq?: number,
    public actualApprover?: string,
    public approvalStatus = 'DRAFT',
    public taskAssignedDate?: any,
    public taskCompletionDate?: any
  ) {}
}
