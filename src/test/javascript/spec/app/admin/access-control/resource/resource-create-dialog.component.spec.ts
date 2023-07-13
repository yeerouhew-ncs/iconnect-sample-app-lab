/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { ResourceService } from 'app/admin/access-control/resource/resource.service';
import { ResourceCreateDialogComponent } from 'app/admin/access-control/resource/resource-create-dialog.component';
import { Resource } from 'app/admin/access-control/resource/resource.model';
describe('Component Tests', () => {
  describe('Resource Management Delete Component', () => {
    let comp: ResourceCreateDialogComponent;
    let fixture: ComponentFixture<ResourceCreateDialogComponent>;
    let service: ResourceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ResourceCreateDialogComponent],
        providers: [ResourceService]
      })
        .overrideTemplate(ResourceCreateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ResourceCreateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceService);
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
          comp.resource = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'resourceListModification', content: 'OK' });
        })
      ));
    });
  });
});
