import { browser, element, by, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage, ResetPasswordPage } from '../page-objects/jhi-page-objects';
const expect = chai.expect;

describe('account', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let resetPasswordPage: ResetPasswordPage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
  });

  it('should fail to login with bad password', async () => {
    await element(by.css('button[type=button]')).click();
    const expect1 = 'Welcome';
    const value1 = await element(by.css('img')).getAttribute('alt');
    expect(value1).to.eq(expect1);
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'foo');
    const value2 = await element(by.css('.alert-danger'));
    expect(value2).to.equal(value2);
  });

  it('should login with a account need to change password', async () => {
    await browser.get('/');
    await element(by.css('button[type=button]')).click();
    signInPage = await navBarPage.getSignInPage();
    const expect1 = 'username';
    const value1 = await element(by.id('username')).getAttribute('name');
    expect(value1).to.eq(expect1);
    await signInPage.autoSignInUsing('Change', 'password1');
    const expect2 = 'reset.request.title';
    const value2 = await element
      .all(by.css('ic-password-reset-init div h1'))
      .first()
      .getAttribute('jhitranslate');
    expect(value2).to.equal(expect2);
    resetPasswordPage = new ResetPasswordPage();
    await resetPasswordPage.resetPassword();
  });

  it('should login successfully with admin account', async () => {
    await browser.get('/');
    await element(by.css('button[type=button]')).click();
    signInPage = await navBarPage.getSignInPage();

    const expect1 = 'username';
    const value1 = await element(by.id('username')).getAttribute('name');
    expect(value1).to.eq(expect1);
    await signInPage.autoSignInUsing('appadmin', 'password1');

    const expect2 = 'al-sidebar-list';
    await browser.wait(ec.visibilityOf(element(by.id('al-sidebar-list'))));
    const value2 = await element(by.id('al-sidebar-list')).getAttribute('class');
    expect(value2).to.eq(expect2);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
