/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';
import { DatePipe } from '@angular/common';
import { CodeService } from 'app/admin/code-admin/code/code.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Function Service', () => {
    let injector: TestBed;
    let service: CodeService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils, CodeService, DatePipe]
      });
      injector = getTestBed();
      service = injector.get(CodeService);
      httpMock = injector.get(HttpTestingController);
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.find('123').subscribe(() => {});

        const req = httpMock.expectOne({ method: 'GET' });

        const resourceUrl = SERVER_API_URL + 'api/codeAdmin/code';
        expect(req.request.url).toEqual(resourceUrl + '/' + 123);
      });

      it('should propagate not found response', () => {
        service.find('123').subscribe(null, (_error: any) => {
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