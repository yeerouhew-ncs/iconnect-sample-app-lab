import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { UserTokenDialogComponent, UserTokenService } from 'app/admin/access-control/login-control';

describe('Component Tests', () => {
  describe('User token Dialog Component', () => {
    let comp: UserTokenDialogComponent;
    let fixture: ComponentFixture<UserTokenDialogComponent>;
    let service: UserTokenService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserTokenDialogComponent],
        providers: [UserTokenService]
      })
        .overrideTemplate(UserTokenDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserTokenDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserTokenService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    it('Should call user token service on delete method', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'delete').and.returnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(3);
        tick(); // simulate async

        // THEN
        expect(service.delete).toHaveBeenCalled();

        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'userTokenListModification', content: 'Logout an User' });
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      })
    ));
  });
});
