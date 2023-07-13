import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable()
export class IcUserPickerService {
  private resourceUrlForGetUser = 'api/users';

  private resourceUrlForGetUserInfo = 'api/userInfo';

  constructor(private http: HttpClient) {}

  findByCondition(condition: string): Observable<any> {
    return this.http.get(`${this.resourceUrlForGetUser}?condition=${condition}`).pipe(
      map((res: HttpResponse<any>) => {
        try {
          return res;
        } catch (error) {
          console.error(error);
          return null;
        }
      })
    );
  }

  findUserInfoBySubjectId(subjectId: string): Observable<any> {
    return this.http.get(`${this.resourceUrlForGetUserInfo}/${subjectId}`).pipe(
      map((res: HttpResponse<any>) => {
        try {
          return res;
        } catch (error) {
          console.error(error);
          return null;
        }
      })
    );
  }
}
