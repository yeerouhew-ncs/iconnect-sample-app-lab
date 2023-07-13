import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { FunctionService } from './function.service';
import { FunctionPopupService } from './function-popup.service';

@Component({
  selector: 'ic-function-delete-dialog',
  templateUrl: './function-delete-dialog.component.html'
})
export class FunctionDeleteDialogComponent {
  func: Resource = new Resource();

  constructor(
    private functionService: FunctionService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: any): void {
    this.functionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'functionListModification',
        content: 'Deleted an function'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-function']);
    }, 0);
  }
}

@Component({
  selector: 'ic-function-delete-popup',
  template: ''
})
export class FunctionDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private functionPopupService: FunctionPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.functionPopupService.open(FunctionDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
