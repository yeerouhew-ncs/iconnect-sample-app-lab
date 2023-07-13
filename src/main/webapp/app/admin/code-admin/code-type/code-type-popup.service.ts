import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CodeTypeService } from './code-type.service';
import { CodeType } from './code-type.model';

@Injectable()
export class CodeTypePopupService {
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private router: Router, private codeTypeService: CodeTypeService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: string | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }

      if (id) {
        this.codeTypeService.find(id).subscribe(codeType => {
          this.ngbModalRef = this.codeTypeModalRef(component, codeType);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.codeTypeModalRef(component, new CodeType());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  codeTypeModalRef(component: Component, codeType: CodeType): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.codeType = codeType;
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
