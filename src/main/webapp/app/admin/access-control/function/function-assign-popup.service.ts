import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class FunctionAssignPopupService {
  private ngbModalRef: NgbModalRef | null;

  constructor(private modalService: NgbModal, private router: Router) {
    this.ngbModalRef = null;
  }

  open(component: Component, applicationId?: number | any, resourceId?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      if (this.ngbModalRef) {
        resolve(this.ngbModalRef);
      }
      if (applicationId && resourceId) {
        setTimeout(() => {
          this.ngbModalRef = this.resourceIdModalRef(component, applicationId, resourceId);
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  resourceIdModalRef(component: Component, applicationId: string, resourceId: string): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationId = applicationId;
    modalRef.componentInstance.resourceId = resourceId;
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
