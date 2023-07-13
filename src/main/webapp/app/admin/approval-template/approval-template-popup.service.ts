import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ApprovalTemplateService } from 'app/admin/approval-template/approval-template.service';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';

@Injectable()
export class ApprovalTemplatePopupService {
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private router: Router, private approvalTemplateService: ApprovalTemplateService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }
      if (id) {
        this.approvalTemplateService.find(id).subscribe(param => {
          this.ngbModalRef = this.paramModalRef(component, param);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.paramModalRef(component, new ApprovalTemplate());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  convertDate(entity: any): void {
    if (entity.effectiveDate) {
      entity.effectiveDate = {
        year: entity.effectiveDate.getFullYear(),
        month: entity.effectiveDate.getMonth() + 1,
        day: entity.effectiveDate.getDate()
      };
    }
    if (entity.expireDate) {
      entity.expireDate = {
        year: entity.expireDate.getFullYear(),
        month: entity.expireDate.getMonth() + 1,
        day: entity.expireDate.getDate()
      };
    }
  }

  paramModalRef(component: Component, param: ApprovalTemplate): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvalTemplate = param;
    modalRef.result.then(
      () => {
        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
        this.ngbModalRef = null;
      },
      () => {
        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
        this.ngbModalRef = null;
      }
    );
    return modalRef;
  }
}
