import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class MaintainProfileService {
  constructor(private http: HttpClient) {}

  save(user: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/itrust/profile', user);
  }

  createAccessToken(tokenVM: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/accessToken', tokenVM);
  }
}
