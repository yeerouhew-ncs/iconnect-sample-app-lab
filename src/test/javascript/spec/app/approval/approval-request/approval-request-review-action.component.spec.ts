import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalRequestReviewActionComponent, ApprovalRequestService, ApprovalRequest, Approver } from 'app/approval/approval-request';

describe('Component Tests', () => {
  describe('Approval request review action Component', () => {
    let comp: ApprovalRequestReviewActionComponent;
    let fixture: ComponentFixture<ApprovalRequestReviewActionComponent>;
    let service: ApprovalRequestService;
    let mockEventManager: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalRequestReviewActionComponent],
        providers: [ApprovalRequestService]
      })
        .overrideTemplate(ApprovalRequestReviewActionComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApprovalRequestReviewActionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApprovalRequestService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
    });

    describe('Action', () => {
      it('Should call ApprovalRequestService submit method', () => {
        // GIVEN
        spyOn(service, 'submitRequest').and.returnValue(of(new HttpResponse()));

        // WHEN
        comp.approvalRequest = new ApprovalRequest();
        comp.approvalRequest.status = 'PENDING_APPROVAL';
        const approver = new Approver();
        approver.approverId = '123';
        approver.approvalStatus = 'PENDING_APPROVAL';
        comp.approvalRequest.approvers = [approver];
        comp.currentAccount = { subjectId: '123' };
        comp.confirmReviewAction('Submit');

        // THEN
        expect(service.submitRequest).toHaveBeenCalled();
        expect(comp.isSaving).toBeFalsy();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({
          name: 'approvalRequestModification',
          content: 'Approval Request State Changed'
        });
      });

      it('Should call ApprovalRequestService Approve method', () => {
        // GIVEN
        spyOn(service, 'approveRequest').and.returnValue(of(new HttpResponse()));

        // WHEN
        comp.approvalRequest = new ApprovalRequest();
        comp.approvalRequest.status = 'PENDING_APPROVAL';
        const approver = new Approver();
        approver.approverId = '123';
        approver.approvalStatus = 'PENDING_APPROVAL';
        comp.approvalRequest.approvers = [approver];
        comp.currentAccount = { subjectId: '123' };
        comp.confirmReviewAction('Approve');

        // THEN
        expect(service.approveRequest).toHaveBeenCalled();
        expect(comp.isSaving).toBeFalsy();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({
          name: 'approvalRequestModification',
          content: 'Approval Request State Changed'
        });
      });

      it('Should call ApprovalRequestService Reject method', () => {
        // GIVEN
        spyOn(service, 'rejectRequest').and.returnValue(of(new HttpResponse()));

        // WHEN
        comp.approvalRequest = new ApprovalRequest();
        comp.approvalRequest.status = 'PENDING_APPROVAL';
        const approver = new Approver();
        approver.approverId = '123';
        approver.approvalStatus = 'PENDING_APPROVAL';
        comp.approvalRequest.approvers = [approver];
        comp.currentAccount = { subjectId: '123' };
        comp.confirmReviewAction('Reject');

        // THEN
        expect(service.rejectRequest).toHaveBeenCalled();
        expect(comp.isSaving).toBeFalsy();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({
          name: 'approvalRequestModification',
          content: 'Approval Request State Changed'
        });
      });

      it('Should call ApprovalRequestService sendBackRequest method', () => {
        // GIVEN
        spyOn(service, 'sendBackRequest').and.returnValue(of(new HttpResponse()));

        // WHEN
        comp.approvalRequest = new ApprovalRequest();
        comp.approvalRequest.status = 'PENDING_APPROVAL';
        const approver = new Approver();
        approver.approverId = '123';
        approver.approvalStatus = 'PENDING_APPROVAL';
        comp.approvalRequest.approvers = [approver];
        comp.currentAccount = { subjectId: '123' };
        comp.confirmReviewAction('Rollback to Applicant');

        // THEN
        expect(service.sendBackRequest).toHaveBeenCalled();
        expect(comp.isSaving).toBeFalsy();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({
          name: 'approvalRequestModification',
          content: 'Approval Request State Changed'
        });
      });

      it('Should call ApprovalRequestService cancelRequest method', () => {
        // GIVEN
        spyOn(service, 'cancelRequest').and.returnValue(of(new HttpResponse()));

        // WHEN
        comp.approvalRequest = new ApprovalRequest();
        comp.approvalRequest.status = 'PENDING_APPROVAL';
        const approver = new Approver();
        approver.approverId = '123';
        approver.approvalStatus = 'PENDING_APPROVAL';
        comp.approvalRequest.approvers = [approver];
        comp.currentAccount = { subjectId: '123' };
        comp.confirmReviewAction('Cancel');

        // THEN
        expect(service.cancelRequest).toHaveBeenCalled();
        expect(comp.isSaving).toBeFalsy();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({
          name: 'approvalRequestModification',
          content: 'Approval Request State Changed'
        });
      });
    });
  });
});
