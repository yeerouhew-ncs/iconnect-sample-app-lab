import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from '../../app.constants';
import { GeneralApproval } from './general-approval.model';
import { createRequestOption } from 'app/shared/util/request-util';

export type EntityResponseType = HttpResponse<GeneralApproval>;

@Injectable()
export class GeneralApprovalService {
  private resourceUrl = SERVER_API_URL + 'api/approval/general-approvals';

  constructor(private http: HttpClient) {}

  create(generalApproval: GeneralApproval): Observable<EntityResponseType> {
    const copy = this.convert(generalApproval);
    return this.http
      .post<GeneralApproval>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
  }

  update(generalApproval: GeneralApproval): Observable<EntityResponseType> {
    const copy = this.convert(generalApproval);
    return this.http
      .put<GeneralApproval>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<GeneralApproval>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertResponse(res)));
  }

  query(req?: any): Observable<HttpResponse<GeneralApproval[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<GeneralApproval[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: HttpResponse<GeneralApproval[]>) => this.convertArrayResponse(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertResponse(res: EntityResponseType): EntityResponseType {
    const body: GeneralApproval = this.convertItemFromServer(res.body);
    return res.clone({ body });
  }

  private convertArrayResponse(res: HttpResponse<GeneralApproval[]>): HttpResponse<GeneralApproval[]> {
    const jsonResponse: GeneralApproval[] = res.body;
    const body: GeneralApproval[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  /**
   * Convert a returned JSON object to GeneralApproval.
   */
  private convertItemFromServer(generalApproval: GeneralApproval): GeneralApproval {
    const copy: GeneralApproval = Object.assign({}, generalApproval);
    return copy;
  }

  /**
   * Convert a GeneralApproval to a JSON which can be sent to the server.
   */
  private convert(generalApproval: GeneralApproval): GeneralApproval {
    const copy: GeneralApproval = Object.assign({}, generalApproval);
    return copy;
  }
}
