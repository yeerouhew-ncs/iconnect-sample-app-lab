import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalTemplateDialogComponent, ApprovalTemplateService } from 'app/admin/approval-template';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';

describe('Component Tests', () => {
  describe('Approval Template dialog Component', () => {
    let comp: ApprovalTemplateDialogComponent;
    let fixture: ComponentFixture<ApprovalTemplateDialogComponent>;
    let service: ApprovalTemplateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalTemplateDialogComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalTemplateService]
      })
        .overrideTemplate(ApprovalTemplateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalTemplateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApprovalTemplateService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    }));

    it('Should call ApprovalTemplateService create method', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'create').and.returnValue(of(new ApprovalTemplate()));

        // WHEN
        comp.save();
        tick();
        // THEN
        expect(service.create).toHaveBeenCalled();
        expect(mockEventManager.broadcastSpy).toBeCalledWith({ name: 'approvalTemplateListModification', content: 'OK' });
        expect(mockActiveModal.dismissSpy).toBeCalled();
      })
    ));
  });
});
