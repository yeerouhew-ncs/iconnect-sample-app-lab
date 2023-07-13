import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from '../../app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ApprovalRequest } from './approval-request.model';
import { Approver } from './approver.model';
import { ApprovalTemplateData } from '../../admin/approval-template/approval-template-data.model';
import { ApprovalTemplate } from '../../admin/approval-template/approval-template.model';
import { TaskAction } from './task-action.model';

export type ApprovalRequestResponseType = HttpResponse<ApprovalRequest>;
export type ApprovalTemplateResponseType = HttpResponse<ApprovalTemplate>;

@Injectable()
export class ApprovalRequestService {
  private resourceUrl = SERVER_API_URL + 'api/approval/approval-requests';
  private approvalTemplateDataResourceUrl = SERVER_API_URL + 'api/approval/approval-template-datas';
  private approvalTemplateResourceUrl = SERVER_API_URL + 'api/approval/approval-templates';

  constructor(private http: HttpClient) {}

  private convertResponse(res: ApprovalRequestResponseType): ApprovalRequestResponseType {
    const body: ApprovalRequest = this.convertItemFromServer(res.body);
    return res.clone({ body });
  }

  private convertArrayResponse(res: HttpResponse<ApprovalRequest[]>): HttpResponse<ApprovalRequest[]> {
    const jsonResponse: ApprovalRequest[] = res.body;
    const body: ApprovalRequest[] = [];
    for (let i = 0; i < jsonResponse.length; i++) {
      body.push(this.convertItemFromServer(jsonResponse[i]));
    }
    return res.clone({ body });
  }

  /**
   * Convert a returned JSON object to ApprovalRequest.
   */
  private convertItemFromServer(approvalRequest: ApprovalRequest): ApprovalRequest {
    const copy: ApprovalRequest = Object.assign({}, approvalRequest);
    return copy;
  }

  /**
   * Convert a ApprovalRequest to a JSON which can be sent to the server.
   */
  private convert(approvalRequest: ApprovalRequest): ApprovalRequest {
    const copy: ApprovalRequest = Object.assign({}, approvalRequest);
    return copy;
  }

  private convertTaskAction(taskAction: TaskAction): TaskAction {
    const copy: TaskAction = Object.assign({}, taskAction);
    return copy;
  }

  getApproversFromJson(typeKey: string): Observable<Approver[]> {
    return this.http.get('content/approval/json/' + typeKey + '_approvers.json').pipe(map(response => response as Approver[]));
  }

  getApprovers(templateId: string): Observable<Approver[]> {
    return this.http
      .get(`${this.approvalTemplateDataResourceUrl}:by-template-id/${templateId}`)
      .pipe(map((res: ApprovalTemplateData[]) => this.toApprovers(res)));
  }

  private toApprovers(templates: ApprovalTemplateData[]): Approver[] {
    return templates.map(item => this.toApprover(item));
  }

  private toApprover(approvalTemplateData: ApprovalTemplateData): any {
    const approver: Approver = new Approver();
    Object.keys(approvalTemplateData).forEach(key => (approver[key] = approvalTemplateData[key]));
    return approver;
  }

  getApprovalTemplate(typeKey: string, templateKey: string): Observable<ApprovalTemplateResponseType> {
    if (!templateKey || templateKey === '') {
      templateKey = 'DEFAULT';
    }
    return this.http.get<ApprovalTemplate>(`${this.approvalTemplateResourceUrl}:by-selector/${typeKey}/${templateKey}`, {
      observe: 'response'
    });
  }

  submitRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':submit/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  approveRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':approve/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  completeRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':complete/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  rejectRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':reject/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  cancelRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':cancel/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  findAttachmentsByRequestId(approvalRequestId: number): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.resourceUrl}/${approvalRequestId}/attachments`, { observe: 'response' });
  }

  deleteAttachment(approvalRequestId: number, attachmentId: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${approvalRequestId}/attachments/${attachmentId}`, { observe: 'response' });
  }

  downloadAttachment(approvalRequestId: number, attachmentId: number): Observable<HttpResponse<any>> {
    return this.http.get(`${this.resourceUrl}/${approvalRequestId}/attachments/${attachmentId}`, {
      observe: 'response',
      responseType: 'text'
    });
  }

  sendBackRequest(approvalRequest: ApprovalRequest, taskAction: TaskAction): Observable<ApprovalRequestResponseType> {
    const copy = this.convertTaskAction(taskAction);
    return this.http
      .post<ApprovalRequest>(this.resourceUrl + ':rollbackToApplicant/' + approvalRequest.id, copy, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  query(req?: any): Observable<HttpResponse<ApprovalRequest[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<ApprovalRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: HttpResponse<ApprovalRequest[]>) => this.convertArrayResponse(res)));
  }

  find(id: number): Observable<ApprovalRequestResponseType> {
    return this.http
      .get<ApprovalRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: ApprovalRequestResponseType) => this.convertResponse(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
