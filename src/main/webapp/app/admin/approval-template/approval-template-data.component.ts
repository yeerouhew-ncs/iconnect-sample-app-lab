import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ApprovalTemplateDataService } from './approval-template-data.service';
import { ApprovalTemplateData } from 'app/admin/approval-template/approval-template-data.model';
import { ApprovalTemplateService } from './approval-template.service';
import { ApprovalTemplate } from './approval-template.model';
import { IcUserPickerService } from 'app/ng-iconnect';

@Component({
  selector: 'ic-approval-template-data',
  templateUrl: './approval-template-data.component.html',
  styles: []
})
export class ApprovalTemplateDataComponent implements OnInit, OnDestroy {
  approvalTemplateDatas: ApprovalTemplateData[];
  subscription: Subscription;
  approvalTemplateId: string;
  approvalRequest: ApprovalTemplate;
  selectedApprover: ApprovalTemplateData;
  userInfo: any;
  displayDialog: boolean;
  updateAction: string;
  filteredUsers: any[];
  isSaving: boolean;

  constructor(
    private approvalTemplateDataService: ApprovalTemplateDataService,
    private alertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private approvalTemplateService: ApprovalTemplateService,
    private userPickerService: IcUserPickerService
  ) {}

  ngOnInit(): void {
    this.isSaving = true;
    this.userInfo = {
      displayName: '',
      subjectId: '',
      firstName: '',
      lastName: '',
      email: ''
    };
    this.subscription = this.activatedRoute.params.subscribe(param => {
      this.approvalTemplateId = param['id'];
      this.approvalTemplateService.find(param['id']).subscribe(data => {
        this.approvalRequest = data;
        this.load();
      });
    });
  }
  addApprover(newApprover: ApprovalTemplateData): void {
    const approvers = this.approvalTemplateDatas;
    const selectedApprover = approvers.find(approver => approver.approverSeq === newApprover.approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);
    approvers.splice(selectedIndex + 1, 0, newApprover);
    this.resetApproverSeq(approvers);
    this.hideApproverDialog();
  }
  addAfter(approverSeq: number): void {
    // Add a new approver to array
    this.selectedApprover = new ApprovalTemplateData();

    this.userInfo = {
      displayName: '',
      subjectId: '',
      firstName: '',
      lastName: '',
      email: '',
      loginName: '',
      loginMechanism: ''
    };
    this.selectedApprover.approverSeq = approverSeq;
    // this.selectedApprover.approvalStatus = 'DRAFT';
    this.updateAction = 'add';
    this.displayDialog = true;
    event.preventDefault();
    this.isSaving = false;
  }
  moveUp(approverSeq: number): void {
    const approvers = this.approvalTemplateDatas;
    const selectedApprover = approvers.find(approver => approver.approverSeq === approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);

    // If approver already on top
    if (selectedIndex === 0) {
      return;
    } else {
      const approverBefore = approvers[selectedIndex - 1];
      selectedApprover.approverSeq = approverSeq - 1;
      approverBefore.approverSeq = approverSeq;
    }
    this.sortApprovers(approvers);
  }
  moveDown(approverSeq: number): void {
    const approvers = this.approvalTemplateDatas;
    const selectedApprover = approvers.find(approver => approver.approverSeq === approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);

    // If approver already at bottom
    if (selectedIndex === approvers.length) {
      return;
    } else {
      const approverAfter = approvers[selectedIndex + 1];
      selectedApprover.approverSeq = approverSeq + 1;
      approverAfter.approverSeq = approverSeq;
    }
    this.sortApprovers(approvers);
  }
  saveApprover(updatedApprover: ApprovalTemplateData): void {
    const approvers = this.approvalTemplateDatas;
    const selectedApprover = approvers.find(approver => approver.approverSeq === updatedApprover.approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);
    approvers.splice(selectedIndex, 1, updatedApprover);
    this.hideApproverDialog();
  }
  sortApprovers(approvers: ApprovalTemplateData[]): void {
    approvers.sort((a, b) => {
      if (a.approverSeq < b.approverSeq) {
        return -1;
      } else if (a.approverSeq > b.approverSeq) {
        return 1;
      } else {
        return 0;
      }
    });
    this.isSaving = false;
  }
  onSelect(event): void {
    const selectedUser = this.filteredUsers.find(userInfo => userInfo.subjectId === event.subjectId);
    this.selectedApprover.approverId = selectedUser.subjectId;
    this.selectedApprover.approverDisplayName = selectedUser.firstName + ' ' + selectedUser.lastName + ' (' + selectedUser.email + ')';
  }
  hideApproverDialog(): void {
    this.displayDialog = false;
    this.selectedApprover = null;
    this.isSaving = false;
  }
  remove(approverSeq: number): void {
    const approvers = this.approvalTemplateDatas;

    // If last item, do not remove
    if (approvers.length === 1) {
      this.alertService.addAlert({ type: 'danger', msg: 'error.removeError' }, []);
      return;
    }
    this.approvalTemplateDatas = approvers.filter(approver => approver.approverSeq !== approverSeq);
    this.resetApproverSeq(approvers);
  }
  filterUser(event): void {
    const query = event.query;
    const filtered = [];
    this.userPickerService.findByCondition(query).subscribe(data => {
      data.forEach(userInfo => {
        userInfo.displayName = userInfo.firstName + ' ' + userInfo.lastName + ' (' + userInfo.email + ')';
        filtered.push(userInfo);
      });
      this.filteredUsers = filtered;
    });
  }
  edit(event: Event, approver: ApprovalTemplateData): void {
    this.selectedApprover = Object.assign({}, approver);
    this.userInfo.displayName = this.selectedApprover.approverDisplayName;
    this.updateAction = 'edit';
    this.displayDialog = true;
    event.preventDefault();
  }
  resetApproverSeq(approvers: ApprovalTemplateData[]): void {
    for (let i = 0; i < approvers.length; i++) {
      approvers[i].approverSeq = i + 1;
    }
    this.isSaving = false;
  }
  save(): void {
    this.isSaving = true;
    if (!this.approvalTemplateDatas || this.approvalTemplateDatas.length === 0) {
      this.alertService.addAlert({ type: 'danger', msg: 'approval.approvalRequest.errorMsg.noApproversSelected' }, []);
      return;
    }

    this.approvalRequest.approvalTemplateData = this.approvalTemplateDatas;
    this.approvalTemplateService.update(this.approvalRequest).subscribe(() => {});
  }

  ngOnDestroy(): void {}

  load(): void {
    this.approvalTemplateDataService.find(this.approvalTemplateId).subscribe(data => {
      this.approvalTemplateDatas = data;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
