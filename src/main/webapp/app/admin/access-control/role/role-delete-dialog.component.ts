import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { RoleService } from './role.service';
import { RolePopupService } from './role-popup.service';

@Component({
  selector: 'ic-role-delete-dialog',
  templateUrl: './role-delete-dialog.component.html'
})
export class RoleDeleteDialogComponent {
  role: Resource = new Resource();

  constructor(
    private roleService: RoleService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: string): void {
    this.roleService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'roleListModification',
        content: 'Deleted an role'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-role']);
    }, 0);
  }
}

@Component({
  selector: 'ic-role-delete-popup',
  template: ''
})
export class RoleDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private rolePopupService: RolePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.rolePopupService.open(RoleDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
