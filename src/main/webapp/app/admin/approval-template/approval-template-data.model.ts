import { ApprovalTemplate } from './approval-template.model';

export class ApprovalTemplateData {
  constructor(
    public id?: string,
    public approverDisplayName?: string,
    public approverTitle?: string,
    public approverId?: string,
    public approvalTemplateId?: string,
    public approverSeq?: number,
    public approveAction?: string,
    public rejectAction?: string,
    public approvalTemplate?: ApprovalTemplate
  ) {}
}
