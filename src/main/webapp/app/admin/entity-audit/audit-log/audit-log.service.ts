import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class AuditLogService {
  private resourceUrlForAuditList = SERVER_API_URL + 'api/log/auditLog';
  private resourceUrlForExportLog = SERVER_API_URL + 'api/log/exportCSV';

  constructor(private http: HttpClient) {}

  query(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrlForAuditList, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  exportToCSV(req?: any): Observable<HttpResponse<string>> {
    const params: HttpParams = createRequestOption(req);
    return this.http.get(this.resourceUrlForExportLog, { params, observe: 'response', responseType: 'text' });
  }

  private convertArrayResponse(res: HttpResponse<any[]>): HttpResponse<any[]> {
    const jsonResponse: any[] = res.body;
    const body: any[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(jsonResponse[i]);
    }
    return res.clone({ body });
  }
}
