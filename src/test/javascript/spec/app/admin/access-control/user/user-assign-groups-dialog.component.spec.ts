import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { UserService } from 'app/admin/access-control/user/user.service';
import { UserAssignGroupsDialogComponent } from 'app/admin/access-control/user/user-assign-groups-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Resource } from 'app/admin/access-control/resource/resource.model';
import { Group } from 'app/admin/access-control/group/group.model';
describe('Component Tests', () => {
  describe('UserAssignGroupsDialogComponent', () => {
    let comp: UserAssignGroupsDialogComponent;
    let fixture: ComponentFixture<UserAssignGroupsDialogComponent>;
    let service: UserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserAssignGroupsDialogComponent],
        providers: [UserService]
      })
        .overrideTemplate(UserAssignGroupsDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserAssignGroupsDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign role', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123, '123');
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'assignGroups').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedGroups = entityArray;
          comp.subjectId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignGroups).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedGroupListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignGroups', () => {
      it('Function searchUnAssignGroups role', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedGroups').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.searchedGroups = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignGroups(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedGroups).toHaveBeenCalled();
        })
      ));
    });
  });
});
