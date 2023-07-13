/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { CodeService } from 'app/admin/code-admin/code/code.service';
import { CodeManageComponent } from 'app/admin/code-admin/code/code-manage.component';
import { Code } from 'app/admin/code-admin/code/code.model';
import { CodeType } from 'app/admin/code-admin/code-type/code-type.model';
import { CodeTypeService } from 'app/admin/code-admin/code-type/code-type.service';

describe('Component Tests', () => {
  describe('CodeManage Management Component', () => {
    let comp: CodeManageComponent;
    let fixture: ComponentFixture<CodeManageComponent>;
    let service: CodeService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [CodeManageComponent],
        providers: [CodeService, CodeTypeService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(CodeManageComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CodeManageComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CodeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new Code('123');
          spyOn(service, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.code = entity;
          comp.codes = [];
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
          const entity = new Code();
          spyOn(service, 'create').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          comp.code = entity;
          comp.codes = [];
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalled();
        })
      ));
    });
    describe('ngOnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const entity = new Code('123');
        const entityArray = [];
        entityArray.push(entity);

        const codeTypeEntity = new CodeType('123', '123');
        const codrTypeEntityArray = [];
        codrTypeEntityArray.push(codeTypeEntity);

        spyOn(service, 'getCodesByCodeTypePk').and.returnValue(
          of(
            new HttpResponse({
              body: entityArray
            })
          )
        );
        spyOn(service, 'getInternalCodeTypesByAppIdAndCodeTypePkNot').and.returnValue(
          of(
            new HttpResponse({
              body: codrTypeEntityArray
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getCodesByCodeTypePk).toHaveBeenCalled();
        expect(service.getInternalCodeTypesByAppIdAndCodeTypePkNot).toHaveBeenCalled();
        expect(comp.parentCodeTypes).toEqual(codrTypeEntityArray);
        expect(comp.codes).toEqual(entityArray);
      });
    });
  });
});
