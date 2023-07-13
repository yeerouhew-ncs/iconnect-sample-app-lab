import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Resource } from '../resource/resource.model';
import { RoleService } from './role.service';

@Injectable()
export class RolePopupService {
  private ngbModalRef: NgbModalRef | null;

  constructor(private modalService: NgbModal, private router: Router, private roleService: RoleService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      if (this.ngbModalRef) {
        resolve(this.ngbModalRef);
      }
      if (id) {
        this.roleService.find(id).subscribe(role => {
          this.ngbModalRef = this.roleModalRef(component, role);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.roleModalRef(component, new Resource());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  roleModalRef(component: Component, role: Resource): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.role = role;
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
