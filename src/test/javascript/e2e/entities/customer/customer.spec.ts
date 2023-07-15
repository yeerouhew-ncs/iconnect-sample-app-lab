import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage, AsideBarPage } from '../../page-objects/jhi-page-objects';

import { CustomerComponentsPage, CustomerDeleteDialog, CustomerUpdatePage } from './customer.page-object';

const expect = chai.expect;

describe('Customer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let asideBarPage: AsideBarPage;
  let customerComponentsPage: CustomerComponentsPage;
  let customerUpdatePage: CustomerUpdatePage;
  let customerDeleteDialog: CustomerDeleteDialog;

  before(async () => {
    // await browser.get('/');
    navBarPage = new NavBarPage(true);
    asideBarPage = new AsideBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('appadmin', 'password1');
    await browser.wait(ec.visibilityOf(asideBarPage.entityMenu), 5000);
  });

  it('should load Customers', async () => {
    await asideBarPage.goToEntity('customer');
    customerComponentsPage = new CustomerComponentsPage();
    await browser.wait(ec.visibilityOf(customerComponentsPage.title), 5000);
    expect(await customerComponentsPage.getTitle()).to.eq('iconnectSampleAppLabApp.customer.home.title');
    await browser.wait(ec.or(ec.visibilityOf(customerComponentsPage.entities), ec.visibilityOf(customerComponentsPage.noResult)), 1000);
  });

  it('should load create Customer page', async () => {
    await customerComponentsPage.clickOnCreateButton();
    customerUpdatePage = new CustomerUpdatePage();
    expect(await customerUpdatePage.getPageTitle()).to.eq('iconnectSampleAppLabApp.customer.home.createOrEditLabel');
    await customerUpdatePage.cancel();
  });

  it('should create and save Customers', async () => {
    const nbButtonsBeforeCreate = await customerComponentsPage.countDeleteButtons();

    await customerComponentsPage.clickOnCreateButton();

    await promise.all([
      customerUpdatePage.setNameInput('name'),
      customerUpdatePage.accountStatusSelectLastOption(),
      customerUpdatePage.setEmailInput('8Z-rBXS-ML.7f@o-Uy-Y_gz.lG9VGH-MbXJY-jpoq.VPX-gWB7l0.eoP.sf.fdJeN-O8rYt'),
      customerUpdatePage.setTelMainInput('telMain'),
      customerUpdatePage.setDescriptionInput('description')
    ]);

    expect(await customerUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await customerUpdatePage.getEmailInput()).to.eq(
      '8Z-rBXS-ML.7f@o-Uy-Y_gz.lG9VGH-MbXJY-jpoq.VPX-gWB7l0.eoP.sf.fdJeN-O8rYt',
      'Expected Email value to be equals to 8Z-rBXS-ML.7f@o-Uy-Y_gz.lG9VGH-MbXJY-jpoq.VPX-gWB7l0.eoP.sf.fdJeN-O8rYt'
    );
    expect(await customerUpdatePage.getTelMainInput()).to.eq('telMain', 'Expected TelMain value to be equals to telMain');
    expect(await customerUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');

    await customerUpdatePage.save();
    expect(await customerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await customerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Customer', async () => {
    const nbButtonsBeforeDelete = await customerComponentsPage.countDeleteButtons();
    await customerComponentsPage.clickOnLastDeleteButton();

    customerDeleteDialog = new CustomerDeleteDialog();
    expect(await customerDeleteDialog.getDialogTitle()).to.eq('iconnectSampleAppLabApp.customer.delete.question');
    await customerDeleteDialog.clickOnConfirmButton();

    expect(await customerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
