import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { Group } from './group.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Subject } from '../user/user.model';
import { Resource } from '../resource/resource.model';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class GroupService {
  private resourceUrl = SERVER_API_URL + 'api/groups';
  private assignedUsersUrl = this.resourceUrl + '/assignedUsers';
  private unAssignUsersUrl = this.resourceUrl + '/unAssignUsers';
  private searchUnAssignedUsersUrl = this.resourceUrl + '/search/unAssignedUsers';
  private assignUsersUrl = this.resourceUrl + '/assignUsers';
  private assignedRolesUrl = this.resourceUrl + '/assignedRoles';
  private unAssignRolesUrl = this.resourceUrl + '/unAssignRoles';
  private searchUnAssignedRolesUrl = this.resourceUrl + '/search/unAssignedRoles';
  private assignRolesUrl = this.resourceUrl + '/assignRoles';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(group: Group): Observable<Group> {
    const copy = this.convert(group);
    return this.http.post(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse, false);
        return jsonResponse;
      })
    );
  }

  update(group: Group): Observable<Group> {
    const copy = this.convert(group);
    return this.http.put(this.resourceUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse, false);
        return jsonResponse;
      })
    );
  }

  find(id: string): Observable<Group> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse, false);
        return jsonResponse;
      })
    );
  }

  query(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAll(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.resourceUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  findAllAssignedUsers(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedUsersUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, true)));
  }

  findAllAssignedRoles(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedRolesUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  unAssignUsers(id: any, subjects: Subject[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignUsersUrl}/${id}`, subjects, { observe: 'response' });
  }

  assignUsers(id: any, subjects: Subject[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignUsersUrl}/${id}`, subjects, { observe: 'response' });
  }

  searchUnAssignedUsers(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedUsersUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, true)));
  }

  unAssignRoles(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignRolesUrl}/${id}`, resources, { observe: 'response' });
  }

  assignRoles(id: any, subjects: Subject[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignRolesUrl}/${id}`, subjects, { observe: 'response' });
  }

  searchUnAssignedRoles(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedRolesUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  private convertArrayResponse(res: HttpResponse<any[]>, isUser: boolean): HttpResponse<any[]> {
    const jsonResponse: any[] | null = res.body;
    const body: any[] = [];
    if (jsonResponse) {
      for (let i = 0; i < jsonResponse.length; i++) {
        body.push(this.convertItemFromServer(jsonResponse[i], isUser));
      }
    }
    return res.clone({ body });
  }

  private convertItemFromServer(entity: any, isUser: boolean): any {
    if (entity) {
      if (isUser) {
        entity.fullName = entity.firstName + ' ' + entity.lastName;
      }
      entity.createdBy = entity.createdBy ? entity.createdBy.replace('PASSWORD/', '').replace('NCSAD/', '') : '';
      entity.updatedBy = entity.updatedBy ? entity.updatedBy.replace('PASSWORD/', '').replace('NCSAD/', '') : '';
    }
    return entity;
  }

  private convert(group: Group): Group {
    const copy: Group = Object.assign({}, group);
    return copy;
  }
}
