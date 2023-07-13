import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { throwError } from 'rxjs';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { JhiAlertService } from 'ng-jhipster';
import { UserTokenComponent, UserTokenService } from 'app/admin/access-control/login-control';
import { MockAlertService } from 'src/test/javascript/spec/helpers/mock-alert.service';

describe('Component Tests', () => {
  describe('User Token Component', () => {
    let comp: UserTokenComponent;
    let fixture: ComponentFixture<UserTokenComponent>;
    let service: UserTokenService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserTokenComponent],
        providers: [UserTokenService, { provide: JhiAlertService, useClass: MockAlertService }]
      })
        .overrideTemplate(UserTokenComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserTokenComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserTokenService);
    });

    it('Should alert error message upon error response', inject(
      [JhiAlertService],
      fakeAsync((jhiAlertService: JhiAlertService) => {
        // GIVEN
        spyOn(service, 'query').and.returnValue(throwError({ body: { message: '123321' } }));
        // WHEN
        comp.loadAll();
        tick(); // simulate async

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(jhiAlertService.error).toHaveBeenCalled();
      })
    ));
  });
});
