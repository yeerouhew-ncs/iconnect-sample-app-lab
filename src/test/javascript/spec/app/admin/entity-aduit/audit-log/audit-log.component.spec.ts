import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { AuditLogComponent } from 'app/admin/entity-audit/audit-log/audit-log.component';
import { MockActivatedRoute } from 'src/test/javascript/spec/helpers/mock-route.service';
import { ActivatedRoute } from '@angular/router';
import { AuditLogService } from 'app/admin/entity-audit/audit-log/audit-log.service';

describe('Component Tests', () => {
  describe('Audit log Component', () => {
    let comp: AuditLogComponent;
    let fixture: ComponentFixture<AuditLogComponent>;
    let service: AuditLogService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [AuditLogComponent],
        providers: [
          AuditLogService,
          {
            provide: ActivatedRoute,
            useValue: { ...new MockActivatedRoute(888), snapshot: { params: {} } }
          }
        ]
      })
        .overrideTemplate(AuditLogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(AuditLogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuditLogService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const headers = new HttpHeaders().append('link', 'link;link');
        spyOn(service, 'query').and.returnValue(
          of(
            new HttpResponse({
              body: [{ revisionType: '222', revisionTypeDesc: '333' }],
              headers
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.query).toHaveBeenCalled();
        expect(comp.auditLogs[0]).toEqual(jasmine.objectContaining({ revisionType: '222' }));
      });
    });
  });
});
