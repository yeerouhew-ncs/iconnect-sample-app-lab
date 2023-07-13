import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ParamService } from './param.service';
import { ParamModel } from './param.model';
import { DatePipe } from '@angular/common';

@Injectable()
export class ParamPopupService {
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private router: Router, private paramService: ParamService, private datePipe: DatePipe) {
    this.ngbModalRef = null;
  }

  open(component: Component, appId?: number | any, paramKey?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      const isOpen = this.ngbModalRef !== null;
      if (isOpen) {
        resolve(this.ngbModalRef);
      }
      if (appId && paramKey) {
        this.paramService.find(appId, paramKey).subscribe(param => {
          if (param) {
            this.paramService.getParamTypes().forEach(paramType => {
              if (param.paramType === paramType.codeId) {
                param.paramTypeDesc = paramType.label;
              }
            });
            this.convertDate(param);
          }
          this.ngbModalRef = this.paramModalRef(component, param);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.paramModalRef(component, new ParamModel());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  convertDate(entity: any): any {
    if (entity.effectiveDate) {
      entity.effectiveDate = this.datePipe.transform(entity.effectiveDate, 'yyyy-MM-dd');
    }
    if (entity.expireDate) {
      entity.expireDate = this.datePipe.transform(entity.expireDate, 'yyyy-MM-dd');
    }
  }

  paramModalRef(component: Component, param: ParamModel): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.param = param;
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
