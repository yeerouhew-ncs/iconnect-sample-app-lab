import { element, by, ElementFinder } from 'protractor';

export class ApprovalTemplateComponentsPage {
  createButton = element(by.id('createApprovalTemp'));
  deleteButtons = element.all(by.css('ic-approval-template div table .btn-danger'));
  title = element.all(by.css('ic-approval-template div span')).first();
  manageButton = element(by.id('GeneralApproval-e2e'));

  async clickOnCreateButton(): Promise<any> {
    await this.createButton.click();
  }

  async getTitle(): Promise<any> {
    return this.title.getAttribute('jhiTranslate');
  }

  async countDeleteButtons(): Promise<any> {
    return this.deleteButtons.count();
  }

  async clickOnManageButton(): Promise<any> {
    await this.manageButton.click();
    return new ApprovalTemplateDataComponentPage();
  }
}
/* eslint @typescript-eslint/no-use-before-define: 0 */
export class ApprovalTemplateDataComponentPage {
  addApprover = element(by.id('addAfter'));
  back = element(by.id('backtest'));
  addApproverButton = element(by.id('addApprover'));
  saveApproverButton = element(by.id('saveApprover'));
  searchUserInput = element.all(by.css('#field_selectedApproverID input')).first();
  userList = element.all(by.css('.ui-autocomplete-items li')).first();
  approverTitle = element(by.id('field_selectedApproverTitle'));

  async setApproverTitle(title): Promise<any> {
    await this.approverTitle.sendKeys(title);
  }

  async setUser(user): Promise<any> {
    await this.searchUserInput.sendKeys(user);
    await this.userList.click();
  }

  async clickOnBack(): Promise<any> {
    await this.back.click();
  }

  async clickOnAddApproverButton(): Promise<any> {
    await this.addApprover.click();
  }

  async add(): Promise<any> {
    await this.addApproverButton.click();
  }

  async save(): Promise<any> {
    await this.saveApproverButton.click();
  }
}

export class ApprovalTemplateCreateComponentsPage {
  templateKey = element(by.id('templateKey'));
  requestType = element(by.css('ic-approval-template-dialog form div input#requestTypeKey'));
  approvlType = element(by.id('multiInstanceType'));
  approverSelection = element(by.id('approverSelection'));
  saveButton = element(by.id('saveApproTemp'));
  cancelButton = element(by.id('cancel-create'));
  pageTitle = element.all(by.css('ic-approval-template-dialog form div h2 span')).first();

  async setTemplateKeyInput(templateKey): Promise<any> {
    await this.templateKey.sendKeys(templateKey);
  }

  async getTemplateKeyInput(): Promise<any> {
    return await this.templateKey.getAttribute('value');
  }

  async setRequestTypeInput(requestType): Promise<any> {
    await this.requestType.sendKeys(requestType);
  }

  async setApprovlType(): Promise<any> {
    await this.approvlType.click();
    await element(by.css("#multiInstanceType [value='SEQUENTIAL']")).click();
  }

  async setApproverSelection(): Promise<any> {
    await this.approverSelection.click();
    await element(by.css("#approverSelection [value='FIXED']")).click();
  }

  async cancel(): Promise<any> {
    await this.cancelButton.click();
  }

  async getPageTitle(): Promise<any> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async save(): Promise<any> {
    await this.saveButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}
