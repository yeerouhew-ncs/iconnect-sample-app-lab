import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Group } from './group.model';
import { GroupPopupService } from './group-popup.service';
import { GroupService } from './group.service';

@Component({
  selector: 'ic-group-delete-dialog',
  templateUrl: './group-delete-dialog.component.html'
})
export class GroupDeleteDialogComponent {
  group: Group = new Group();

  constructor(
    private groupService: GroupService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  confirmDelete(id: any): void {
    this.groupService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'groupListModification',
        content: 'Deleted an group'
      });
      this.activeModal.dismiss(true);
      this.backToPrevious();
    });
  }

  backToPrevious(): void {
    setTimeout(() => {
      this.router.navigate(['admin/acm/ic-group']);
    }, 0);
  }
}

@Component({
  selector: 'ic-group-delete-popup',
  template: ''
})
export class GroupDeletePopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private groupPopupService: GroupPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.groupPopupService.open(GroupDeleteDialogComponent as Component, params['id']);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
