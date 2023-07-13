/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { ApplicationDialogComponent } from 'app/admin/access-control/application/application-dialog.component';
import { ApplicationService } from 'app/admin/access-control/application/application.service';
import { Application } from 'app/admin/access-control/application/application.model';

describe('Component Tests', () => {
  describe('Application Management Dialog Component', () => {
    let comp: ApplicationDialogComponent;
    let fixture: ComponentFixture<ApplicationDialogComponent>;
    let service: ApplicationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApplicationDialogComponent],
        providers: [ApplicationService]
      })
        .overrideTemplate(ApplicationDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApplicationDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Application(123, '123');
          spyOn(service, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.application = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'applicationListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));

      it('Should call create service on save for new entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Application();
          spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.application = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'applicationListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
