import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Group } from './group.model';
import { GroupPopupService } from './group-popup.service';
import { GroupService } from './group.service';

@Component({
  selector: 'ic-group-create-dialog',
  templateUrl: './group-create-dialog.component.html'
})
export class GroupCreateDialogComponent implements OnInit {
  group: Group = new Group();
  isSaving?: boolean;
  parentGroups: Group[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private alertService: JhiAlertService,
    private groupService: GroupService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.groupService
      .findAll({
        page: 0,
        size: 1000,
        sort: null
      })
      .subscribe(
        (res: HttpResponse<any>) => {
          this.parentGroups = res.body;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  clear(): void {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.groupService.create(this.group));
  }

  private subscribeToSaveResponse(result: Observable<Group>): void {
    result.subscribe(
      (res: Group) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Group): void {
    this.isSaving = false;
    this.eventManager.broadcast({ name: 'groupListModification', content: 'OK' });
    this.activeModal.dismiss(result);
    this.backToPrevious();
  }

  private onSaveError(error: any): void {
    this.isSaving = false;
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    const dom = document.getElementById('appDialogErrorMsg');
    if (dom) {
      dom.innerHTML = appDialogErrorMsg;
      dom.style.display = 'block';
    }
  }

  private onError(error: any): void {
    this.alertService.error(error.message, null);
  }

  trackParentGroupById(index: number, item: Group): any {
    return item.groupId;
  }

  backToPrevious(): void {
    window.history.back();
  }
}

@Component({
  selector: 'ic-group-popup',
  template: ''
})
export class GroupPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private groupPopupService: GroupPopupService) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(() => {
      this.groupPopupService.open(GroupCreateDialogComponent as Component);
    });
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
