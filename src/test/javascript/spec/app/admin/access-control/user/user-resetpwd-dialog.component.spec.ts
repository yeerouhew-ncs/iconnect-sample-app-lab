/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UserService } from 'app/admin/access-control/user/user.service';
import { UserResetpwdDialogComponent } from 'app/admin/access-control/user/user-resetpwd-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
describe('Component Tests', () => {
  describe('Function Management Delete Component', () => {
    let comp: UserResetpwdDialogComponent;
    let fixture: ComponentFixture<UserResetpwdDialogComponent>;
    let service: UserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserResetpwdDialogComponent],
        providers: [UserService]
      })
        .overrideTemplate(UserResetpwdDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserResetpwdDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call confirmResetPwd service on confirmResetPwd', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'resetPassword').and.returnValue(of({}));

          // WHEN
          comp.confirmResetPwd('123');
          tick();

          // THEN
          expect(service.resetPassword).toHaveBeenCalledWith('123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
