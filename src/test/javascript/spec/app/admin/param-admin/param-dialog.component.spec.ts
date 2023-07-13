import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { IconnectSampleAppLabTestModule } from '../../../test.module';
import { ParamDialogComponent } from 'app/admin/param-admin/param/param-dialog.component';
import { ParamService } from 'app/admin/param-admin/param/param.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { ParamModel } from 'app/admin/param-admin/param/param.model';
describe('ParamDialogComponent Tests', () => {
  describe('ParamDialogComponent', () => {
    let comp: ParamDialogComponent;
    let fixture: ComponentFixture<ParamDialogComponent>;
    let service: ParamService;
    let mockEventManager: any;
    let mockActiveModal: any;
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ParamDialogComponent],
        providers: [ParamService]
      })
        .overrideTemplate(ParamDialogComponent, '')
        .compileComponents();
    }));
    beforeEach(() => {
      fixture = TestBed.createComponent(ParamDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParamService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });
    describe('save function ', () => {
      it('should be call create service ', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new ParamModel();
          spyOn(service, 'create').and.returnValue(of({}));
          comp.param = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
      it('should be call udpate service ', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new ParamModel({ appId: '123' });
          spyOn(service, 'update').and.returnValue(of({}));
          comp.param = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('getAppList', () => {
      it('Should call getAppList service on getAppList', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'getAppList').and.returnValue(of({}));

          // WHEN
          comp.getAppList();
          tick();

          // THEN
          expect(service.getAppList).toHaveBeenCalled();
        })
      ));
    });
  });
});
