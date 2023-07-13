/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { ResourceEditComponent } from 'app/admin/access-control/resource/resource-edit.component';
import { ResourceService } from 'app/admin/access-control/resource/resource.service';
import { Resource } from 'app/admin/access-control/resource/resource.model';
describe('Component Tests', () => {
  describe('Resource Management Dialog Component', () => {
    let comp: ResourceEditComponent;
    let fixture: ComponentFixture<ResourceEditComponent>;
    let service: ResourceService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ResourceEditComponent],
        providers: [ResourceService]
      })
        .overrideTemplate(ResourceEditComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ResourceEditComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResourceService);
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
          comp.resource = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      ));
    });
    describe('unAssignFunctions', () => {
      it('unAssignFunctions function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignFunctions').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.resourceId = '123';
          comp.selectedFunctions = entityArray;
          comp.unAssignFunctions();
          tick(); // simulate async

          // THEN
          expect(service.unAssignFunctions).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedFunctionListModification', content: 'OK' });
        })
      ));
    });

    describe('select function test', () => {
      it('selectAllFunctions function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.assignedFunctions = entityArray;
          comp.selectAllFunctions(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedFunctions).toEqual(entityArray);
          expect(comp.selectedAllFunctions).toEqual(true);
        })
      ));
      it('selectOneFunction function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.selectedFunctions = null;
          comp.selectOneFunction(true, entity);
          tick(); // simulate async
          // THEN
          expect(comp.selectedFunctions).toEqual(entityArray);
        })
      ));
      it('selectOneFunction function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.selectedFunctions = entityArray;
          comp.selectOneFunction(false, entity);
          tick(); // simulate async
          // THEN
          expect(comp.selectedAllFunctions).toEqual(false);
        })
      ));
    });
  });
});
