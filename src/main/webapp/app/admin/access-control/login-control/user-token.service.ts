import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserToken } from 'app/admin/access-control/login-control/user-token.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable()
export class UserTokenService {
  private resourceUrl = SERVER_API_URL + 'api/userToken';

  constructor(private http: HttpClient) {}

  find(id: number): Observable<UserToken> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<UserToken[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertArrayResponse(res: HttpResponse<UserToken[]>): HttpResponse<UserToken[]> {
    const jsonResponse: UserToken[] | null = res.body;
    const body: UserToken[] = [];
    if (jsonResponse) {
      for (let i = 0; i < jsonResponse.length; i++) {
        body.push(this.convertItemFromServer(jsonResponse[i]));
      }
    }
    return res.clone({ body });
  }

  private convertItemFromServer(entity: any): any {
    if (entity) {
      entity.createdBy = entity.createdBy ? entity.createdBy.replace('PASSWORD/', '').replace('NCSAD/', '') : '';
      entity.updatedBy = entity.updatedBy ? entity.updatedBy.replace('PASSWORD/', '').replace('NCSAD/', '') : '';
    }
    return entity;
  }

  private convert(application: UserToken): UserToken {
    const copy: UserToken = Object.assign({}, application);
    return copy;
  }
}
