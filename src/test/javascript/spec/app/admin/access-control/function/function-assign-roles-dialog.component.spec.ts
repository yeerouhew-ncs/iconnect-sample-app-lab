import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { FunctionService } from 'app/admin/access-control/function/function.service';
import { FunctionAssignRolesDialogComponent } from 'app/admin/access-control/function/function-assign-roles-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Resource } from 'app/admin/access-control/resource/resource.model';

describe('Component Tests', () => {
  describe('FunctuonAssignRolesDialogComponent', () => {
    let comp: FunctionAssignRolesDialogComponent;
    let fixture: ComponentFixture<FunctionAssignRolesDialogComponent>;
    let service: FunctionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [FunctionAssignRolesDialogComponent],
        providers: [FunctionService]
      })
        .overrideTemplate(FunctionAssignRolesDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(FunctionAssignRolesDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FunctionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign role', inject(
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
          comp.resourceId = '123';
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

    describe('searchUnAssignedRoles', () => {
      it('Function unAssignRoles role', inject(
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
