/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IconnectSampleAppLabTestModule } from '../../../../test.module';
import { UserService } from 'app/admin/access-control/user/user.service';
import { UserCreateDialogComponent } from 'app/admin/access-control/user/user-create-dialog.component';
import { Subject } from 'app/admin/access-control/user/user.model';
import { SubjectLogin } from 'app/admin/access-control/user/user-login.model';
describe('Component Tests', () => {
  describe('User Management Create Component', () => {
    let comp: UserCreateDialogComponent;
    let fixture: ComponentFixture<UserCreateDialogComponent>;
    let service: UserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IconnectSampleAppLabTestModule],
        declarations: [UserCreateDialogComponent],
        providers: [UserService]
      })
        .overrideTemplate(UserCreateDialogComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserCreateDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('save function', () => {
      it('Should call create service on save', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const entity = new SubjectLogin(123);
          const userEntity = new Subject(123);
          const entityArray = [];
          entityArray.push(entity);
          spyOn(service, 'create').and.returnValue(of({}));
          userEntity.subjectLogins = entityArray;
          // WHEN
          comp.userLogin = entity;
          comp.user = userEntity;
          comp.save();
          tick();

          // THEN
          expect(service.create).toHaveBeenCalledWith(userEntity);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'userListModification', content: 'OK' });
        })
      ));
    });
  });
});
