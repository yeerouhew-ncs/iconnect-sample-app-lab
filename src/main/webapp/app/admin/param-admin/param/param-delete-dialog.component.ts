import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ParamService } from './param.service';
import { ParamModel } from './param.model';
import { ParamPopupService } from './param-popup.service';

@Component({
  selector: 'ic-param-delete-dialog',
  templateUrl: './param-delete-dialog.component.html'
})
export class ParamDeleteDialogComponent {
  param: ParamModel;

  constructor(
    private paramService: ParamService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(appId: string, paramKey: string): void {
    this.paramService.delete(appId, paramKey).subscribe(() => {
      this.eventManager.broadcast({
        name: 'paramListModification',
        content: 'Deleted a parameter.'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/ic-param']);
    }, 0);
  }
}

@Component({
  selector: 'ic-param-delete-popup',
  template: ''
})
export class ParamDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private paramPopupService: ParamPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.paramPopupService.open(ParamDeleteDialogComponent as Component, params['appId'], params['paramKey']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
