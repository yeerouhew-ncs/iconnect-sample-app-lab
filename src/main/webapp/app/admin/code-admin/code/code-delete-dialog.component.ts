import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { CodeService } from './code.service';
import { CodePopupService } from './code-popup.service';

@Component({
  selector: 'ic-code-delete-dialog',
  templateUrl: './code-delete-dialog.component.html'
})
export class CodeDeleteDialogComponent {
  codeTypePk: string;
  codeId: string;

  constructor(
    private codeService: CodeService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(codeTypePk: string, codeId: string): void {
    this.codeService.delete({ codeTypePk, codeId }).subscribe(() => {
      this.eventManager.broadcast({
        name: 'codeListModification',
        content: 'Deleted an code'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/ic-code/' + this.codeTypePk]);
    }, 0);
  }
}

@Component({
  selector: 'ic-code-delete-popup',
  template: ''
})
export class CodeDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private codePopupService: CodePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.codePopupService.open(CodeDeleteDialogComponent as Component, params['codeTypePk'], params['codeId']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
