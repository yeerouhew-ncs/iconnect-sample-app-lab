import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ResourceService } from 'app/admin/access-control/resource/resource.service';
import { ResourceAssignFunctionsDialogComponent } from 'app/admin/access-control/resource/resource-assign-functions-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('FunctuonAssignResourcesDialogComponent', () => {
    let comp: ResourceAssignFunctionsDialogComponent;
    let fixture: ComponentFixture<ResourceAssignFunctionsDialogComponent>;
    let service: ResourceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ResourceAssignFunctionsDialogComponent],
        providers: [ResourceService]
      })
        .overrideTemplate(ResourceAssignFunctionsDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ResourceAssignFunctionsDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('assign', () => {
      it('Function assign resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123, '123');
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'assignFunctions').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedFunctions = entityArray;
          comp.resourceId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignFunctions).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedFunctionListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignFunctions', () => {
      it('Function searchUnAssignFunctions resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedFunctions').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          // comp.searchedResources = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignFunctions(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedFunctions).toHaveBeenCalled();
          expect(comp.searchedFunctions).toEqual(entityArray);
        })
      ));
    });
  });
});
