import { browser, element, by } from 'protractor';

import {
  NavBarPage,
  SignInPage,
  AsideBarPage,
  EntityAuditPage,
  UserPage,
  CreateNewUserPage,
  EditUserPage,
  GroupPage,
  CreateNewGroupPage,
  ConfirmDeletGroupPage,
  RolePage,
  CreateNewRolePage,
  ConfirmDeleteRolePage,
  FunctionPage,
  CreateNewFunctionPage,
  ConfirmDeleteFunctionPage,
  ResourcePage,
  CreateNewResourcePage,
  ConfirmDeleteResource,
  ApplicationPage,
  CreateOrEditApplicationPage
} from '../page-objects/jhi-page-objects';

const expect = chai.expect;

describe('access-control', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let asideBarPage: AsideBarPage;
  let entityAuditPage: EntityAuditPage;
  let userPage: UserPage;
  let createNewUserPage: CreateNewUserPage;
  let editUserPage: EditUserPage;
  let groupPage: GroupPage;
  let createNewGroupPage: CreateNewGroupPage;
  let confirmDeletGroupPage: ConfirmDeletGroupPage;
  let rolePage: RolePage;
  let createNewRolePage: CreateNewRolePage;
  let confirmDeleteRolePage: ConfirmDeleteRolePage;
  let functionPage: FunctionPage;
  let createNewFunctionPage: CreateNewFunctionPage;
  let confirmDeleteFunctionPage: ConfirmDeleteFunctionPage;
  let resourcePage: ResourcePage;
  let createNewResourcePage: CreateNewResourcePage;
  let confirmDeleteResource: ConfirmDeleteResource;
  let applicationPage: ApplicationPage;
  let createOrEditApplicationPage: CreateOrEditApplicationPage;

  before(async () => {
    navBarPage = new NavBarPage(true);
    asideBarPage = new AsideBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('appadmin', 'password1');
  });

  it('should create a new user, search it and edit it', async () => {
    await asideBarPage.clickOnAdminMainMuen();
    await asideBarPage.clickOnAccessControlMuen();
    userPage = await asideBarPage.clickOnUserMuen();
    const expect1 = 'input';
    const value1 = await element(by.id('firstName')).getTagName();
    expect(value1).to.eq(expect1);
    createNewUserPage = await userPage.clickCreateNewUserButton();
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let string = '';
    for (let i = 0; i < 3; i++) {
      string += letters.charAt(Math.floor(Math.random() * letters.length));
    }

    await createNewUserPage.autoCreateUser('E2E' + '-' + string, 'huangao@ncsi.com.cn', 'E2E' + '-' + string);
    await userPage.autoSearchUserByFirstName('E2E' + '-' + string);
    editUserPage = await userPage.clickEditButton('E2E' + '-' + string);
    await editUserPage.autoEditUser('E2E' + '-' + string + 'test');
    const expect2 = 'E2E' + '-' + string + 'test';
    const value2 = await element(by.id('field_lastName')).getAttribute('ng-reflect-model');
    expect(value2).to.eq(expect2);
  });

  it('should create a new Group, search it and delete', async () => {
    groupPage = await asideBarPage.clickOnGroupMuen();
    const expect1 = 'groupId';
    const value1 = await element(by.id('groupId')).getAttribute('name');
    expect(value1).to.eq(expect1);
    createNewGroupPage = await groupPage.clickCreatButton();
    await createNewGroupPage.autoCreateNewGroup('E2E-Group-test', 'Group_test');
    await groupPage.autoSearchByGroupId('E2E-Group-test');
    confirmDeletGroupPage = await groupPage.deleteGroup();
    await confirmDeletGroupPage.confirm();
    expect(await element(by.id('E2E-Group-test')).isPresent()).to.equal(false);
  });

  it('should create a new Role,search it and delete', async () => {
    rolePage = await asideBarPage.clickOnRoleMuen();
    const expect1 = 'roleName';
    const value1 = await element(by.id('roleName')).getAttribute('name');
    expect(value1).to.eq(expect1);
    createNewRolePage = await rolePage.clickCreate();
    await createNewRolePage.autoCreateNewRole('role-e2e', 'E2E Role');
    await rolePage.searchByRoleName('E2E Role');
    confirmDeleteRolePage = await rolePage.deleteRole();
    await confirmDeleteRolePage.confirDelete();
    expect(await element(by.id('E2E Role')).isPresent()).to.equal(false);
  });

  it('should create a new Function,search it and delete', async () => {
    functionPage = await asideBarPage.clickOnFunctionMuen();
    const expect1 = 'functionName';
    const value1 = await element(by.id('functionName')).getAttribute('name');
    expect(value1).to.eq(expect1);
    createNewFunctionPage = await functionPage.createNewFunction();
    await createNewFunctionPage.autoCreateNewFunction('FUNCTION-E2E');
    await functionPage.searchFunction('FUNCTION-E2E');
    confirmDeleteFunctionPage = await functionPage.deleteFunction();
    await confirmDeleteFunctionPage.confirmDelete();
    expect(await element(by.id('FUNCTION-E2E')).isPresent()).to.equal(false);
  });

  it('should create a new Resource,search it and delete', async () => {
    resourcePage = await asideBarPage.clickOnResourceMuen();
    const expect1 = 'resourceName';
    const value1 = await element(by.id('resourceName')).getAttribute('name');
    expect(value1).to.eq(expect1);
    createNewResourcePage = await resourcePage.createNewResource();
    await createNewResourcePage.autoCreateNewResource('ACM-E2E-Test', '/e2e/test');
    await resourcePage.searchResource('ACM-E2E-Test');
    confirmDeleteResource = await resourcePage.deleteResource();
    await confirmDeleteResource.confirmDelete();
    expect(await element(by.id('ACM-E2E-Test')).isPresent()).to.equal(false);
  });

  it('should create a new Application,search and edit it', async () => {
    applicationPage = await asideBarPage.clickOnApplicationMuen();
    await applicationPage.searchAppByDesc('E2E TEST');
    createOrEditApplicationPage = await applicationPage.createNewApplication();
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let string = '';
    for (let i = 0; i < 3; i++) {
      string += letters.charAt(Math.floor(Math.random() * letters.length));
    }
    await createOrEditApplicationPage.autoCreateNewAPP('E2E' + '-' + string, 'E2E' + '-' + string);
    const createOrEditApplicationPageEdit = await applicationPage.editApp('E2E' + '-' + string);
    await createOrEditApplicationPageEdit.autoEditAPP('E2E TEST');
  });

  it('should load Token', async () => {
    await asideBarPage.clickOnTokenMuen();
    const expect1 = 'loginId';
    const value1 = await element(by.id('loginId')).getAttribute('name');
    expect(value1).to.eq(expect1);
    asideBarPage.clickOnAccessControlMuen();
  });

  it('should load Code Table', async () => {
    await asideBarPage.clickOnCodeTableMuen();
    await browser.sleep(2000);
  });

  it('should load Login Audit page', async () => {
    await asideBarPage.clickOnAuditMuen();
    await asideBarPage.clickOnEventAudit();
    await browser.sleep(2000);
    const expect1 = 'audits.title';
    const value1 = await element(by.tagName('h2')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load Entiy Audit and seach by FuncName with Authorize', async () => {
    entityAuditPage = await asideBarPage.clickOnEntityAudit();
    entityAuditPage.autoSearchByFuncName('Authorize');
    await browser.sleep(2000);
    const expect1 = 'fromDateAsStr';
    const value1 = await element(by.id('fromDateAsStr')).getAttribute('name');
    expect(value1).to.eq(expect1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
