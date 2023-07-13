import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { FunctionService } from 'app/admin/access-control/function/function.service';
import { FunctionAssignResourcesDialogComponent } from 'app/admin/access-control/function/function-assign-resources-dialog.component';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('FunctuonAssignResourcesDialogComponent', () => {
    let comp: FunctionAssignResourcesDialogComponent;
    let fixture: ComponentFixture<FunctionAssignResourcesDialogComponent>;
    let service: FunctionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [FunctionAssignResourcesDialogComponent],
        providers: [FunctionService]
      })
        .overrideTemplate(FunctionAssignResourcesDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(FunctionAssignResourcesDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FunctionService);
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
          spyOn(service, 'assignResources').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          comp.chooseedResources = entityArray;
          comp.resourceId = '123';
          // WHEN
          comp.assign();
          tick(); // simulate async

          // THEN
          expect(service.assignResources).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedResourceListModification', content: 'OK' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
        })
      ));
    });

    describe('searchUnAssignResources', () => {
      it('Function searchUnAssignResources resources', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'searchUnAssignedResources').and.returnValue(
            of(
              new HttpResponse({
                body: entityArray
              })
            )
          );
          // comp.searchedResources = entityArray;
          // WHEN
          const event = {};
          comp.searchUnAssignResources(event);
          tick(); // simulate async

          // THEN
          expect(service.searchUnAssignedResources).toHaveBeenCalled();
          expect(comp.searchedResources).toEqual(entityArray);
        })
      ));
    });
  });
});
