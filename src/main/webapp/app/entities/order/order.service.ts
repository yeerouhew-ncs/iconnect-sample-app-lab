import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { IOrder } from 'app/shared/model/order.model';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import * as moment from 'moment';
import { createRequestOption } from 'app/shared/util/request-util';

type EntityResponseType = HttpResponse<IOrder>;
type EntityArrayResponseType = HttpResponse<IOrder[]>;

@Injectable({ providedIn: 'root' })
export class OrderService {
  private resourceUrl = SERVER_API_URL + 'api/orders';

  constructor(private http: HttpClient) {}

  create(order: IOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .post<IOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(order: IOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(order);
    return this.http
      .put<IOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  private convertDateFromClient(order: IOrder): IOrder {
    const copy: IOrder = Object.assign({}, order, {
      orderDate: order.orderDate != null && order.orderDate.isValid() ? order.orderDate.format(DATE_FORMAT) : null
    });

    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
    return res;
  }

  private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    res.body.forEach((order: IOrder) => {
      order.orderDate = order.orderDate != null ? moment(order.orderDate) : null;
    });

    return res;
  }
}
