import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
@Injectable()
export class ApprovalTemplateService {
  private resourceUrl = SERVER_API_URL + 'api/approvalTemplates';

  constructor(private http: HttpClient) {}

  create(param: ApprovalTemplate): Observable<ApprovalTemplate> {
    const copy = this.convert(param);
    return this.http.post(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return res.body;
      })
    );
  }

  update(param: ApprovalTemplate): Observable<ApprovalTemplate> {
    const copy = this.convert(param);
    return this.http.put(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        return res.body;
      })
    );
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  find(id: string): Observable<ApprovalTemplate> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrl, { params, observe: 'response' })
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

  private convert(param: ApprovalTemplate): ApprovalTemplate {
    const copy: ApprovalTemplate = Object.assign({}, param);
    return copy;
  }
}
