/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { FunctionService } from 'app/admin/access-control/function/function.service';
import { FunctionCreateDialogComponent } from 'app/admin/access-control/function/function-create-dialog.component';
import { Resource } from 'app/admin/access-control/resource/resource.model';
describe('Component Tests', () => {
  describe('Function Management Delete Component', () => {
    let comp: FunctionCreateDialogComponent;
    let fixture: ComponentFixture<FunctionCreateDialogComponent>;
    let service: FunctionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [FunctionCreateDialogComponent],
        providers: [FunctionService]
      })
        .overrideTemplate(FunctionCreateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(FunctionCreateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FunctionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('save function', () => {
      it('Should call create service on save', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          spyOn(service, 'create').and.returnValue(of({}));
          comp.func = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'functionListModification', content: 'OK' });
        })
      ));
    });
  });
});
