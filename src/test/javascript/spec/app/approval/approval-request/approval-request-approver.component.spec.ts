import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { IconnectSampleAppLabTestModule } from 'src/test/javascript/spec/test.module';
import { ApprovalRequestApproversComponent } from 'app/approval/approval-request';
import { IcUserPickerService } from 'app/ng-iconnect';

describe('Component Tests', () => {
  describe('Approval request approver Component', () => {
    let comp: ApprovalRequestApproversComponent;
    let fixture: ComponentFixture<ApprovalRequestApproversComponent>;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ApprovalRequestApproversComponent],
        providers: [IcUserPickerService]
      })
        .overrideTemplate(ApprovalRequestApproversComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ApprovalRequestApproversComponent);
      comp = fixture.componentInstance;
    });

    it('Should move up approvers when moveup method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(comp, 'moveUp').and.callThrough();

        // WHEN
        comp.approvalRequest = {
          approvers: [
            {
              approverId: '1',
              approvalStatus: '1',
              approverSeq: 1
            },
            {
              approverId: '2',
              approvalStatus: '2',
              approverSeq: 2
            },
            {
              approverId: '3',
              approvalStatus: '3',
              approverSeq: 3
            }
          ],
          approvalHistoryItems: [],
          approverSelection: 'FLEXIBLE',
          attachments: []
        };
        comp.moveUp(2);
        tick();
        // THEN
        expect(comp.moveUp).toHaveBeenCalledWith(2);
        expect(comp.approvalRequest.approvers).toStrictEqual([
          {
            approverId: '2',
            approvalStatus: '2',
            approverSeq: 1
          },
          {
            approverId: '1',
            approvalStatus: '1',
            approverSeq: 2
          },
          {
            approverId: '3',
            approvalStatus: '3',
            approverSeq: 3
          }
        ]);
      })
    ));

    it('Should move down approvers when moveDown method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(comp, 'moveDown').and.callThrough();

        // WHEN
        comp.approvalRequest = {
          approvers: [
            {
              approverId: '1',
              approvalStatus: '1',
              approverSeq: 1
            },
            {
              approverId: '2',
              approvalStatus: '2',
              approverSeq: 2
            },
            {
              approverId: '3',
              approvalStatus: '3',
              approverSeq: 3
            }
          ],
          approvalHistoryItems: [],
          approverSelection: 'FLEXIBLE',
          attachments: []
        };
        comp.moveDown(2);
        tick();

        // THEN
        expect(comp.moveDown).toHaveBeenCalledWith(2);
        expect(comp.approvalRequest.approvers).toStrictEqual([
          {
            approverId: '1',
            approvalStatus: '1',
            approverSeq: 1
          },
          {
            approverId: '3',
            approvalStatus: '3',
            approverSeq: 2
          },
          {
            approverId: '2',
            approvalStatus: '2',
            approverSeq: 3
          }
        ]);
      })
    ));

    it('Should move down approvers when moveDown method called', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        spyOn(comp, 'moveDown').and.callThrough();

        // WHEN
        comp.approvalRequest = {
          approvers: [
            {
              approverId: '1',
              approvalStatus: '1',
              approverSeq: 1
            },
            {
              approverId: '2',
              approvalStatus: '2',
              approverSeq: 2
            },
            {
              approverId: '3',
              approvalStatus: '3',
              approverSeq: 3
            }
          ],
          approvalHistoryItems: [],
          approverSelection: 'FLEXIBLE',
          attachments: []
        };
        comp.moveDown(2);
        tick();

        // THEN
        expect(comp.moveDown).toHaveBeenCalledWith(2);
        expect(comp.approvalRequest.approvers).toStrictEqual([
          {
            approverId: '1',
            approvalStatus: '1',
            approverSeq: 1
          },
          {
            approverId: '3',
            approvalStatus: '3',
            approverSeq: 2
          },
          {
            approverId: '2',
            approvalStatus: '2',
            approverSeq: 3
          }
        ]);
      })
    ));
  });
});
