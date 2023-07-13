/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { FunctionEditComponent } from 'app/admin/access-control/function/function-edit.component';
import { FunctionService } from 'app/admin/access-control/function/function.service';
import { Resource } from 'app/admin/access-control/resource/resource.model';
describe('Component Tests', () => {
  describe('Function Management Dialog Component', () => {
    let comp: FunctionEditComponent;
    let fixture: ComponentFixture<FunctionEditComponent>;
    let service: FunctionService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [FunctionEditComponent],
        providers: [FunctionService]
      })
        .overrideTemplate(FunctionEditComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(FunctionEditComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FunctionService);
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
          comp.func = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      ));
    });
    describe('unAssignResources', () => {
      it('unAssignResources function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'unAssignResources').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.resourceId = '123';
          comp.selectedResources = entityArray;
          comp.unAssignResources();
          tick(); // simulate async

          // THEN
          expect(service.unAssignResources).toHaveBeenCalledWith('123', entityArray);
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'assignedResourceListModification', content: 'OK' });
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
          comp.resourceId = '123';
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
      it('selectAllResources function test', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Resource(123);
          const entityArray = [];
          entityArray.push(entity);
          // WHEN
          comp.resourceId = '123';
          comp.assignedResources = entityArray;
          comp.selectAllResources(true);
          tick(); // simulate async
          // THEN
          expect(comp.selectedResources).toEqual(entityArray);
          expect(comp.selectedAllResources).toEqual(true);
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
