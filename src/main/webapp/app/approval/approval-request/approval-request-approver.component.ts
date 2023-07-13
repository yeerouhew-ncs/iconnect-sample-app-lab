import { Component, OnInit, Input } from '@angular/core';
import { IcUserPickerService } from 'app/ng-iconnect';
import { Approver, ApprovalRequest } from './';

@Component({
  selector: 'ic-approval-request-approvers',
  templateUrl: './approval-request-approver.component.html'
})
export class ApprovalRequestApproversComponent implements OnInit {
  @Input() approvalRequest: ApprovalRequest;
  @Input() mode: string;
  userInfo: any;
  filteredUsers: any[];
  selectedApprover: Approver;
  displayDialog: boolean;
  updateAction: string;
  constructor(private userPickerService: IcUserPickerService) {}

  ngOnInit(): void {
    this.userInfo = {
      displayName: '',
      subjectId: '',
      firstName: '',
      lastName: '',
      email: ''
    };
  }

  moveUp(approverSeq: number): void {
    const approvers = this.approvalRequest.approvers;

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
    const approvers = this.approvalRequest.approvers;

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

  edit(event: Event, approver: Approver): void {
    this.selectedApprover = Object.assign({}, approver);
    this.userInfo.displayName = this.selectedApprover.approverDisplayName;
    this.updateAction = 'edit';
    this.displayDialog = true;
    event.preventDefault();
  }

  addAfter(approverSeq: number): void {
    // Add a new approver to array
    this.selectedApprover = new Approver();

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
    this.selectedApprover.approvalStatus = 'DRAFT';
    this.updateAction = 'add';
    this.displayDialog = true;
    event.preventDefault();
  }

  remove(approverSeq: number): void {
    const approvers = this.approvalRequest.approvers;

    // If last item, do not remove
    if (approvers.length === 1) {
      return;
    }
    this.approvalRequest.approvers = approvers.filter(approver => approver.approverSeq !== approverSeq);

    this.resetApproverSeq(approvers);
  }

  resetApproverSeq(approvers: Approver[]): void {
    for (let i = 0; i < approvers.length; i++) {
      approvers[i].approverSeq = i;
    }
  }

  sortApprovers(approvers: Approver[]): void {
    approvers.sort((a, b) => {
      if (a.approverSeq < b.approverSeq) {
        return -1;
      } else if (a.approverSeq > b.approverSeq) {
        return 1;
      } else {
        return 0;
      }
    });
  }

  hideApproverDialog(): void {
    this.displayDialog = false;
    this.selectedApprover = null;
  }

  addApprover(newApprover: Approver): void {
    if (!newApprover.approverDisplayName || !newApprover.approverId) {
      return;
    }
    const approvers = this.approvalRequest.approvers;
    const selectedApprover = approvers.find(approver => approver.approverSeq === newApprover.approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);
    approvers.splice(selectedIndex + 1, 0, newApprover);
    this.resetApproverSeq(approvers);
    this.hideApproverDialog();
  }

  saveApprover(updatedApprover: Approver): void {
    if (!updatedApprover.approverDisplayName || !updatedApprover.approverId) {
      return;
    }
    const approvers = this.approvalRequest.approvers;
    const selectedApprover = approvers.find(approver => approver.approverSeq === updatedApprover.approverSeq);
    const selectedIndex = approvers.indexOf(selectedApprover);
    approvers.splice(selectedIndex, 1, updatedApprover);
    this.hideApproverDialog();
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

  onSelect(event): void {
    const selectedUser = this.filteredUsers.find(userInfo => userInfo.subjectId === event.subjectId);
    this.selectedApprover.approverId = selectedUser.subjectId;
    this.selectedApprover.approverDisplayName = selectedUser.firstName + ' ' + selectedUser.lastName + ' (' + selectedUser.email + ')';
  }
}
