/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiPaginationUtil } from 'ng-jhipster';
import { PaginationConfig } from 'app/blocks/config/uib-pagination.config';
import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { GroupService } from 'app/admin/access-control/group/group.service';
import { GroupComponent } from 'app/admin/access-control/group/group.component';
import { Group } from 'app/admin/access-control/group/group.model';

describe('Component Tests', () => {
  describe('Group Management Component', () => {
    let comp: GroupComponent;
    let fixture: ComponentFixture<GroupComponent>;
    let service: GroupService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GroupComponent],
        providers: [GroupService, JhiPaginationUtil, PaginationConfig, NgbPaginationConfig]
      })
        .overrideTemplate(GroupComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GroupComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new Group(123)],
              headers
            })
          )
        );
        // WHEN
        comp.ngOnInit();
        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(comp.groups[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
