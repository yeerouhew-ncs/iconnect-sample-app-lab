import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalTemplateDetailComponent, ApprovalTemplateService } from 'app/admin/approval-template';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';

describe('Component Tests', () => {
  describe('Approval Template delte dialog Component', () => {
    let comp: ApprovalTemplateDetailComponent;
    let fixture: ComponentFixture<ApprovalTemplateDetailComponent>;
    let service: ApprovalTemplateService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalTemplateDetailComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalTemplateService, { provide: ActivatedRoute, useValue: new MockActivatedRoute({ id: '456' }) }]
      })
        .overrideTemplate(ApprovalTemplateDetailComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalTemplateDetailComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApprovalTemplateService);
    }));

    it('Should move up data when moveup method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of());
        spyOn(comp, 'load').and.callThrough();
        // WHEN
        comp.ngOnInit();
        tick();
        // THEN
        expect(comp.load).toHaveBeenCalledWith('456');
        expect(service.find).toHaveBeenCalledWith('456');
      })
    ));
  });
});
