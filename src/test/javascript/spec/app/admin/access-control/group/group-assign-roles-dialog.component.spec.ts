import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { GroupService } from 'app/admin/access-control/group/group.service';
import { GroupAssignRolesDialogComponent } from 'app/admin/access-control/group/group-assign-roles-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('GroupAssignRolesDialogComponent', () => {
    let comp: GroupAssignRolesDialogComponent;
    let fixture: ComponentFixture<GroupAssignRolesDialogComponent>;
    let service: GroupService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GroupAssignRolesDialogComponent],
        providers: [GroupService]
      })
        .overrideTemplate(GroupAssignRolesDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GroupAssignRolesDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Group assign role', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123, '123');
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'assignRoles').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedRoles = entityArray;
          comp.groupId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignRoles).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedRoleListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignRoles', () => {
      it('Function unAssignRoles role', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedRoles').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.searchedRoles = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignRoles(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedRoles).toHaveBeenCalled();
        })
      ));
    });
  });
});
