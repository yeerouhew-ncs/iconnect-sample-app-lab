import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ParamService } from './param.service';
import { ParamModel } from './param.model';
import { ParamPopupService } from './param-popup.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'ic-param-dialog',
  templateUrl: './param-dialog.component.html',
  styles: [
    `
      .errMsgs {
        margin-left: auto;
      }
    `
  ]
})
export class ParamDialogComponent implements OnInit, OnDestroy {
  appList: any[];
  routeData: any;
  param: ParamModel;
  paramTypes: any;
  effectiveDateDp: any;
  expireDateDp: any;
  errorBody: any;

  constructor(
    private paramService: ParamService,
    private alertService: JhiAlertService,
    private eventManager: JhiEventManager,
    public activeModal: NgbActiveModal,
    private router: Router
  ) {}

  ngOnInit(): any {
    this.initParam();
    this.getAppList();
  }

  getAppList(): any {
    this.paramService.getAppList().subscribe((data: any[]) => {
      this.appList = data;
    });
  }

  initParam(): any {
    this.paramTypes = this.paramService.getParamTypes();

    if (!this.param.paramList) {
      this.param.paramList = [{ value: '' }];
    }

    if (!this.param.paramMap) {
      this.param.paramMap = [{ id: this.paramService.newGuid() }];
    }
  }

  ngOnDestroy(): any {}

  save(): any {
    if (this.param.id) {
      this.subscribeToSaveResponse(this.paramService.update(this.param));
    } else {
      this.subscribeToSaveResponse(this.paramService.create(this.param));
    }
  }

  clear(): any {
    this.activeModal.dismiss('cancel');
    this.backToPrevious();
  }

  private subscribeToSaveResponse(result: Observable<ParamModel>): any {
    result.subscribe(
      (res: ParamModel) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: ParamModel): any {
    this.eventManager.broadcast({ name: 'paramListModification', content: 'OK' });
    this.activeModal.dismiss(result);
    this.backToPrevious();
  }

  private onSaveError(error): any {
    try {
      this.errorBody = error.body;
    } catch (exception) {
      error.message = error.text();
    }
    this.onError(error);
  }

  addNewListItem(): any {
    const listItem = { value: '' };
    this.param.paramList.push(listItem);
  }

  removeListItem(i): any {
    this.param.paramList.splice(i, 1);
  }

  addMapItem(): any {
    const mapItem = { id: this.paramService.newGuid() };
    this.param.paramMap.push(mapItem);
  }

  removeMapItem(i): any {
    this.param.paramMap.splice(i, 1);
  }

  private onError(error): any {
    this.alertService.error(error.message, null, null);
  }

  private backToPrevious(): any {
    window.history.back();
  }
}

@Component({
  selector: 'ic-param-popup',
  template: ''
})
export class ParamPopupComponent implements OnInit, OnDestroy {
  routeSub: any;

  constructor(private route: ActivatedRoute, private paramPopupService: ParamPopupService) {}

  ngOnInit(): any {
    this.routeSub = this.route.params.subscribe(params => {
      if (params['appId'] && params['paramKey']) {
        this.paramPopupService.open(ParamDialogComponent as Component, params['appId'], params['paramKey']);
      } else {
        this.paramPopupService.open(ParamDialogComponent as Component);
      }
    });
  }

  ngOnDestroy(): any {
    this.routeSub.unsubscribe();
  }
}
