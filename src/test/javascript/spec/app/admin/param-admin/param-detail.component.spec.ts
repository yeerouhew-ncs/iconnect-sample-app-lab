import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { IconnectSampleAppLabTestModule } from '../../../test.module';
import { ParamDetailComponent } from 'app/admin/param-admin/param/param-detail.component';
import { ParamService } from 'app/admin/param-admin/param/param.service';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ParamModel } from 'app/admin/param-admin/param/param.model';
describe('Component Tests', () => {
  describe('ParamDetailComponent', () => {
    let comp: ParamDetailComponent;
    let fixture: ComponentFixture<ParamDetailComponent>;
    let service: ParamService;
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [ParamDetailComponent],
        providers: [ParamService]
      })
        .overrideTemplate(ParamDetailComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ParamDetailComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParamService);
    });

    describe('load function ', () => {
      it('should be load', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new ParamModel({ appId: '123', paramKey: '123' });
          spyOn(service, 'find').and.returnValue(
            of(
              new HttpResponse({
                body: entity
              })
            )
          );
          // WHEN
          comp.load('123', '123');
          tick();
          // THEN
          expect(service.find).toHaveBeenCalledWith('123', '123');
        })
      ));
    });
  });
});
