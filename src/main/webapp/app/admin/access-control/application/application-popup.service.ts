import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Application } from './application.model';
import { ApplicationService } from './application.service';

@Injectable()
export class ApplicationPopupService {
  private ngbModalRef: NgbModalRef | null;

  constructor(private modalService: NgbModal, private router: Router, private applicationService: ApplicationService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      if (this.ngbModalRef) {
        resolve(this.ngbModalRef);
      }
      if (id) {
        this.applicationService.find(id).subscribe(application => {
          this.ngbModalRef = this.applicationModalRef(component, application);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.applicationModalRef(component, new Application());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  applicationModalRef(component: Component, application: Application): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.application = application;
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
