import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class CodePopupService {
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private router: Router) {
    this.ngbModalRef = null;
  }

  open(component: Component, codeTypePk?: string | any, codeId?: string | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      if (codeTypePk) {
        setTimeout(() => {
          this.ngbModalRef = this.codeModalRef(component, codeTypePk, codeId);
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  codeModalRef(component: Component, codeTypePk: string, codeId: string): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.codeTypePk = codeTypePk;
    modalRef.componentInstance.codeId = codeId;
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
