/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { UserService } from 'app/admin/access-control/user/user.service';
import { UserComponent } from 'app/admin/access-control/user/user.component';
import { Subject } from 'app/admin/access-control/user/user.model';

describe('Component Tests', () => {
  describe('User Management Component', () => {
    let comp: UserComponent;
    let fixture: ComponentFixture<UserComponent>;
    let service: UserService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserComponent],
        providers: [UserService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(UserComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new Subject(123)],
              headers
            })
          )
        );
        // WHEN
        comp.ngOnInit();
        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(comp.users[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
