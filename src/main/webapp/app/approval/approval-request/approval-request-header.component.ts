import { Component, OnInit, Input } from '@angular/core';

import { ApprovalRequest } from './approval-request.model';

@Component({
  selector: 'ic-approval-request-header',
  templateUrl: './approval-request-header.component.html'
})
export class ApprovalRequestHeaderComponent implements OnInit {
  @Input() approvalRequest: ApprovalRequest;
  @Input() formPath: string;
  @Input() formId: string;
  constructor() {}

  ngOnInit(): void {}
}
