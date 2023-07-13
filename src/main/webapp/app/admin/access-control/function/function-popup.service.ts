import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Resource } from '../resource/resource.model';
import { FunctionService } from './function.service';

@Injectable()
export class FunctionPopupService {
  private ngbModalRef: NgbModalRef | null;

  constructor(private modalService: NgbModal, private router: Router, private functionService: FunctionService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      if (this.ngbModalRef) {
        resolve(this.ngbModalRef);
      }
      if (id) {
        this.functionService.find(id).subscribe(func => {
          this.ngbModalRef = this.functionModalRef(component, func);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.functionModalRef(component, new Resource());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  functionModalRef(component: Component, func: Resource): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.func = func;
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
