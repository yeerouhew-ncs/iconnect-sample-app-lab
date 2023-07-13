/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { AuditLogService } from 'app/admin/entity-audit/audit-log/audit-log.service';

describe('Service Tests', () => {
  describe('Function Service', () => {
    let injector: TestBed;
    let service: AuditLogService;
    let httpMock: HttpTestingController;
    let elemDefault: any;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [AuditLogService]
      });
      injector = getTestBed();
      expectedResult = {};
      service = injector.get(AuditLogService);
      httpMock = injector.get(HttpTestingController);
      elemDefault = { prop1: '111', prop2: 'AAAAA', prop3: 'BBBBB' };
    });

    it('should call correct URL', () => {
      service.exportToCSV('123').subscribe(() => {});

      const req = httpMock.expectOne({ method: 'GET' });

      const resourceUrl = SERVER_API_URL + 'api/log/exportCSV';
      expect(req.request.url).toEqual(resourceUrl);
    });

    it('should return an Audit log', async () => {
      const returnedFromService = Object.assign({}, elemDefault);
      const expected = Object.assign({}, returnedFromService);
      service
        .query(expected)
        .pipe(
          take(1),
          map(resp => resp.body)
        )
        .subscribe(body => (expectedResult = body));
      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
      await expectedResult.json;
    });
  });
});
