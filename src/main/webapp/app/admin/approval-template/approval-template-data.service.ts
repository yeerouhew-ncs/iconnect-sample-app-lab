import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
@Injectable()
export class ApprovalTemplateDataService {
  private resourceUrl = SERVER_API_URL + 'api/approval/approval-template-datas';

  constructor(private http: HttpClient) {}
  find(templateId: string): Observable<any> {
    return this.http.get(`${this.resourceUrl}:by-template-id/${templateId}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  private convertFromServer(entity: any): void {
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
}
