import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CodeType } from './code-type.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class CodeTypeService {
  private codeTypeUrl = SERVER_API_URL + 'api/codeAdmin/codeType';
  private appListUrl = SERVER_API_URL + 'api/codeAdmin/appList';

  constructor(private http: HttpClient) {}

  getAppList(): Observable<HttpResponse<any[]>> {
    return this.http
      .get(`${this.appListUrl}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertApplicationArrayResponse(res)));
  }

  create(codeType: CodeType): Observable<CodeType> {
    const copy = this.convert(codeType);
    return this.http.post(this.codeTypeUrl, copy).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  update(codeType: CodeType): Observable<CodeType> {
    const copy = this.convert(codeType);
    return this.http.put(this.codeTypeUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  find(id: string): Observable<CodeType> {
    return this.http.get(`${this.codeTypeUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(res);
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<CodeType[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(`${this.codeTypeUrl}`, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.codeTypeUrl}/${id}`, { observe: 'response' });
  }

  private convertApplicationArrayResponse(res: HttpResponse<CodeType[]>): HttpResponse<CodeType[]> {
    const jsonResponse: any[] = res.body;
    const body: any[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  private convertArrayResponse(res: HttpResponse<CodeType[]>): HttpResponse<CodeType[]> {
    const jsonResponse: CodeType[] = res.body;
    const body: CodeType[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  private convertItemFromServer(entity: any): any {
    return entity;
  }

  private convert(codeType: CodeType): CodeType {
    const copy: CodeType = Object.assign({}, codeType);
    return copy;
  }
}
