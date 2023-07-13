import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { GeneralApprovalUpdateComponent, GeneralApprovalService } from 'app/approval/general-approval';
import { ApprovalRequestService, Approver } from 'app/approval/approval-request';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ApprovalTemplateService } from 'app/admin/approval-template';
import { MockAlertService } from '../../../helpers/mock-alert.service';

describe('Component Tests', () => {
  describe('Entity with all types request Component', () => {
    let comp: GeneralApprovalUpdateComponent;
    let fixture: ComponentFixture<GeneralApprovalUpdateComponent>;
    let generalService: GeneralApprovalService;
    let approvalService: ApprovalRequestService;
    let jhiService: JhiAlertService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GeneralApprovalUpdateComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [
          GeneralApprovalService,
          ApprovalRequestService,
          ApprovalTemplateService,
          { provide: JhiAlertService, useClass: MockAlertService }
        ]
      })
        .overrideTemplate(GeneralApprovalUpdateComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(GeneralApprovalUpdateComponent);
      comp = fixture.componentInstance;
      fixture.detectChanges();
      generalService = fixture.debugElement.injector.get(GeneralApprovalService);
      approvalService = fixture.debugElement.injector.get(ApprovalRequestService);
      jhiService = fixture.debugElement.injector.get(JhiAlertService);
    }));

    it('Should call find Method', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(generalService, 'find').and.returnValue(
          of(
            new HttpResponse({
              body: {
                approvalRequest: {
                  approvers: []
                }
              }
            })
          )
        );
        spyOn(approvalService, 'getApprovers').and.returnValue(
          of({
            data: []
          })
        );

        // WHEN
        comp.load(123);
        tick();

        // THEN
        expect(generalService.find).toHaveBeenCalledWith(123);
        expect(approvalService.getApprovers).toBeCalled();
      })
    ));

    describe('Save method', () => {
      it('Should call customerRequestService create method', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(generalService, 'create').and.returnValue(
            of(
              new HttpResponse({
                body: {}
              })
            )
          );
          // WHEN
          comp.generalApproval.approvalRequest.approvers = [new Approver()];
          comp.generalApproval.approvalRequest.approvers[0].approverId = '123';
          comp.save();
          tick();

          // THEN
          expect(generalService.create).toHaveBeenCalled();
        })
      ));

      it('Should call customerRequestService update method', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(generalService, 'update').and.returnValue(
            of(
              new HttpResponse({
                body: {}
              })
            )
          );
          // WHEN
          comp.generalApproval.approvalRequest.approvers = [new Approver()];
          comp.generalApproval.approvalRequest.approvers[0].approverId = '123';
          comp.generalApproval.id = 2;
          comp.save();
          tick();

          // THEN
          expect(generalService.update).toHaveBeenCalled();
        })
      ));

      describe('error handling', () => {
        it('Should pop up no approver selected error message', inject(
          [],
          fakeAsync(() => {
            // GIVEN
            spyOn(jhiService, 'addAlert');
            // WHEN
            comp.save();
            tick();

            // THEN
            expect(jhiService.addAlert).toHaveBeenCalledWith(
              { type: 'danger', msg: 'approval.approvalRequest.errorMsg.noApproversSelected' },
              []
            );
          })
        ));

        it('Should pop up invalid approver error message', inject(
          [],
          fakeAsync(() => {
            // GIVEN
            spyOn(jhiService, 'addAlert');
            // WHEN
            comp.generalApproval.approvalRequest.approvers = [new Approver()];
            comp.save();
            tick();

            // THEN
            expect(jhiService.addAlert).toHaveBeenCalledWith(
              { type: 'danger', msg: 'approval.approvalRequest.errorMsg.invalidApprovers' },
              []
            );
          })
        ));
      });
    });
  });
});
