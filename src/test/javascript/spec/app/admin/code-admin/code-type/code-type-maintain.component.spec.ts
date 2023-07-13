/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { CodeTypeMaintainComponent } from 'app/admin/code-admin/code-type/code-type-maintain.component';
import { CodeTypeService } from 'app/admin/code-admin/code-type/code-type.service';
import { CodeType } from 'app/admin/code-admin/code-type/code-type.model';

describe('Component Tests', () => {
  describe('CodeType MainTain Management Dialog Component', () => {
    let comp: CodeTypeMaintainComponent;
    let fixture: ComponentFixture<CodeTypeMaintainComponent>;
    let service: CodeTypeService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [CodeTypeMaintainComponent],
        providers: [CodeTypeService]
      })
        .overrideTemplate(CodeTypeMaintainComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CodeTypeMaintainComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CodeTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new CodeType('123');
          spyOn(service, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.codeType = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
        })
      ));
      it('Should call create service on save for entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new CodeType();
          spyOn(service, 'create').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.codeType = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
        })
      ));
    });
    describe('ngOnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        spyOn(service, 'getAppList').and.returnValue(
          of(
            new HttpResponse({
              body: [new CodeType('123')]
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getAppList).toHaveBeenCalled();
      });
      it('findCodeType when id is confirm', () => {
        // GIVEN
        const entity = new CodeType('123');
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'find').and.returnValue(
          of(
            new HttpResponse({
              headers,
              body: entity
            })
          )
        );
        // WHEN
        comp.findCodeType('123');

        // THEN
        expect(service.find).toHaveBeenCalled();
      });
    });
  });
});
