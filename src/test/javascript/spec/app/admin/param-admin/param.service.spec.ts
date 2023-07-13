/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';
import { DatePipe } from '@angular/common';
import { ParamService } from 'app/admin/param-admin/param/param.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Param Service', () => {
    let injector: TestBed;
    let service: ParamService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils, ParamService, DatePipe]
      });
      injector = getTestBed();
      service = injector.get(ParamService);
      httpMock = injector.get(HttpTestingController);
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.find('123', '456').subscribe(() => {});
        const req = httpMock.expectOne({ method: 'GET' });
        const resourceUrl = SERVER_API_URL + 'api/paramadmin';
        expect(req.request.url).toEqual(resourceUrl + '/' + 123 + '/' + 456);
      });
      it('should propagate not found response', () => {
        service.find('123', '456').subscribe(null, (_error: any) => {
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
