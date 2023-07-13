import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { RoleAssignFuncsDialogComponent } from 'app/admin/access-control/role/role-assign-funcs-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Resource } from 'app/admin/access-control/resource/resource.model';

describe('Component Tests', () => {
  describe('RoleAssignFuncsDialogComponent', () => {
    let comp: RoleAssignFuncsDialogComponent;
    let fixture: ComponentFixture<RoleAssignFuncsDialogComponent>;
    let service: RoleService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleAssignFuncsDialogComponent],
        providers: [RoleService]
      })
        .overrideTemplate(RoleAssignFuncsDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleAssignFuncsDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
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
          spyOn(service, 'assignFuncs').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedFuncs = entityArray;
          comp.resourceId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignFuncs).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedFuncListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignFuncs', () => {
      it('Function searchUnAssignFuncs ', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedFuncs').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          // WHEN
          const event = {};
          comp.searchUnAssignFuncs(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedFuncs).toHaveBeenCalled();
          expect(comp.searchedFuncs).toEqual(entityArray);
        })
      ));
    });
  });
});
