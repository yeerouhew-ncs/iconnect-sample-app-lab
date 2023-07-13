/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { ApplicationService } from 'app/admin/access-control/application/application.service';
import { ApplicationComponent } from 'app/admin/access-control/application/application.component';
import { Application } from 'app/admin/access-control/application/application.model';

describe('Component Tests', () => {
  describe('Application Management Component', () => {
    let comp: ApplicationComponent;
    let fixture: ComponentFixture<ApplicationComponent>;
    let service: ApplicationService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApplicationComponent],
        providers: [ApplicationService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(ApplicationComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApplicationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
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
        expect(comp.applications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
