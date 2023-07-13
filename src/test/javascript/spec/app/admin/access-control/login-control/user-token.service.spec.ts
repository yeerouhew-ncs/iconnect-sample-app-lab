/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SERVER_API_URL } from 'app/app.constants';
import { UserTokenService, UserToken } from 'app/admin/access-control/login-control';
import { take, map } from 'rxjs/operators';

describe('Service Tests', () => {
  describe('Function Service', () => {
    let injector: TestBed;
    let service: UserTokenService;
    let httpMock: HttpTestingController;
    let elemDefault: any;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [UserTokenService]
      });
      injector = getTestBed();
      expectedResult = {};
      service = injector.get(UserTokenService);
      httpMock = injector.get(HttpTestingController);
      elemDefault = { ...new UserToken(0, '222', '333'), updatedBy: 'test', createdBy: 'test' };
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.find(123).subscribe(() => {});

        const req = httpMock.expectOne({ method: 'GET' });

        const resourceUrl = SERVER_API_URL + 'api/userToken';
        expect(req.request.url).toEqual(resourceUrl + '/' + 123);
      });

      it('should return a list of UserToken', async () => {
        const returnedFromService = Object.assign(
          {
            loginId: '123',
            content: '321'
          },
          elemDefault
        );
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

      it('should find an UserToken', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
        await expectedResult.json;
      });

      it('should delete a UserToken', async () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
        await expectedResult.json;
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
