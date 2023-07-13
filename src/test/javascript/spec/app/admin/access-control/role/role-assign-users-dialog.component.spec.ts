import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { RoleAssignUsersDialogComponent } from 'app/admin/access-control/role/role-assign-users-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('FunctuonAssignRolesDialogComponent', () => {
    let comp: RoleAssignUsersDialogComponent;
    let fixture: ComponentFixture<RoleAssignUsersDialogComponent>;
    let service: RoleService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleAssignUsersDialogComponent],
        providers: [RoleService]
      })
        .overrideTemplate(RoleAssignUsersDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleAssignUsersDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign user', inject(
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
          comp.resourceId = '123';
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
      it('Function searchUnAssignUsers user', inject(
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
          comp.searchedUsers = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignUsers(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedUsers).toHaveBeenCalled();
        })
      ));
    });
  });
});
