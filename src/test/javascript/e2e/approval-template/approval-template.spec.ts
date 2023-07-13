import { browser, ExpectedConditions as ec, by, element } from 'protractor';
import { NavBarPage, SignInPage, AsideBarPage } from '../page-objects/jhi-page-objects';
import {
  ApprovalTemplateComponentsPage,
  ApprovalTemplateCreateComponentsPage,
  ApprovalTemplateDataComponentPage
} from './approval-template.page-object';

const expect = chai.expect;

describe('Approval Template e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let asideBarPage: AsideBarPage;
  let approvalTemplateComponentsPage: ApprovalTemplateComponentsPage;
  let approvalTemplateCreateComponentsPage: ApprovalTemplateCreateComponentsPage;
  let approvalTemplateDataComponentPage: ApprovalTemplateDataComponentPage;

  before(async () => {
    navBarPage = new NavBarPage(true);
    asideBarPage = new AsideBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('appadmin', 'password1');
    await browser.wait(ec.visibilityOf(asideBarPage.adminMainMuen), 5000);
  });

  it('should load approval-template page', async () => {
    await asideBarPage.clickOnAdminMainMuen();
    await asideBarPage.clickOnApprovalTemplate();
    approvalTemplateComponentsPage = new ApprovalTemplateComponentsPage();
    await browser.wait(ec.visibilityOf(approvalTemplateComponentsPage.title), 5000);
    expect(await approvalTemplateComponentsPage.getTitle()).to.eq('approvalTemplate.home.title');
  });

  it('should load create approval-template page', async () => {
    await approvalTemplateComponentsPage.clickOnCreateButton();
    approvalTemplateCreateComponentsPage = new ApprovalTemplateCreateComponentsPage();
    expect(await approvalTemplateCreateComponentsPage.getPageTitle()).to.eq('approvalTemplate.home.createTitile');
    await approvalTemplateCreateComponentsPage.cancel();
  });

  it('should create and save approval-template', async () => {
    const nbButtonsBeforeCreate = await approvalTemplateComponentsPage.countDeleteButtons();
    await approvalTemplateComponentsPage.clickOnCreateButton();
    await approvalTemplateCreateComponentsPage.setTemplateKeyInput('e2e');
    await approvalTemplateCreateComponentsPage.setRequestTypeInput('GeneralApproval');
    await approvalTemplateCreateComponentsPage.setApprovlType();
    await approvalTemplateCreateComponentsPage.setApproverSelection();
    expect(await approvalTemplateCreateComponentsPage.getTemplateKeyInput()).to.eq('e2e', 'Expected TemplateKey value to be equals to e2e');
    await approvalTemplateCreateComponentsPage.save();
    expect(await approvalTemplateCreateComponentsPage.getSaveButton().isPresent(), 'Expected save button disappear').to.equal(false);
    expect(await approvalTemplateComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should add approver', async () => {
    approvalTemplateDataComponentPage = await approvalTemplateComponentsPage.clickOnManageButton();
    await approvalTemplateDataComponentPage.clickOnAddApproverButton();
    await approvalTemplateDataComponentPage.setApproverTitle('title');
    await approvalTemplateDataComponentPage.setUser('appadmin');
    await approvalTemplateDataComponentPage.add();
    await approvalTemplateDataComponentPage.save();
    expect(
      await element
        .all(by.css('ic-alert'))
        .first()
        .isDisplayed()
    ).to.equal(true);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
