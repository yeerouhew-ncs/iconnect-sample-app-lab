/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { CodeTypeService } from 'app/admin/code-admin/code-type/code-type.service';
import { CodeTypeComponent } from 'app/admin/code-admin/code-type/code-type.component';
import { CodeType } from 'app/admin/code-admin/code-type/code-type.model';

describe('Component Tests', () => {
  describe('CodeType Management Component', () => {
    let comp: CodeTypeComponent;
    let fixture: ComponentFixture<CodeTypeComponent>;
    let service: CodeTypeService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [CodeTypeComponent],
        providers: [CodeTypeService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(CodeTypeComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CodeTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CodeTypeService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new CodeType('123')],
              headers
            })
          )
        );
        spyOn(service, 'getAppList').and.returnValue(
          of(
            new HttpResponse({
              body: [new CodeType('123')],
              headers
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(service.getAppList).toHaveBeenCalled();
        expect(comp.codeTypes[0]).toEqual(jasmine.objectContaining({ codeTypePk: '123' }));
      });
    });
  });
});
