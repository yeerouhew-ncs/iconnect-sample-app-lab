import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { Resource } from './resource.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class ResourceService {
  private resourceUrl = SERVER_API_URL + 'api/resources';
  private getAllApplicationUrl = this.resourceUrl + '/applications';

  private unAssignFunctionsUrl = this.resourceUrl + '/unAssignFunctions';
  private searchUnAssignedFunctionsUrl = this.resourceUrl + '/search/unAssignFunctions';
  private assignFunctionsUrl = this.resourceUrl + '/assignFunctions';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(func: Resource): Observable<Resource> {
    const copy = this.convert(func);
    return this.http.post(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  update(func: Resource): Observable<Resource> {
    const copy = this.convert(func);
    return this.http.put(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  find(id: any): Observable<Resource> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
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

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllApplication(): Observable<HttpResponse<any>> {
    return this.http
      .get(this.getAllApplicationUrl, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  unAssignFunctions(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignFunctionsUrl}/${id}`, resources, { observe: 'response' });
  }

  searchUnAssignedFunctions(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedFunctionsUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  assignFunctions(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignFunctionsUrl}/${id}`, resources, { observe: 'response' });
  }

  private convertArrayResponse(res: HttpResponse<Resource[]>): HttpResponse<Resource[]> {
    const jsonResponse: Resource[] | null = res.body;
    const body: Resource[] = [];
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

  private convert(func: Resource): Resource {
    const copy: Resource = Object.assign({}, func);
    return copy;
  }
}
