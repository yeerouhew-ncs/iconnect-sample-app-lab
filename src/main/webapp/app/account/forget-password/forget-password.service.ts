import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class ForgetPasswordService {
  constructor(private http: HttpClient) {}

  save(resetPwdRequest: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/itrust/resetPassword', resetPwdRequest);
  }

  getRecalQuestion(resetPwdRequest: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/itrust/recallQuestion', resetPwdRequest);
  }
}
