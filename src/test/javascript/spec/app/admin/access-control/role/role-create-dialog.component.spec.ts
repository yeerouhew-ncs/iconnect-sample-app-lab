/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { RoleCreateDialogComponent } from 'app/admin/access-control/role/role-create-dialog.component';
import { Resource } from 'app/admin/access-control/resource/resource.model';
describe('Component Tests', () => {
  describe('Role Management Delete Component', () => {
    let comp: RoleCreateDialogComponent;
    let fixture: ComponentFixture<RoleCreateDialogComponent>;
    let service: RoleService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleCreateDialogComponent],
        providers: [RoleService]
      })
        .overrideTemplate(RoleCreateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleCreateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
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
          comp.role = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'roleListModification', content: 'OK' });
        })
      ));
    });
  });
});
