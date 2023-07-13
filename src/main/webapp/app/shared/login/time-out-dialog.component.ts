import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'ic-time-out-dialog',
  templateUrl: './time-out-dialog.component.html'
})
export class TimeOutDialogComponent {
  constructor(public activeModal: NgbActiveModal, private router: Router) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.router.navigate(['']);
  }
}
