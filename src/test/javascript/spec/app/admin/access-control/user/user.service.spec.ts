import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';
import { DatePipe } from '@angular/common';
import { UserService } from 'app/admin/access-control/user/user.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Function Service', () => {
    let injector: TestBed;
    let service: UserService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils, UserService, DatePipe]
      });
      injector = getTestBed();
      service = injector.get(UserService);
      httpMock = injector.get(HttpTestingController);
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.find(123).subscribe(() => {});

        const req = httpMock.expectOne({ method: 'GET' });

        const resourceUrl = SERVER_API_URL + 'api/users';
        expect(req.request.url).toEqual(resourceUrl + '/' + 123);
      });
      it('should return Entity', () => {
        service.find(123).subscribe(received => {
          expect(received.id).toEqual(123);
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush({ id: 123 });
      });

      it('should propagate not found response', () => {
        service.find(123).subscribe(null, (_error: any) => {
          expect(_error.status).toEqual(404);
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush('Invalid request parameters', {
          status: 404,
          statusText: 'Bad Request'
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
