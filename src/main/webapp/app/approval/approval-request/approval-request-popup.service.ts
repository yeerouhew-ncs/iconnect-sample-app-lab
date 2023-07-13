import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ApprovalRequest } from './approval-request.model';
import { ApprovalRequestService } from './approval-request.service';

@Injectable()
export class ApprovalRequestPopupService {
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private router: Router, private approvalRequestService: ApprovalRequestService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      if (id) {
        this.approvalRequestService.find(id).subscribe((approvalRequestResponse: HttpResponse<ApprovalRequest>) => {
          const approvalRequest: ApprovalRequest = approvalRequestResponse.body;
          this.ngbModalRef = this.approvalRequestModalRef(component, approvalRequest);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.approvalRequestModalRef(component, new ApprovalRequest());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  approvalRequestModalRef(component: Component, approvalRequest: ApprovalRequest): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvalRequest = approvalRequest;
    modalRef.result.then(
      () => {
        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
        this.ngbModalRef = null;
      },
      () => {
        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
        this.ngbModalRef = null;
      }
    );
    return modalRef;
  }
}
