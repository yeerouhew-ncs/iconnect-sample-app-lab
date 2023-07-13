import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface CodeView {
  codetypeId?: string;
  codeId?: string;
  label?: any;
  codeSeq?: number;
  status?: string;
}

@Injectable()
export class IcCodeService {
  private resourceUrlForFindMultipleCodeType = 'api/codes';
  private resourceUrlForFindAllChildLocaleCodes = 'api/codes/childCodes';
  private resourceUrlForGetCode = 'api/code';

  constructor(private http: HttpClient) {}

  /**
   *  Performs http 'Get' request to find all local codes by multiple CodeType
   * @param codeTypeIdsAsStr
   * @returns {Observable<R>}
   */
  gets(codeTypeIdsAsStr: string): Observable<any> {
    return this.http.get(`${this.resourceUrlForFindMultipleCodeType}/${codeTypeIdsAsStr}`).pipe(
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

  /**
   *  Performs http 'Get' request to get one code
   * @param codeTypeBZId
   * @param codeId
   * @returns {Observable<R>}
   */
  get(codeTypeBZId: string, codeId: string): Observable<any> {
    return this.http.get(`${this.resourceUrlForGetCode}/${codeTypeBZId}/${codeId}`).pipe(
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

  /**
   *  Performs http 'Get' request to find all child local codes
   * @param parentCodeTypeId
   * @param parentCodeId
   * @returns {Observable<R>}
   */
  getChildren(parentCodeTypeId: string, parentCodeId: string): Observable<any> {
    return this.http.get(`${this.resourceUrlForFindAllChildLocaleCodes}/${parentCodeTypeId}/${parentCodeId}`).pipe(
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
