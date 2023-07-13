/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { FunctionService } from 'app/admin/access-control/function/function.service';
import { FunctionComponent } from 'app/admin/access-control/function/function.component';
import { Application } from 'app/admin/access-control/application/application.model';
import { Resource } from 'app/admin/access-control/resource/resource.model';

describe('Component Tests', () => {
  describe('Function Management Component', () => {
    let comp: FunctionComponent;
    let fixture: ComponentFixture<FunctionComponent>;
    let service: FunctionService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [FunctionComponent],
        providers: [FunctionService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(FunctionComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(FunctionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FunctionService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new Resource(123)],
              headers
            })
          )
        );
        spyOn(service, 'findAllApplication').and.returnValue(
          of(
            new HttpResponse({
              body: [new Application(123)],
              headers
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(service.findAllApplication).toHaveBeenCalled();
        expect(comp.functions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
