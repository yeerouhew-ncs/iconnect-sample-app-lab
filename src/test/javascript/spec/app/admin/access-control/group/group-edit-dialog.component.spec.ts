/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { GroupEditComponent } from 'app/admin/access-control/group/group-edit.component';
import { GroupService } from 'app/admin/access-control/group/group.service';
import { Resource } from 'app/admin/access-control/resource/resource.model';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('Group Management Dialog Component', () => {
    let comp: GroupEditComponent;
    let fixture: ComponentFixture<GroupEditComponent>;
    let service: GroupService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GroupEditComponent],
        providers: [GroupService]
      })
        .overrideTemplate(GroupEditComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GroupEditComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          spyOn(service, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.group = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      ));
    });
    describe('unAssignUsers', () => {
      it('unAssignUsers function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignUsers').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.groupId = '123';
          comp.selectedUsers = entityArray;
          comp.unAssignUsers();
          tick(); // simulate async

          // THEN
          expect(service.unAssignUsers).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedUserListModification', content: 'OK' });
        })
      ));
    });
    describe('unAssignRoles', () => {
      it('unAssignRoles function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignRoles').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.groupId = '123';
          comp.selectedRoles = entityArray;
          comp.unAssignRoles();
          tick(); // simulate async

          // THEN
          expect(service.unAssignRoles).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedRoleListModification', content: 'OK' });
        })
      ));
    });

    describe('select function test', () => {
      it('selectAllUsers function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.assignedUsers = entityArray;
          comp.selectAllUsers(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedUsers).toEqual(entityArray);
          expect(comp.selectedAllUsers).toEqual(true);
        })
      ));
      it('selectAllRoles function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.assignedRoles = entityArray;
          comp.selectAllRoles(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedRoles).toEqual(entityArray);
          expect(comp.selectedAllRoles).toEqual(true);
        })
      ));
    });
  });
});
