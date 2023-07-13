import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Code } from './code.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';
import { CodeType } from '../code-type/code-type.model';

@Injectable()
export class CodeService {
  private getCodesByCodeTypePkUrl = SERVER_API_URL + 'api/codeAdmin/codesByCodeTypePk';
  private getCodesByCodeTypeIdAndCodeIdUrl = SERVER_API_URL + 'api/codeAdmin/codesByCodeTypePkAndCodeId';
  private getCodeTypesByAppIdAndCodeTypePkNotUrl = SERVER_API_URL + 'api/codeAdmin/codeTypesByAppIdAndCodeTypePkNot';
  private changeCodeSeqUrl = SERVER_API_URL + 'api/codeAdmin/changeCodeSeq';
  private codeUrl = SERVER_API_URL + 'api/codeAdmin/code';

  constructor(private http: HttpClient, private datePipe: DatePipe) {}

  getCodesByCodeTypePk(req?: any): Observable<HttpResponse<Code[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.getCodesByCodeTypePkUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertCodeArrayResponse(res)));
  }

  getCodesByCodeTypeIdAndCodeId(req?: any): Observable<HttpResponse<Code[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.getCodesByCodeTypeIdAndCodeIdUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertCodeArrayResponse(res)));
  }

  changeCodeSeq(code: Code[]): Observable<HttpResponse<any>> {
    const copy = this.convertCodes(code);
    return this.http.put(this.changeCodeSeqUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertCodeItemFromServer(jsonResponse);
      })
    );
  }

  getInternalCodeTypesByAppIdAndCodeTypePkNot(req?: any): Observable<HttpResponse<CodeType[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.getCodeTypesByAppIdAndCodeTypePkNotUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertCodeTypeArrayResponse(res)));
  }

  create(code: Code): Observable<Code> {
    const copy = this.convertCode(code);
    return this.http.post(this.codeUrl, copy).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertCodeItemFromServer(jsonResponse);
      })
    );
  }

  update(code: Code): Observable<Code> {
    const copy = this.convertCode(code);
    return this.http.put(this.codeUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertCodeItemFromServer(jsonResponse);
      })
    );
  }

  delete(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http.delete(this.codeUrl, { params, observe: 'response' });
  }

  find(id: string): Observable<Code> {
    return this.http.get(`${this.codeUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return this.convertCodeItemFromServer(res);
      })
    );
  }

  private convertCodeTypeArrayResponse(res: HttpResponse<CodeType[]>): HttpResponse<CodeType[]> {
    const jsonResponse: CodeType[] = res.body;
    const body: CodeType[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertCodeTypeItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  private convertCodeArrayResponse(res: HttpResponse<Code[]>): HttpResponse<Code[]> {
    const jsonResponse: Code[] = res.body;
    const body: Code[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertCodeItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  private convertCodeTypeItemFromServer(entity: any): any {
    return entity;
  }

  private convertCodeItemFromServer(entity: any): any {
    const copy: Code = Object.assign({}, entity);
    if (copy.effectiveDt) {
      const dateString = copy.effectiveDt.split('/');
      copy.effectiveDt = new Date(dateString[2], dateString[0] - 1, dateString[1]);
    }
    if (copy.expiryDt) {
      const dateString = copy.expiryDt.split('/');
      copy.expiryDt = new Date(dateString[2], dateString[0] - 1, dateString[1]);
    }
    return copy;
  }

  private convertCode(code: Code): Code {
    const copy: Code = Object.assign({}, code);
    if (copy.effectiveDt) {
      copy.effectiveDt = this.datePipe.transform(copy.effectiveDt, 'MM/dd/yyyy');
    }
    if (copy.expiryDt) {
      copy.expiryDt = this.datePipe.transform(copy.expiryDt, 'MM/dd/yyyy');
    }
    return copy;
  }

  private convertCodes(codes: Code[]): Code[] {
    const copy: Code[] = new Array<Code>();
    codes.forEach(element => {
      element.effectiveDt = this.datePipe.transform(element.effectiveDt, 'MM/dd/yyyy');
      element.expiryDt = this.datePipe.transform(element.effectiveDt, 'MM/dd/yyyy');
      copy.push(element);
    });
    return copy;
  }
}
