import { ApprovalTemplateData } from './approval-template-data.model';

export const enum MultiInstanceType {
  SEQUENTIAL = 'SEQUENTIAL',
  PARALLEL = 'PARALLEL'
}

export const enum ApprovalBehavior {
  UNANIMOUS_APPROVAL = 'UNANIMOUS_APPROVAL',
  FIRST_APPROVAL = 'FIRST_APPROVAL'
}

export const enum ApproverSelection {
  FIXED = 'FIXED',
  FIXED_STEP = 'FIXED_STEP',
  FLEXIBLE = 'FLEXIBLE'
}

export class ApprovalTemplate {
  constructor(
    public id?: string,
    public requestTypeKey?: string,
    public templateKey?: string,
    public processOwner?: string,
    public multiInstanceType?: MultiInstanceType,
    public approvalBehavior?: ApprovalBehavior,
    public enableRejectAll?: boolean,
    public enableRejectStep?: boolean,
    public enableRejectToApplicant?: boolean,
    public approverSelection?: ApproverSelection,
    public createdBy?: string,
    public createdDt?: any,
    public updatedBy?: string,
    public updatedDt?: any,
    public approvalTemplateData?: ApprovalTemplateData[]
  ) {
    this.approvalBehavior = ApprovalBehavior.UNANIMOUS_APPROVAL;
    this.enableRejectAll = true;
    this.enableRejectStep = false;
    this.enableRejectToApplicant = true;
  }
}
