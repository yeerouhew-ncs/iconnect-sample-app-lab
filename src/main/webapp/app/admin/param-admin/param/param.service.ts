import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ParamModel } from './param.model';
import { JhiDateUtils } from 'ng-jhipster';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';
import { DatePipe } from '@angular/common';
@Injectable()
export class ParamService {
  private appListUrl = SERVER_API_URL + 'api/applicationList';
  private urlForParameter = SERVER_API_URL + 'api/paramadmin';
  private random: any;

  constructor(private http: HttpClient, private datePipe: DatePipe, private dateUtils: JhiDateUtils) {}

  getAppList(): Observable<any> {
    return this.http.get(`${this.appListUrl}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return res.body;
      })
    );
  }

  create(param: ParamModel): Observable<ParamModel> {
    const copy = this.convert(param);
    return this.http.post(this.urlForParameter, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return res.body;
      })
    );
  }

  update(param: ParamModel): Observable<ParamModel> {
    const copy = this.convert(param);
    return this.http.put(this.urlForParameter, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return res.body;
      })
    );
  }

  delete(appId: string, paramKey: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.urlForParameter}/${appId}/${paramKey}`, { observe: 'response' });
  }

  find(appId: string, paramKey: string): Observable<ParamModel> {
    return this.http.get(`${this.urlForParameter}/${appId}/${paramKey}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.urlForParameter, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  private convertArrayResponse(res: HttpResponse<any[]>): HttpResponse<any[]> {
    const jsonResponse: any[] = res.body;
    const body: any[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(jsonResponse[i]);
    }
    return res.clone({ body });
  }

  private convert(param: ParamModel): ParamModel {
    const copy: ParamModel = Object.assign({}, param);
    if (copy.effectiveDate) {
      copy.effectiveDate = this.datePipe.transform(copy.effectiveDate, 'MM/dd/yyyy');
    }
    if (copy.expireDate) {
      copy.expireDate = this.datePipe.transform(copy.expireDate, 'MM/dd/yyyy');
    }
    return copy;
  }

  private convertFromServer(entity: any): any {
    if (entity.effectiveDate) {
      const effectiveDateString = entity.effectiveDate.split('/');
      entity.effectiveDate = new Date(effectiveDateString[2], effectiveDateString[0] - 1, effectiveDateString[1]);
    }
    if (entity.expireDate) {
      const expireDateString = entity.expireDate.split('/');
      entity.expireDate = new Date(expireDateString[2], expireDateString[0] - 1, expireDateString[1]);
    }
    return null;
  }

  getParamTypes(): any {
    return [
      { codeId: 'S', label: 'String' },
      { codeId: 'L', label: 'List' },
      { codeId: 'M', label: 'Map' }
    ];
  }

  newGuid(): any {
    let guid = '';
    for (let i = 1; i <= 32; i++) {
      this.random = window.crypto.getRandomValues(new Uint8Array(1));
      const n = Math.floor(this.random * 0.001 * 16.0)
        .toString(16)
        .toUpperCase();
      guid += n;
      if (i === 8 || i === 12 || i === 16 || i === 20) {
        guid += '-';
      }
    }
    return guid;
  }
}
