/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { UserEditComponent } from 'app/admin/access-control/user/user-edit.component';
import { UserService } from 'app/admin/access-control/user/user.service';
import { Group } from 'app/admin/access-control/group/group.model';
import { Resource } from 'app/admin/access-control/resource/resource.model';
import { Subject } from 'app/admin/access-control/user/user.model';
import { SubjectLogin } from 'app/admin/access-control/user/user-login.model';
describe('Component Tests', () => {
  describe('User Management Dialog Component', () => {
    let comp: UserEditComponent;
    let fixture: ComponentFixture<UserEditComponent>;
    let service: UserService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserEditComponent],
        providers: [UserService]
      })
        .overrideTemplate(UserEditComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserEditComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const userLogin = new SubjectLogin(123);
          entity.subjectLogins = [userLogin];

          spyOn(service, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          spyOn(service, 'find').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.user = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
          expect(service.find).toHaveBeenCalled();
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
          comp.subjectId = '123';
          comp.selectedGroups = entityArray;
          comp.unAssignGroups();
          tick(); // simulate async

          // THEN
          expect(service.unAssignGroups).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedGroupListModification', content: 'OK' });
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
          comp.subjectId = '123';
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
      it('selectAllGroups function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
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
