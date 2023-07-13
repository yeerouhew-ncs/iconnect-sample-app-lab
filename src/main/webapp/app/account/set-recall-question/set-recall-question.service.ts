import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class SetRecallQuestionService {
  constructor(private http: HttpClient) {}

  save(setRecallRequest: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/itrust/setRecall', setRecallRequest);
  }
}
