/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { RoleService } from 'app/admin/access-control/role/role.service';
import { RoleComponent } from 'app/admin/access-control/role/role.component';
import { Application } from 'app/admin/access-control/application/application.model';
import { Resource } from 'app/admin/access-control/resource/resource.model';

describe('Component Tests', () => {
  describe('Role Management Component', () => {
    let comp: RoleComponent;
    let fixture: ComponentFixture<RoleComponent>;
    let service: RoleService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [RoleComponent],
        providers: [RoleService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(RoleComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(RoleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoleService);
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
        expect(comp.roles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
