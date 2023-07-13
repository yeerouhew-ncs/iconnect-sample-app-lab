import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { CodeTypeService } from './code-type.service';
import { CodeTypePopupService } from './code-type-popup.service';
import { CodeType } from './code-type.model';

@Component({
  selector: 'ic-code-type-delete-dialog',
  templateUrl: './code-type-delete-dialog.component.html'
})
export class CodeTypeDeleteDialogComponent {
  codeType: CodeType;

  constructor(
    private codeTypeService: CodeTypeService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: string): void {
    this.codeTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'codeTypeListModification',
        content: 'Deleted a code type'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/ic-codetype']);
    }, 0);
  }
}

@Component({
  selector: 'ic-code-type-delete-popup',
  template: ''
})
export class CodeTypeDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private codeTypePopupService: CodeTypePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.codeTypePopupService.open(CodeTypeDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
