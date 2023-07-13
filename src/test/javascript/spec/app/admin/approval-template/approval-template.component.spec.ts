import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalTemplateComponent, ApprovalTemplateService } from 'app/admin/approval-template';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { ActivatedRoute } from '@angular/router';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';

describe('Component Tests', () => {
  describe('Approval Template Component', () => {
    let comp: ApprovalTemplateComponent;
    let fixture: ComponentFixture<ApprovalTemplateComponent>;
    let service: ApprovalTemplateService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalTemplateComponent],
        providers: [
          ApprovalTemplateService,
          {
            provide: ActivatedRoute,
            useValue: { ...new MockActivatedRoute(888), snapshot: { params: {} } }
          }
        ]
      })
        .overrideTemplate(ApprovalTemplateComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApprovalTemplateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApprovalTemplateService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new ApprovalTemplate('123')],
              headers
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(comp.approvalTemplates[0]).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
