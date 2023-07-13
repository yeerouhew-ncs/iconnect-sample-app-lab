/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { RoleEditComponent } from 'app/admin/access-control/role/role-edit.component';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { Group } from 'app/admin/access-control/group/group.model';
import { Resource } from 'app/admin/access-control/resource/resource.model';
import { Subject } from 'app/admin/access-control/user/user.model';
describe('Component Tests', () => {
  describe('Application Management Dialog Component', () => {
    let comp: RoleEditComponent;
    let fixture: ComponentFixture<RoleEditComponent>;
    let service: RoleService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleEditComponent],
        providers: [RoleService]
      })
        .overrideTemplate(RoleEditComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleEditComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
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
          comp.role = entity;
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
          comp.resourceId = '123';
          comp.selectedUsers = entityArray;
          comp.unAssignUsers();
          tick(); // simulate async

          // THEN
          expect(service.unAssignUsers).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedUserListModification', content: 'OK' });
        })
      ));
    });
    describe('unAssignFuncs', () => {
      it('unAssignFuncs function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignFuncs').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.resourceId = '123';
          comp.selectedFuncs = entityArray;
          comp.unAssignFuncs();
          tick(); // simulate async

          // THEN
          expect(service.unAssignFuncs).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedFuncListModification', content: 'OK' });
        })
      ));
    });
    describe('unAssignGroups', () => {
      it('unAssignGroups function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignGroups').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.resourceId = '123';
          comp.selectedGroups = entityArray;
          comp.unAssignGroups();
          tick(); // simulate async

          // THEN
          expect(service.unAssignGroups).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedGroupListModification', content: 'OK' });
        })
      ));
    });
    describe('select function test', () => {
      it('selectAllGroups function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.assignedGroups = entityArray;
          comp.selectAllGroups(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedGroups).toEqual(entityArray);
          expect(comp.selectedAllGroups).toEqual(true);
        })
      ));
      it('selectAllFuncs function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.assignedFuncs = entityArray;
          comp.selectAllFuncs(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedFuncs).toEqual(entityArray);
          expect(comp.selectedAllFuncs).toEqual(true);
        })
      ));
      it('selectOneFunc function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.selectedFuncs = null;
          comp.selectOneFunc(true, entity);
          tick(); // simulate async
          // THEN
          expect(comp.selectedFuncs).toEqual(entityArray);
        })
      ));
      it('selectOneGroup function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.selectedGroups = null;
          comp.selectOneGroup(true, entity);
          tick(); // simulate async
          // THEN
          expect(comp.selectedGroups).toEqual(entityArray);
        })
      ));
    });
  });
});
