import { Approver } from './approver.model';
import { Attachment } from './attachment.model';
import { HistoryItem } from './history-item.model';

export class ApprovalRequest {
  constructor(
    public id?: number,
    public key?: string,
    public templateId?: string,
    public approvalBehavior?: string,
    public approverSelection = 'FLEXIBLE',
    public enableRejectAll?: boolean,
    public enableRejectStep?: boolean,
    public enableRejectToApplicant?: boolean,
    public initiator?: string,
    public initiatorDisplayName?: string,
    public summary?: string,
    public status?: string,
    public createdDate?: string,
    public submittedDate?: any,
    public approvers: Approver[] = [],
    public approvalHistoryItems: HistoryItem[] = [],
    public attachments: Attachment[] = []
  ) {}
}
