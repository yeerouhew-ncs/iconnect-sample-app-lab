import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage, AsideBarPage } from '../page-objects/jhi-page-objects';
import { GeneralApprovalUpdateComponentPage } from './approval.page-object';
const expect = chai.expect;

describe('Approval e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let asideBarPage: AsideBarPage;
  let generalApprovalUpdateComponentPage: GeneralApprovalUpdateComponentPage;

  before(async () => {
    navBarPage = new NavBarPage(true);
    asideBarPage = new AsideBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('appadmin', 'password1');
    await browser.wait(ec.visibilityOf(asideBarPage.adminMainMuen), 5000);
  });

  it('should load approval-template page', async () => {
    await asideBarPage.clickOnWorkflowMenu();
    await asideBarPage.clickOnGeneralApproval();
    generalApprovalUpdateComponentPage = new GeneralApprovalUpdateComponentPage();
    await browser.wait(ec.visibilityOf(generalApprovalUpdateComponentPage.title), 5000);
    expect(await generalApprovalUpdateComponentPage.getTitle()).to.eq('General Approval Request');
  });

  it('should save approval request', async () => {
    await generalApprovalUpdateComponentPage.setApprovalProcess();
    await generalApprovalUpdateComponentPage.setSummary('summary');
    await generalApprovalUpdateComponentPage.clickOnSaveButton();
    await browser.executeScript('window.scrollTo(0,10000)');
    expect(await generalApprovalUpdateComponentPage.getSubmitButton().isEnabled(), 'Expected Submit button enable').to.equal(true);
  });

  it('should submit and approve the request', async () => {
    await generalApprovalUpdateComponentPage.clickOnSubmitButton();
    expect(await generalApprovalUpdateComponentPage.getApproveButton().isEnabled(), 'Expected Approve button enable').to.equal(true);
    await generalApprovalUpdateComponentPage.clickOnApproveButton();
    await generalApprovalUpdateComponentPage.clickOnComfirmButton();
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
