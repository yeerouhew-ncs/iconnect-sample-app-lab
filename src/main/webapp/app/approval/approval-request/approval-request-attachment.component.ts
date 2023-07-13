import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { saveAs } from 'file-saver';
import { SERVER_API_URL } from '../../app.constants';
import { PlatformLocation } from '@angular/common';
import { ApprovalRequest, ApprovalRequestService } from '.';

@Component({
  selector: 'ic-approval-request-attachments',
  templateUrl: './approval-request-attachment.component.html'
})
export class ApprovalRequestAttachmentComponent implements OnInit {
  @Input() approvalRequest: ApprovalRequest;
  @Input() mode: string;
  public microServiceURL = SERVER_API_URL + '';
  public url: string;
  constructor(
    private approvalRequestService: ApprovalRequestService,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService,
    private location: PlatformLocation
  ) {}

  ngOnInit(): void {
    this.url = this.microServiceURL + this.location.getBaseHrefFromDOM();
  }

  onUpload(): void {
    this.approvalRequestService.findAttachmentsByRequestId(this.approvalRequest.id).subscribe((response: HttpResponse<any>) => {
      this.approvalRequest.attachments = response.body;
    });
  }

  onBeforeSend(event): void {
    // PrimeNG use XMLHttpRequest instead of Angular http to upload, need to inject Authorization token manually
    const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
    event.xhr.setRequestHeader('Authorization', 'Bearer ' + token);
  }

  deleteAttachment(attachmentId): void {
    this.approvalRequestService.deleteAttachment(this.approvalRequest.id, attachmentId).subscribe(() => {
      this.approvalRequest.attachments = this.approvalRequest.attachments.filter(attachment => attachment.id !== attachmentId);
    });
  }

  downloadAttachment(attachmentId): void {
    this.approvalRequestService.downloadAttachment(this.approvalRequest.id, attachmentId).subscribe((response: HttpResponse<any>) => {
      saveAs(
        new Blob([response.body], { type: 'application/download' }),
        decodeURI(response.headers.get('content-disposition').substring(22) || 'download')
      );
    });
  }
}
