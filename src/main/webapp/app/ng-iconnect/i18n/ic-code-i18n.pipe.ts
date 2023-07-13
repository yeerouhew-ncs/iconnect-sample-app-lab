import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({ name: 'icCodeI18n' })
export class IcCodeI18nPipe implements PipeTransform {
  private currentLanguage: string;
  private defaultLabel: string;
  constructor(private translateService: TranslateService) {}

  transform(label: any): string {
    this.currentLanguage = this.translateService.currentLang;
    return this.getLabelByLanguage(label, this.currentLanguage);
  }

  getLabelByLanguage(label: any, language: string): any {
    if (label) {
      let item;
      for (item in label) {
        if (Object.prototype.hasOwnProperty.call(label, item)) {
          if (!this.defaultLabel) {
            // get the first value for default label if there is no language match with the data
            this.defaultLabel = label[item];
          }
          if (item.toLowerCase().indexOf(language) !== -1) {
            return label[item];
          }
        } else {
          return label;
        }
      }
    }
    return this.defaultLabel;
  }
}
