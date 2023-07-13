import { ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';

import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { PasswordResetInitComponent } from 'app/account/password-reset/init/password-reset-init.component';
import { PasswordResetInitService } from 'app/account/password-reset/init/password-reset-init.service';

describe('Component Tests', () => {
  describe('PasswordResetInitComponent', () => {
    let fixture: ComponentFixture<PasswordResetInitComponent>;
    let comp: PasswordResetInitComponent;

    beforeEach(() => {
      fixture = TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [PasswordResetInitComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PasswordResetInitComponent, '')
        .createComponent(PasswordResetInitComponent);
      comp = fixture.componentInstance;
    });

    /* it('sets focus after the view has been initialized',
            () => {
                const node = {
                    focus(): void {}
                };
                comp.resetAccount = new ElementRef(node);
                spyOn(node, 'focus');

                comp.ngAfterViewInit();

                expect(node.focus).toHaveBeenCalled();
            }
        ); */

    it('notifies of success upon successful requestReset', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(of({}));
      comp.resetAccount = {
        username: 'test',
        newPassword: 'password1',
        newPassword2: 'password1'
      };

      comp.requestReset();

      expect(service.save).toHaveBeenCalledWith({ newPassword: 'password1', newPassword2: 'password1', username: 'test' });
      expect(comp.success);
    }));

    it('no notification of success upon error response', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(
        throwError({
          status: 503,
          data: 'something else'
        })
      );
      comp.resetAccount = {
        username: 'test',
        newPassword: 'password1',
        newPassword2: 'password2'
      };
      comp.requestReset();

      expect(comp.success).toBe(null);
      expect(comp.doNotMatch).toBe('ERROR');
    }));
  });
});
