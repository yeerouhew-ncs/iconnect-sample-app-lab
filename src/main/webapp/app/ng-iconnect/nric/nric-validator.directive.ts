// import { Directive, forwardRef } from '@angular/core';
import { Directive, forwardRef } from '@angular/core';
import { Validator, FormControl, NG_VALIDATORS } from '@angular/forms';
import { validateNRIC } from './nric-validate';

const NRIC_VALIDATOR = {
  provide: NG_VALIDATORS,
  // eslint-disable-next-line @typescript-eslint/no-use-before-define
  useExisting: forwardRef(() => NRICValidatorDirective),
  multi: true
};

@Directive({
  selector: '[icNric][formControlName],[icNric][formControl],[icNric][ngModel]',
  providers: [NRIC_VALIDATOR]
})
export class NRICValidatorDirective implements Validator {
  private isValid = false;

  validate(c: FormControl): { [key: string]: any } {
    if (c.value) {
      this.isValid = validateNRIC(c.value);
      if (this.isValid) {
        return null;
      } else {
        return { nric: 'invalid' };
      }
    } else {
      return null;
    }
  }
}
