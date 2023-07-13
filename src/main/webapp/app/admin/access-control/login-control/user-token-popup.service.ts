import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserToken } from 'app/admin/access-control/login-control/user-token.model';
import { UserTokenService } from 'app/admin/access-control/login-control/user-token.service';

@Injectable()
export class UserTokenPopupService {
  private ngbModalRef: NgbModalRef | null;

  constructor(private modalService: NgbModal, private router: Router, private userTokenService: UserTokenService) {
    this.ngbModalRef = null;
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>(resolve => {
      if (this.ngbModalRef) {
        resolve(this.ngbModalRef);
      }
      // const isOpen = this.ngbModalRef !== null;
      // if (isOpen) {
      //     resolve(this.ngbModalRef);
      // }
      if (id) {
        this.userTokenService.find(id).subscribe(userToken => {
          this.ngbModalRef = this.userTokenModalRef(component, userToken);
          resolve(this.ngbModalRef);
        });
      } else {
        // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
        setTimeout(() => {
          this.ngbModalRef = this.userTokenModalRef(component, new UserToken());
          resolve(this.ngbModalRef);
        }, 0);
      }
    });
  }

  userTokenModalRef(component: Component, userToken: UserToken): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userToken = userToken;
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
