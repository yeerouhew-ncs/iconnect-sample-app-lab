import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class FunctionService {
  private functionUrl = SERVER_API_URL + 'api/functions';
  private getAllApplicationUrl = this.functionUrl + '/applications';

  private unAssignResourcesUrl = this.functionUrl + '/unAssignResources';
  private searchUnAssignedResourcesUrl = this.functionUrl + '/search/unAssignResources';
  private assignResourcesUrl = this.functionUrl + '/assignResources';

  private unAssignRolesUrl = this.functionUrl + '/unAssignRoles';
  private searchUnAssignedRolesUrl = this.functionUrl + '/search/unAssignRoles';
  private assignRolesUrl = this.functionUrl + '/assignRoles';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(func: Resource): Observable<Resource> {
    const copy = this.convert(func);
    return this.http.post(this.functionUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  update(func: Resource): Observable<Resource> {
    const copy = this.convert(func);
    return this.http.put(this.functionUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse);
        return jsonResponse;
      })
    );
  }

  find(id: number): Observable<Resource> {
    return this.http.get(`${this.functionUrl}/${id}`, { observe: 'response' }).pipe(
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
      .get(this.functionUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.functionUrl}/${id}`, { observe: 'response' });
  }

  findAllApplication(): Observable<HttpResponse<any>> {
    return this.http
      .get(this.getAllApplicationUrl, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  unAssignResources(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignResourcesUrl}/${id}`, resources, { observe: 'response' });
  }

  searchUnAssignedResources(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedResourcesUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  assignResources(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignResourcesUrl}/${id}`, resources, { observe: 'response' });
  }

  unAssignRoles(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignRolesUrl}/${id}`, resources, { observe: 'response' });
  }

  searchUnAssignedRoles(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedRolesUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  assignRoles(id: any, functions: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignRolesUrl}/${id}`, functions, { observe: 'response' });
  }

  private convertArrayResponse(res: HttpResponse<any[]>): HttpResponse<any[]> {
    const jsonResponse: any[] | null = res.body;
    const body: any[] = [];
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
