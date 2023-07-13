import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { Application } from './application.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class ApplicationService {
  private resourceUrl = SERVER_API_URL + 'api/applications';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(application: Application): Observable<Application> {
    const copy = this.convert(application);
    return this.http.post(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  update(application: Application): Observable<Application> {
    const copy = this.convert(application);
    return this.http.put(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  find(id: number): Observable<Application> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<Application[]>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertArrayResponse(res: HttpResponse<Application[]>): HttpResponse<Application[]> {
    const jsonResponse: Application[] | null = res.body;
    const body: Application[] = [];
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

  private convert(application: Application): Application {
    const copy: Application = Object.assign({}, application);
    return copy;
  }
}
