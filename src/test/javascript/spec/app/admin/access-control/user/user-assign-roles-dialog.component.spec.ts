import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { UserService } from 'app/admin/access-control/user/user.service';
import { UserAssignRolesDialogComponent } from 'app/admin/access-control/user/user-assign-roles-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Resource } from 'app/admin/access-control/resource/resource.model';

describe('Component Tests', () => {
  describe('UserAssignRolesDialogComponent', () => {
    let comp: UserAssignRolesDialogComponent;
    let fixture: ComponentFixture<UserAssignRolesDialogComponent>;
    let service: UserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserAssignRolesDialogComponent],
        providers: [UserService]
      })
        .overrideTemplate(UserAssignRolesDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserAssignRolesDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123, '123');
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
          comp.subjectId = '123';
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
      it('Function searchUnAssignRoles resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedRoles').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          // comp.searchedResources = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignRoles(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedRoles).toHaveBeenCalled();
          expect(comp.searchedRoles).toEqual(entityArray);
        })
      ));
    });
  });
});
