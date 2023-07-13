import { element, by, ElementFinder } from 'protractor';

export class GeneralApprovalUpdateComponentPage {
  title = element(by.id('myGeneralApprovalLabel'));
  approvalProcess = element(by.id('field_templateKey'));
  summary = element(by.id('field_summary'));
  saveButton = element(by.id('saveApprovalRequest'));
  submitButton = element(by.id('submitApprovalRequest'));
  approveButton = element(by.id('approveRequest'));
  comfirmButton = element(by.id('comfirmApprove'));

  async getTitle(): Promise<any> {
    return this.title.getText();
  }

  async setApprovalProcess(): Promise<any> {
    await this.approvalProcess.click();
    await element(by.css("#field_templateKey [value='GeneralApproval-e2e']")).click();
  }

  async setSummary(summary): Promise<any> {
    await this.summary.sendKeys(summary);
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }

  getSubmitButton(): ElementFinder {
    return this.submitButton;
  }

  async clickOnSaveButton(): Promise<any> {
    await this.saveButton.click();
  }

  async clickOnSubmitButton(): Promise<any> {
    await this.submitButton.click();
  }

  getApproveButton(): ElementFinder {
    return this.approveButton;
  }

  async clickOnApproveButton(): Promise<any> {
    await this.approveButton.click();
  }

  async clickOnComfirmButton(): Promise<any> {
    await this.comfirmButton.click();
  }
}
