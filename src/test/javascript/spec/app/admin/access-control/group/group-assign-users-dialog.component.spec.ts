import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { GroupService } from 'app/admin/access-control/group/group.service';
import { GroupAssignUsersDialogComponent } from 'app/admin/access-control/group/group-assign-users-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('GroupAssignUsersDialogComponent', () => {
    let comp: GroupAssignUsersDialogComponent;
    let fixture: ComponentFixture<GroupAssignUsersDialogComponent>;
    let service: GroupService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GroupAssignUsersDialogComponent],
        providers: [GroupService]
      })
        .overrideTemplate(GroupAssignUsersDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GroupAssignUsersDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Group assign resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123, '123');
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'assignUsers').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedUsers = entityArray;
          comp.groupId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignUsers).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedUserListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignUsers', () => {
      it('Function searchUnAssignUsers resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedUsers').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          // comp.searchedResources = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignUsers(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedUsers).toHaveBeenCalled();
          expect(comp.searchedUsers).toEqual(entityArray);
        })
      ));
    });
  });
});
