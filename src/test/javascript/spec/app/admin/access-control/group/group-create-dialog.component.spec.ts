/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { GroupService } from 'app/admin/access-control/group/group.service';
import { GroupCreateDialogComponent } from 'app/admin/access-control/group/group-create-dialog.component';
import { Group } from 'app/admin/access-control/group/group.model';
describe('Component Tests', () => {
  describe('Group Management Create Component', () => {
    let comp: GroupCreateDialogComponent;
    let fixture: ComponentFixture<GroupCreateDialogComponent>;
    let service: GroupService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GroupCreateDialogComponent],
        providers: [GroupService]
      })
        .overrideTemplate(GroupCreateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GroupCreateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('save function', () => {
      it('Should call create service on save', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123);
          spyOn(service, 'create').and.returnValue(of({}));
          comp.group = entity;
          // WHEN
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'groupListModification', content: 'OK' });
        })
      ));
    });
  });
});
