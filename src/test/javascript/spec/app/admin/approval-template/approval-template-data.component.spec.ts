import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalTemplateDataComponent, ApprovalTemplateService, ApprovalTemplateDataService } from 'app/admin/approval-template';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { IcUserPickerService } from 'app/ng-iconnect';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';

describe('Component Tests', () => {
  describe('Approval Template data Component', () => {
    let comp: ApprovalTemplateDataComponent;
    let fixture: ComponentFixture<ApprovalTemplateDataComponent>;
    let templateService: ApprovalTemplateService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalTemplateDataComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [ApprovalTemplateService, ApprovalTemplateDataService, IcUserPickerService]
      })
        .overrideTemplate(ApprovalTemplateDataComponent, '')
        .compileComponents();
    }));

    beforeEach(async(() => {
      fixture = TestBed.createComponent(ApprovalTemplateDataComponent);
      comp = fixture.componentInstance;
      fixture.detectChanges();
      templateService = fixture.debugElement.injector.get(ApprovalTemplateService);
    }));

    describe('move up and move down', () => {
      it('Should move up data when moveup method called', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(comp, 'moveUp').and.callThrough();

          // WHEN
          comp.approvalTemplateDatas = [
            {
              approverId: '1',
              id: '1',
              approverSeq: 1
            },
            {
              approverId: '2',
              id: '2',
              approverSeq: 2
            },
            {
              approverId: '3',
              id: '3',
              approverSeq: 3
            }
          ];

          comp.moveUp(2);
          tick();
          // THEN
          expect(comp.moveUp).toHaveBeenCalledWith(2);
          expect(comp.approvalTemplateDatas).toStrictEqual([
            {
              approverId: '2',
              id: '2',
              approverSeq: 1
            },
            {
              approverId: '1',
              id: '1',
              approverSeq: 2
            },
            {
              approverId: '3',
              id: '3',
              approverSeq: 3
            }
          ]);
        })
      ));

      it('Should move down data when moveDown method called', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(comp, 'moveDown').and.callThrough();

          // WHEN
          comp.approvalTemplateDatas = [
            {
              approverId: '1',
              id: '1',
              approverSeq: 1
            },
            {
              approverId: '2',
              id: '2',
              approverSeq: 2
            },
            {
              approverId: '3',
              id: '3',
              approverSeq: 3
            }
          ];
          comp.moveDown(2);
          tick();

          // THEN
          expect(comp.moveDown).toHaveBeenCalledWith(2);
          expect(comp.approvalTemplateDatas).toStrictEqual([
            {
              approverId: '1',
              id: '1',
              approverSeq: 1
            },
            {
              approverId: '3',
              id: '3',
              approverSeq: 2
            },
            {
              approverId: '2',
              id: '2',
              approverSeq: 3
            }
          ]);
        })
      ));
    });
    it('Should call ApprovalTemplateDataService update method upon component save method calling', inject(
      [],
      fakeAsync(() => {
        spyOn(templateService, 'update').and.returnValue(of());

        comp.approvalRequest = new ApprovalTemplate();
        comp.approvalTemplateDatas = [{ approverId: '1', id: '1', approverSeq: 1 }];
        comp.save();
        tick();

        expect(templateService.update).toHaveBeenCalled();
        expect(comp.isSaving).toBeTruthy();
      })
    ));
  });
});
