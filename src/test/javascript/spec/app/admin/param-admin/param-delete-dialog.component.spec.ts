import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { IconnectSampleAppLabTestModule } from '../../../test.module';
import { ParamDeleteDialogComponent } from 'app/admin/param-admin/param/param-delete-dialog.component';
import { ParamService } from 'app/admin/param-admin/param/param.service';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
describe('ParamDetailComponent Tests', () => {
  describe('ParamDeleteDialogComponent', () => {
    let comp: ParamDeleteDialogComponent;
    let fixture: ComponentFixture<ParamDeleteDialogComponent>;
    let service: ParamService;
    let mockEventManager: any;
    let mockActiveModal: any;
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ParamDeleteDialogComponent],
        providers: [ParamService]
      })
        .overrideTemplate(ParamDeleteDialogComponent, '')
        .compileComponents();
    }));
    beforeEach(() => {
      fixture = TestBed.createComponent(ParamDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParamService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });
    describe('ngOnInit function ', () => {
      it('should be clear', () => {
        comp.clear();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
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
          expect(service.delete).toHaveBeenCalledWith('123', '123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
