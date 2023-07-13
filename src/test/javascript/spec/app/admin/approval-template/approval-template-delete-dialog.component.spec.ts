import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalTemplateDeleteDialogComponent, ApprovalTemplateService } from 'app/admin/approval-template';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Component Tests', () => {
  describe('Approval Template delte dialog Component', () => {
    let comp: ApprovalTemplateDeleteDialogComponent;
    let fixture: ComponentFixture<ApprovalTemplateDeleteDialogComponent>;
    let service: ApprovalTemplateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalTemplateDeleteDialogComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalTemplateService]
      })
        .overrideTemplate(ApprovalTemplateDeleteDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalTemplateDeleteDialogComponent);
      comp = fixture.componentInstance;
      fixture.detectChanges();
      service = fixture.debugElement.injector.get(ApprovalTemplateService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    }));

    it('Should move up data when moveup method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(service, 'delete').and.returnValue(of(new HttpResponse()));

        // WHEN

        comp.confirmDelete('123');
        tick();
        // THEN
        expect(service.delete).toHaveBeenCalledWith('123');
        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({
          name: 'approvalTemplateListModification',
          content: 'Deleted a approvalTemplate.'
        });
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      })
    ));
  });
});
