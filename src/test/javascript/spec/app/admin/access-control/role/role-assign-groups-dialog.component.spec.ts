import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { RoleAssignGroupsDialogComponent } from 'app/admin/access-control/role/role-assign-groups-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Group } from 'app/admin/access-control/group/group.model';
describe('Component Tests', () => {
  describe('FunctuonAssignRolesDialogComponent', () => {
    let comp: RoleAssignGroupsDialogComponent;
    let fixture: ComponentFixture<RoleAssignGroupsDialogComponent>;
    let service: RoleService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleAssignGroupsDialogComponent],
        providers: [RoleService]
      })
        .overrideTemplate(RoleAssignGroupsDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleAssignGroupsDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign group', inject(
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
          comp.resourceId = '123';
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
      it('Function searchUnAssignGroups user', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Group(123);
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
