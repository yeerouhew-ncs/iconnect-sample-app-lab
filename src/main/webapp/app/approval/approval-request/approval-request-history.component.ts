import { Component, OnInit, Input } from '@angular/core';
import { ApprovalRequest } from './';

@Component({
  selector: 'ic-approval-request-history',
  templateUrl: './approval-request-history.component.html'
})
export class ApprovalRequestHistoryComponent implements OnInit {
  @Input() approvalRequest: ApprovalRequest;
  @Input() mode: string;
  constructor() {}

  ngOnInit(): void {}
}
