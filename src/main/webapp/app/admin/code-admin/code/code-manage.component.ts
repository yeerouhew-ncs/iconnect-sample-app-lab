import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { CodeType } from '../code-type/code-type.model';
import { CodeTypeService } from '../code-type/code-type.service';
import { CodeService } from './code.service';
import { Code, CodeValid } from './code.model';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'ic-code-manage',
  templateUrl: './code-manage.component.html'
})
export class CodeManageComponent implements OnInit, OnDestroy {
  eventSubscriber: Subscription;
  subscription: Subscription;
  routeData: any;
  codeType: CodeType;
  codes: Code[];
  code: Code;
  parentCodeTypes: CodeType[];
  parentCodes: Code[];
  statuses: any[];
  effectiveDtDp: any;
  expiryDtDp: any;
  codeIdEn: string;
  codeDescEn: string;
  codeIdCn: string;
  codeId: string;
  codeDescCn: string;
  seqInValidMsg: string;
  isChange: boolean;
  isFirst: boolean;
  isLast: boolean;

  constructor(
    private codeTypeService: CodeTypeService,
    private codeService: CodeService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager,
    private datePipe: DatePipe
  ) {
    this.codeType = new CodeType();
    this.code = new Code();
    this.statuses = [
      { codeId: 'A', codeDesc: 'Active' },
      { codeId: 'I', codeDesc: 'Inactive' }
    ];
    this.seqInValidMsg = 'Code Seq is not valid or is existed';
    this.isFirst = false;
    this.isLast = false;
    this.isChange = false;
  }

  ngOnInit(): void {
    this.subscription = this.activatedRoute.params.subscribe(params => {
      this.findCodeType(params['codeTypePk']);
      this.getCodesByCodeTypePk(params['codeTypePk']);
    });

    this.registerChangeInCodes();
  }

  ngOnDestroy(): void {
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInCodes(): void {
    this.eventSubscriber = this.eventManager.subscribe('codeListModification', () => this.getCodesByCodeTypePk(this.codeType.codeTypePk));
  }

  isFirstOrLast(codeId: string): void {
    if (codeId === this.codes[0].codeId) {
      this.isFirst = true;
      this.isLast = false;
    } else if (codeId === this.codes[this.codes.length - 1].codeId) {
      this.isLast = true;
      this.isFirst = false;
    } else {
      this.isFirst = false;
      this.isLast = false;
    }
  }

  saveChangeSeq(): void {
    let seq = 1;
    this.codes.forEach((item: Code, index: number) => {
      if (index !== 0 && item.codeId === this.codes[index - 1].codeId) {
        item.codeSeq = this.codes[index - 1].codeSeq;
      } else {
        item.codeSeq = seq++;
      }
    });
    this.codeService.changeCodeSeq(this.codes).subscribe(() => {
      this.registerChangeInCodes();
      this.selectCode(this.codeId);
      this.isChange = false;
    });
  }

  changeSeq(type: string): void {
    if (this.codeId === undefined) {
      this.showErrorMsg('There is no Code has been select');
    } else if (this.codes.length <= 1) {
      this.showErrorMsg('The amount of data is too small to change the sort');
    } else {
      this.isChange = true;
      const itemCode: Code[] = new Array<Code>();
      this.codes.forEach((item: Code, index: number) => {
        if (item.codeId === this.codeId) {
          if (itemCode.length === 0) {
            item.codeSeq = this.setSeqByType(type, index);
          } else {
            item.codeSeq = this.codes[index - 1].codeSeq;
          }
          itemCode.push(item);
        } else if (index !== 0 && item.codeId === this.codes[index - 1].codeId) {
          item.codeSeq = this.codes[index - 1].codeSeq;
        }
      });
      this.codes.sort(this.sortBySeq);
      this.isFirstOrLast(this.codeId);
    }
  }

  sort(): void {
    this.codes.forEach((item: Code, index: number) => {
      if (index !== 0 && item.codeId === this.codes[index - 1].codeId) {
        item.codeSeq = this.codes[index - 1].codeSeq;
      }
    });
    this.codes.sort(this.sortBySeq);
  }

  sortBySeq(a: Code, b: Code): any {
    if (a.codeId === b.codeId) {
      return 0;
    } else {
      return a.codeSeq - b.codeSeq;
    }
  }

  setSeqByType(type: string, index: number): any {
    switch (type) {
      case 'top':
        for (let i = 0; i < this.codes.length; i++) {
          if (this.codes[i].codeId === this.codes[0].codeId && this.codes[i].codeSeq != null) {
            return this.codes[i].codeSeq - 0.5;
          }
        }
        break;
      case 'up':
        // eslint-disable-next-line no-case-declarations
        let seq: number;
        this.codes.forEach((item: Code) => {
          if (item.codeId === this.codes[index - 1].codeId && item.codeSeq != null) {
            seq = item.codeSeq - 0.5;
          }
        });
        return seq;
      case 'down':
        for (let i = index + 1; i < this.codes.length; i++) {
          if (this.codes[index].codeId !== this.codes[i].codeId && this.codes[i].codeSeq != null) {
            return this.codes[i].codeSeq + 0.5;
          }
        }
        break;
      case 'bottom':
        for (let i = this.codes.length - 1; i >= 0; i--) {
          if (this.codes[i].codeId === this.codes[this.codes.length - 1].codeId && this.codes[i].codeSeq != null) {
            return this.codes[i].codeSeq + 0.5;
          }
        }
        break;
    }
  }

  findCodeType(codeTypePk): void {
    this.codeTypeService.find(codeTypePk).subscribe(codeType => {
      this.codeType = codeType;
      this.getParentCodeTypes();
    });
  }

  getCodesByCodeTypePk(codeTypePk): void {
    this.codeService.getCodesByCodeTypePk({ codeTypePk, isExternal: false }).subscribe((data: any) => {
      this.codes = data.body;
      this.sort();
      this.init();
    });
  }

  getParentCodeTypes(): void {
    const appId = this.codeType.appId ? this.codeType.appId : 'NA';
    this.codeService.getInternalCodeTypesByAppIdAndCodeTypePkNot({ appId, codeTypePk: this.codeType.codeTypePk }).subscribe((data: any) => {
      this.parentCodeTypes = data.body;
    });
  }

  getParentCodes(): void {
    this.codeService.getCodesByCodeTypePk({ codeTypePk: this.code.parentCodeTypePk, isExternal: false }).subscribe((data: any) => {
      this.parentCodes = data.body;
    });
  }

  selectCode(codeId): void {
    if (codeId) {
      this.init();
      this.codeId = codeId;
      this.codeService.getCodesByCodeTypeIdAndCodeId({ codeTypePk: this.codeType.codeTypePk, codeId }).subscribe((data: any) => {
        const codes = data.body;
        if (codes) {
          this.code = codes[0];

          if (this.code.effectiveDt) {
            this.code.effectiveDt = this.datePipe.transform(this.code.effectiveDt, 'yyyy-MM-dd');
          }
          if (this.code.expiryDt) {
            this.code.expiryDt = this.datePipe.transform(this.code.expiryDt, 'yyyy-MM-dd');
          }

          for (let i = 0; i < codes.length; i++) {
            if (codes[i].locale === 'en') {
              this.codeIdEn = codes[i].id;
              this.codeDescEn = codes[i].codeDesc;
            }
            if (codes[i].locale === 'cn') {
              this.codeIdCn = codes[i].id;
              this.codeDescCn = codes[i].codeDesc;
            }
          }
          this.getParentCodeTypes();
          this.getParentCodes();
        }
        this.isFirstOrLast(codeId);
      });
    }
  }

  init(): void {
    this.code = new Code();
    this.codeIdEn = null;
    this.codeDescEn = null;
    this.codeIdCn = null;
    this.codeDescCn = null;
    this.getParentCodeTypes();
    this.getParentCodes();
  }

  save(): void {
    const validResult = this.isValid(this.code);
    if (validResult.valid) {
      this.subscribeToSaveResponse(this.code.id ? this.updateCode() : this.createCode());
    } else {
      this.showErrorMsg(validResult.errorMsg);
    }
  }

  goToSearchPage(): void {
    this.router.navigate(['/admin/ic-codetype']);
  }

  cancel(): void {
    this.goToSearchPage();
  }

  private createCode(): any {
    this.code.codeTypePK = this.codeType.codeTypePk;
    this.code.codeDescs = [];

    if (!this.codeDescEn) {
      this.codeDescEn = this.code.codeId;
    }
    if (this.codeDescEn) {
      this.code.codeDescs.push({ locale: 'en', codeDesc: this.codeDescEn });
    }
    if (this.codeDescCn) {
      this.code.codeDescs.push({ locale: 'cn', codeDesc: this.codeDescCn });
    }

    return this.codeService.create(this.code);
  }

  private updateCode(): any {
    this.code.codeDescs = [];
    if (!this.codeDescEn) {
      this.codeDescEn = this.code.codeId;
    }
    this.code.codeDescs.push({ id: this.codeIdEn, locale: 'en', codeDesc: this.codeDescEn });
    this.code.codeDescs.push({ id: this.codeIdCn, locale: 'cn', codeDesc: this.codeDescCn });

    return this.codeService.update(this.code);
  }

  private subscribeToSaveResponse(result: Observable<Code>): void {
    result.subscribe(
      (res: Code) => this.onSaveSuccess(res),
      (res: HttpResponse<any>) => this.onSaveError(res)
    );
  }

  private onSaveSuccess(result: Code): void {
    this.getCodesByCodeTypePk(this.codeType.codeTypePk);
    this.selectCode(result.codeId);
  }

  private onSaveError(error): void {
    const appDialogErrorMsg = error.headers.get('X-IconnectSampleAppLabApp-params');
    document.getElementById('appDialogErrorMsg').innerHTML = appDialogErrorMsg;
    document.getElementById('appDialogErrorMsg').style.display = 'block';

    window.scrollTo(0, 0);
  }
  private isValid(newCode: Code): any {
    const isValidResult = new CodeValid();
    isValidResult.valid = true;
    const errorMsg = this.seqInValidMsg;
    if (newCode.codeSeq <= 0) {
      isValidResult.valid = false;
      isValidResult.errorMsg = errorMsg;
      return isValidResult;
    }
    this.codes.forEach((code: Code) => {
      if (code.codeSeq === newCode.codeSeq && newCode.codeId !== code.codeId) {
        isValidResult.valid = false;
        isValidResult.errorMsg = errorMsg;
      }
    });
    return isValidResult;
  }

  private showErrorMsg(errorMsg: string): void {
    document.getElementById('appDialogErrorMsg').innerHTML = errorMsg;
    document.getElementById('appDialogErrorMsg').style.display = 'block';
    window.setTimeout(function(): void {
      document.getElementById('appDialogErrorMsg').style.display = 'none';
    }, 3000);
    window.scrollTo(0, 0);
  }
}
