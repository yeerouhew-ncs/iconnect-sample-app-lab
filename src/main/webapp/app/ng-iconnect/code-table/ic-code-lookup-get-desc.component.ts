import { Component, Input, OnInit } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CodeView, IcCodeService } from '../user-picker/ic-code-service';

@Component({
  selector: 'ic-code-lookup-get-desc',
  template: '<span *ngIf="code" [innerHTML]="code.label | icCodeI18n"></span>'
})
export class IcCodeLookupGetDescComponent implements OnInit {
  code: CodeView;
  currentLanguage: any;

  constructor(private codeService: IcCodeService, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this.currentLanguage = event.lang;
    });
  }

  @Input('icCodeLookupGetDesc')
  set icCodeLookupGetDesc(value: string) {
    const valueAsArray = value.split(',');
    const codeTypeBZId = valueAsArray[0];
    const codeId = valueAsArray[1];
    this.codeService.get(codeTypeBZId, codeId).subscribe((code: CodeView) => {
      this.code = code;
    });
  }
}
