/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';

import { ApprovalTemplateService } from 'app/admin/approval-template';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';

describe('Service Tests', () => {
  describe('Function Service', () => {
    let injector: TestBed;
    let service: ApprovalTemplateService;
    let httpMock: HttpTestingController;
    let elemDefault: any;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [ApprovalTemplateService]
      });
      injector = getTestBed();
      expectedResult = {};
      service = injector.get(ApprovalTemplateService);
      httpMock = injector.get(HttpTestingController);
      elemDefault = new ApprovalTemplate('111', 'AAAAA', 'BBBBB');
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.find('123').subscribe(() => {});

        const req = httpMock.expectOne({ method: 'GET' });
        const resourceUrl = SERVER_API_URL + 'api/approvalTemplates';
        expect(req.request.url).toEqual(resourceUrl + '/' + '123');
      });

      it('should create an Approval Template', async () => {
        const returnedFromService = Object.assign(
          {
            id: '123'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new ApprovalTemplate(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
        await expectedResult.json;
      });

      it('should update an Approval Template', async () => {
        const returnedFromService = Object.assign({}, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
        await expectedResult.json;
      });

      it('should return an Approval Template', async () => {
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

      it('should find an Approval Template', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
        await expectedResult.json;
      });

      it('should delete an Approval Template', async () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
        await expectedResult.json;
      });
    });
  });
});
