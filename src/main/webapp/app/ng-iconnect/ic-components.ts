import { IcCodeI18nPipe } from './i18n';
import { NRICValidatorDirective } from './nric';
import { IcCodeLookupGetDescComponent } from './code-table';
import { IcCodeService, IcUserPickerService } from './user-picker';

export const IC_PIPES = [IcCodeI18nPipe];

export const IC_DIRECTIVES = [NRICValidatorDirective];

export const IC_COMPONENTS = [IcCodeLookupGetDescComponent];

export const IC_SERVICES = [IcCodeService, IcUserPickerService];
