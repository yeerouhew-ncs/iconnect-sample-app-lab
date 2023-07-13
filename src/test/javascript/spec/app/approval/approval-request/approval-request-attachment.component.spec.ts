import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalRequestAttachmentComponent, ApprovalRequestService, ApprovalRequest } from 'app/approval/approval-request';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Component Tests', () => {
  describe('Approval request attachment Component', () => {
    let comp: ApprovalRequestAttachmentComponent;
    let fixture: ComponentFixture<ApprovalRequestAttachmentComponent>;
    let service: ApprovalRequestService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalRequestAttachmentComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalRequestService]
      })
        .overrideTemplate(ApprovalRequestAttachmentComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalRequestAttachmentComponent);
      comp = fixture.componentInstance;
      fixture.detectChanges();
      service = fixture.debugElement.injector.get(ApprovalRequestService);
    }));

    it('Should call ApprovalRequestService findAttachmentsByRequestId Method', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'findAttachmentsByRequestId').and.returnValue(of({ data: '222' }));

        comp.approvalRequest = new ApprovalRequest();
        // WHEN
        comp.onUpload();
        tick();

        // THEN
        expect(service.findAttachmentsByRequestId).toHaveBeenCalled();
      })
    ));
  });
});
