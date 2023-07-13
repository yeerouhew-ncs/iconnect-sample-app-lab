/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { IconnectSampleAppLabTestModule } from '../../../test.module';
import { GeneralApprovalDetailComponent } from 'app/approval/general-approval/general-approval-detail.component';
import { GeneralApprovalService } from 'app/approval/general-approval/general-approval.service';
import { GeneralApproval } from 'app/approval/general-approval/general-approval.model';

describe('Component Tests', () => {
  describe('GeneralApproval Management Detail Component', () => {
    let comp: GeneralApprovalDetailComponent;
    let fixture: ComponentFixture<GeneralApprovalDetailComponent>;
    let service: GeneralApprovalService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [GeneralApprovalDetailComponent],
        providers: [GeneralApprovalService]
      })
        .overrideTemplate(GeneralApprovalDetailComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(GeneralApprovalDetailComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GeneralApprovalService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        spyOn(service, 'find').and.returnValue(
          of(
            new HttpResponse({
              body: new GeneralApproval(123)
            })
          )
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.find).toHaveBeenCalledWith(123);
        expect(comp.generalApproval).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
