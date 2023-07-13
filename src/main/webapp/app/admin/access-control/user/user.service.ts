import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDateUtils } from 'ng-jhipster';
import { DatePipe } from '@angular/common';
import { Subject } from './user.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Resource } from '../resource/resource.model';
import { Group } from '../group/group.model';
import { SERVER_API_URL } from '../../../app.constants';

@Injectable()
export class UserService {
  private userUrl = SERVER_API_URL + 'api/users';

  private assignedRolesUrl = this.userUrl + '/assignedRoles';
  private unAssignRolesUrl = this.userUrl + '/unAssignRoles';
  private searchUnAssignedRolesUrl = this.userUrl + '/search/unAssignedRoles';
  private assignRolesUrl = this.userUrl + '/assignRoles';

  private assignedGroupsUrl = this.userUrl + '/assignedGroups';
  private unAssignGroupsUrl = this.userUrl + '/unAssignGroups';
  private searchUnAssignedGroupsUrl = this.userUrl + '/search/unAssignedGroups';
  private assignGroupsUrl = this.userUrl + '/assignGroups';
  private resetPasswordUrl = this.userUrl + '/resetPassword';

  constructor(private http: HttpClient, private datePipe: DatePipe, private dateUtils: JhiDateUtils) {}

  create(user: Subject): Observable<Subject> {
    const copy = this.convert(user);
    // return this.http.post<Subject>(this.userUrl, user);
    return this.http.post(this.userUrl, copy).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertItemFromServer(jsonResponse);
      })
    );
  }

  update(user: Subject): Observable<Subject> {
    const copy = this.convert(user);
    return this.http.put(this.userUrl, copy, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertItemFromServer(jsonResponse);
      })
    );
  }

  find(id: number): Observable<Subject> {
    return this.http.get(`${this.userUrl}/${id}`, { observe: 'response' }).pipe(
      map((res: HttpResponse<any>) => {
        const jsonResponse = res.body;
        return this.convertItemFromServer(jsonResponse);
      })
    );
  }

  query(req?: any): Observable<HttpResponse<any>> {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.userUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  findAllAssignedRoles(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedRolesUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
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

  assignRoles(id: any, roles: Resource[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignRolesUrl}/${id}`, roles, { observe: 'response' });
  }

  findAllAssignedGroups(id: string): Observable<HttpResponse<any>> {
    return this.http
      .get(`${this.assignedGroupsUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  unAssignGroups(id: any, groups: Group[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.unAssignGroupsUrl}/${id}`, groups, { observe: 'response' });
  }

  searchUnAssignedGroups(req?: any): any {
    const params: HttpParams = createRequestOption(req);
    return this.http
      .get(this.searchUnAssignedGroupsUrl, { params, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => this.convertArrayResponse(res)));
  }

  assignGroups(id: any, groups: Group[]): Observable<HttpResponse<any>> {
    return this.http.put(`${this.assignGroupsUrl}/${id}`, groups, { observe: 'response' });
  }

  resetPassword(loginName: string): Observable<HttpResponse<any>> {
    return this.http.post(`${this.resetPasswordUrl}`, loginName, { observe: 'response' });
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
    const copy: Subject = Object.assign({}, entity);
    if (copy.effectiveDt) {
      const dateString = copy.effectiveDt.split('/');
      copy.effectiveDt = new Date(dateString[2], dateString[0] - 1, dateString[1]);
    }
    if (copy.expiryDt) {
      const dateString = copy.expiryDt.split('/');
      copy.expiryDt = new Date(dateString[2], dateString[0] - 1, dateString[1]);
    }
    return copy;
  }

  private convert(user: Subject): Subject {
    const copy: Subject = Object.assign({}, user);
    if (copy.effectiveDt) {
      copy.effectiveDt = this.datePipe.transform(copy.effectiveDt, 'MM/dd/yyyy');
    }
    if (copy.expiryDt) {
      copy.expiryDt = this.datePipe.transform(copy.expiryDt, 'MM/dd/yyyy');
    }
    return copy;
  }
}
