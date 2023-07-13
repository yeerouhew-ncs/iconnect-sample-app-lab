import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalRequestDeleteDialogComponent, ApprovalRequestService } from 'app/approval/approval-request';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Component Tests', () => {
  describe('Approval request delte dialog Component', () => {
    let comp: ApprovalRequestDeleteDialogComponent;
    let fixture: ComponentFixture<ApprovalRequestDeleteDialogComponent>;
    let service: ApprovalRequestService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalRequestDeleteDialogComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalRequestService]
      })
        .overrideTemplate(ApprovalRequestDeleteDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalRequestDeleteDialogComponent);
      comp = fixture.componentInstance;
      fixture.detectChanges();
      service = fixture.debugElement.injector.get(ApprovalRequestService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    }));

    it('Should move up data when moveup method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'delete').and.returnValue(of(new HttpResponse()));

        // WHEN

        comp.confirmDelete(123);
        tick();
        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
          name: 'approvalRequestDeleted',
          content: 123
        });
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      })
    ));
  });
});
