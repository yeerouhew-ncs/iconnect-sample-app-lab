import { browser, element, by } from 'protractor';
import { NavBarPage, SignInPage, AsideBarPage } from '../page-objects/jhi-page-objects';

const expect = chai.expect;

describe('system', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let asideBarPage: AsideBarPage;

  before(async () => {
    navBarPage = new NavBarPage(true);
    asideBarPage = new AsideBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('appadmin', 'password1');
  });

  it('should load  metrics', async () => {
    await asideBarPage.clickOnAdminMainMuen();
    await asideBarPage.clickOnSystemMainMuen();
    await asideBarPage.clickOnMetricsMuen();
    const expect1 = 'metrics.title';
    const value1 = await element(by.id('metrics-page-heading')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load health', async () => {
    await asideBarPage.clickOnHealthMuen();
    const expect1 = 'health.title';
    const value1 = await element(by.id('health-page-heading')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load logs', async () => {
    await asideBarPage.clickOnadminLogsMuen();
    const expect1 = 'logs.title';
    const value1 = await element(by.id('logs-page-heading')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load configuration', async () => {
    await asideBarPage.clickOnadminConfigurationMuen();
    await browser.sleep(2000);
    const expect1 = 'configuration.title';
    const value1 = await element(by.id('configuration-page-heading')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load Swagger UI', async () => {
    await asideBarPage.clickOnadminSwaggerMuen();
    await browser.sleep(2000);
    const expect1 = true;
    const value1 = await element(by.tagName('iframe')).isDisplayed();
    expect(value1).to.eq(expect1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
