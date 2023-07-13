import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalRequestListComponent, ApprovalRequestService, ApprovalRequest } from 'app/approval/approval-request';

describe('Component Tests', () => {
  describe('Approval request list Component', () => {
    let comp: ApprovalRequestListComponent;
    let fixture: ComponentFixture<ApprovalRequestListComponent>;
    let service: ApprovalRequestService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalRequestListComponent],
        providers: [ApprovalRequestService]
      })
        .overrideTemplate(ApprovalRequestListComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApprovalRequestListComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApprovalRequestService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [new ApprovalRequest(123)],
              headers
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(comp.approvalRequests[0]).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
