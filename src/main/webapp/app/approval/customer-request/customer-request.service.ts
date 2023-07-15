import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICustomerRequest } from './customer-request.model';

type EntityResponseType = HttpResponse<ICustomerRequest>;
type EntityArrayResponseType = HttpResponse<ICustomerRequest[]>;

@Injectable({ providedIn: 'root' })
export class CustomerRequestService {
  private resourceUrl = SERVER_API_URL + 'api/approval/customer-requests';

  constructor(private http: HttpClient) {}

  create(customerRequest: ICustomerRequest): Observable<EntityResponseType> {
    return this.http.post<ICustomerRequest>(this.resourceUrl, customerRequest, { observe: 'response' });
  }

  update(customerRequest: ICustomerRequest): Observable<EntityResponseType> {
    return this.http.put<ICustomerRequest>(this.resourceUrl, customerRequest, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerRequest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
