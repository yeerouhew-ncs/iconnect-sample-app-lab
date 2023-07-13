/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { CodeService } from 'app/admin/code-admin/code/code.service';
import { CodeDeleteDialogComponent } from 'app/admin/code-admin/code/code-delete-dialog.component';
describe('Component Tests', () => {
  describe('Function Management Delete Component', () => {
    let comp: CodeDeleteDialogComponent;
    let fixture: ComponentFixture<CodeDeleteDialogComponent>;
    let service: CodeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [CodeDeleteDialogComponent],
        providers: [CodeService]
      })
        .overrideTemplate(CodeDeleteDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(CodeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CodeService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('123', '123');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith({ codeTypePk: '123', codeId: '123' });
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
