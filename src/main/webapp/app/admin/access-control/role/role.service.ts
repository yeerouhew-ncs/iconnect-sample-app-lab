import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { Resource } from '../resource/resource.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Subject } from '../user/user.model';
import { Group } from '../group/group.model';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class RoleService {
  private roleUrl = SERVER_API_URL + 'api/roles';
  private getAllApplicationUrl = this.roleUrl + '/applications';
  private assignedUsersUrl = this.roleUrl + '/assignedUsers';
  private unAssignUsersUrl = this.roleUrl + '/unAssignUsers';
  private searchUnAssignedUsersUrl = this.roleUrl + '/search/unAssignedUsers';
  private assignUsersUrl = this.roleUrl + '/assignUsers';

  private assignedFuncsUrl = this.roleUrl + '/assignedFuncs';
  private unAssignFuncsUrl = this.roleUrl + '/unAssignFuncs';
  private searchUnAssignedFuncsUrl = this.roleUrl + '/search/unAssignedFuncs';
  private assignFuncsUrl = this.roleUrl + '/assignFuncs';

  private assignedGroupsUrl = this.roleUrl + '/assignedGroups';
  private unAssignGroupsUrl = this.roleUrl + '/unAssignGroups';
  private searchUnAssignedGroupsUrl = this.roleUrl + '/search/unAssignedGroups';
  private assignGroupsUrl = this.roleUrl + '/assignGroups';

  constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

  create(role: Resource): Observable<Resource> {
    const copy = this.convert(role);
    return this.http.post(this.roleUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse, false);
        return jsonResponse;
      })
    );
  }

  update(role: Resource): Observable<Resource> {
    const copy = this.convert(role);
    return this.http.put(this.roleUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        this.convertItemFromServer(jsonResponse, false);
        return jsonResponse;
      })
    );
  }

  find(id: number): Observable<Resource> {
    return this.http.get(`${this.roleUrl}/${id}`, { observe: 'response' }).pipe(
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
      .get(this.roleUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.roleUrl}/${id}`, { observe: 'response' });
  }

  findAllApplication(): Observable<HttpResponse<any>> {
    // const params: HttpParams = null;
    // return this.http.get(this.getAllApplicationUrl, { params, observe: 'response' }).pipe(
    return this.http
      .get(this.getAllApplicationUrl, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  findAllAssignedUsers(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedUsersUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, true)));
  }

  unAssignUsers(id: any, subjects: Subject[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignUsersUrl}/${id}`, subjects, { observe: 'response' });
  }

  searchUnAssignedUsers(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedUsersUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, true)));
  }

  assignUsers(id: any, users: Subject[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignUsersUrl}/${id}`, users, { observe: 'response' });
  }

  findAllAssignedFuncs(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedFuncsUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  unAssignFuncs(id: any, resources: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignFuncsUrl}/${id}`, resources, { observe: 'response' });
  }

  searchUnAssignedFuncs(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedFuncsUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  assignFuncs(id: any, functions: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignFuncsUrl}/${id}`, functions, { observe: 'response' });
  }

  findAllAssignedGroups(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedGroupsUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  unAssignGroups(id: any, groups: Group[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignGroupsUrl}/${id}`, groups, { observe: 'response' });
  }

  searchUnAssignedGroups(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedGroupsUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res, false)));
  }

  assignGroups(id: any, groups: Group[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignGroupsUrl}/${id}`, groups, { observe: 'response' });
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

  private convert(role: Resource): Resource {
    const copy: Resource = Object.assign({}, role);
    return copy;
  }
}
