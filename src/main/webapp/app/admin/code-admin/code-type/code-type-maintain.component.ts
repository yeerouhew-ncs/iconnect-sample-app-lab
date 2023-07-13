import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { CodeType } from './code-type.model';
import { CodeTypeService } from './code-type.service';

@Component({
  selector: 'ic-code-type-maintain',
  templateUrl: './code-type-maintain.component.html'
})
export class CodeTypeMaintainComponent implements OnInit, OnDestroy {
  appList: any[];
  subscription: Subscription;
  routeData: any;
  codeType: CodeType;

  constructor(
    private codeTypeService: CodeTypeService,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscription = this.activatedRoute.params.subscribe(params => {
      if (params['id']) {
        this.findCodeType(params['id']);
      } else {
        this.initCodeType();
      }
    });
    this.getAppList();
  }

  ngOnDestroy(): void {}

  getAppList(): void {
    this.codeTypeService.getAppList().subscribe(
      (res: HttpResponse<any>) => {
        this.appList = res.body;
      },
      (res: HttpResponse<any>) => this.onError(res.body)
    );
  }

  findCodeType(id): void {
    this.codeTypeService.find(id).subscribe(codeType => {
      this.codeType = codeType;
    });
  }

  initCodeType(): void {
    this.codeType = {
      codeTypePk: '',
      appId: null,
      codeTypeDesc: '',
      codeTypeId: '',
      codeTypeTable: 'TBL_CODE_INT',
      colCodeTypeId: 'CODETYPE_ID',
      colCodeId: 'CODE_ID',
      colCodeDesc: 'CODE_DESC',
      colCodeSeq: 'CODE_SEQ',
      colStatus: 'STATUS',
      colEffectiveDate: 'EFFECTIVE_DT',
      colExpiryDate: 'EXPIRY_DT',
      colLocale: 'LOCALE'
    };
  }

  save(): void {
    this.subscribeToSaveResponse(
      this.codeType.codeTypePk ? this.codeTypeService.update(this.codeType) : this.codeTypeService.create(this.codeType)
    );
  }

  goToSearchPage(): void {
    this.router.navigate(['/admin/ic-codetype']);
  }

  cancel(): void {
    this.goToSearchPage();
  }

  private subscribeToSaveResponse(result: Observable<CodeType>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(): void {
    this.goToSearchPage();
  }

  private onSaveError(error): void {
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    document.getElementById('appDialogErrorMsg').innerHTML = appDialogErrorMsg;
    document.getElementById('appDialogErrorMsg').style.display = 'block';
  }

  private onError(error): void {
    this.alertService.error(error.message, null, null);
  }
}
