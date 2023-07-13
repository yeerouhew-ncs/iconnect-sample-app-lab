import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Resource } from './resource.model';
import { ResourceService } from './resource.service';
import { ResourcePopupService } from './resource-popup.service';

@Component({
  selector: 'ic-resource-delete-dialog',
  templateUrl: './resource-delete-dialog.component.html'
})
export class ResourceDeleteDialogComponent {
  resource: Resource = new Resource();

  constructor(
    private resourceService: ResourceService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: any): void {
    this.resourceService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'resourceListModification',
        content: 'Deleted an resource'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-resource']);
    }, 0);
  }
}

@Component({
  selector: 'ic-resource-delete-popup',
  template: ''
})
export class ResourceDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private resourcePopupService: ResourcePopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.resourcePopupService.open(ResourceDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
