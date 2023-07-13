import { element, by, ElementFinder } from 'protractor';

/* eslint @typescript-eslint/no-use-before-define: 0 */
export class NavBarPage {
  entityMenu = element(by.id('entity-menu'));
  accountMenu = element(by.id('account-menu'));
  adminMenu: ElementFinder;
  signIn = element(by.id('login'));
  register = element(by.css('[routerLink="register"]'));
  signOut = element(by.id('logout'));
  passwordMenu = element(by.css('[routerLink="password"]'));
  settingsMenu = element(by.css('[routerLink="settings"]'));
  homeMenu = element(by.id('home-menu'));

  constructor(asAdmin?: Boolean) {
    if (asAdmin) {
      this.adminMenu = element(by.id('admin-menu'));
    }
  }

  async clickOnHomeMenu(): Promise<void> {
    await this.homeMenu.click();
  }

  async clickOnEntityMenu(): Promise<void> {
    await this.entityMenu.click();
  }

  async clickOnAccountMenu(): Promise<void> {
    await this.accountMenu.click();
  }

  async clickOnAdminMenu(): Promise<void> {
    await this.adminMenu.click();
  }

  async clickOnSignIn(): Promise<void> {
    await this.signIn.click();
  }

  async clickOnRegister(): Promise<void> {
    await this.signIn.click();
  }

  async clickOnSignOut(): Promise<void> {
    await this.signOut.click();
  }

  async clickOnPasswordMenu(): Promise<void> {
    await this.passwordMenu.click();
  }

  async clickOnSettingsMenu(): Promise<void> {
    await this.settingsMenu.click();
  }

  async clickOnEntity(entityName: string): Promise<void> {
    await element(by.css('[routerLink="' + entityName + '"]')).click();
  }

  async clickOnAdmin(entityName: string): Promise<void> {
    await element(by.css('[routerLink="admin/' + entityName + '"]')).click();
  }

  async getSignInPage(): Promise<SignInPage> {
    await this.clickOnAccountMenu();
    await this.clickOnSignIn();
    return new SignInPage();
  }
  async getPasswordPage(): Promise<PasswordPage> {
    await this.clickOnAccountMenu();
    await this.clickOnPasswordMenu();
    return new PasswordPage();
  }

  async getSettingsPage(): Promise<SettingsPage> {
    await this.clickOnAccountMenu();
    await this.clickOnSettingsMenu();
    return new SettingsPage();
  }

  async goToEntity(entityName: string): Promise<void> {
    await this.clickOnEntityMenu();
    await this.clickOnEntity(entityName);
  }

  async goToSignInPage(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnSignIn();
  }

  async goToPasswordMenu(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnPasswordMenu();
  }

  async autoSignOut(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnSignOut();
  }
}

export class AsideBarPage {
  adminMainMuen = element(by.linkText('Administration'));
  adminSystemMuen = element(by.linkText('System'));
  adminAccessControlMuen = element(by.linkText('Access Control'));
  adminUserMuen = element(by.linkText('User'));
  adminGroupMuen = element(by.linkText('Group'));
  adminRoleMuen = element(by.linkText('Role'));
  adminFunctionMuen = element(by.linkText('Function'));
  adminResourceMuen = element(by.linkText('Resource'));
  adminApplicationMuen = element(by.linkText('Application'));
  adminTokenMuen = element(by.linkText('Token'));
  adminMetricsMuen = element(by.linkText('Metrics'));
  adminHelathMuen = element(by.linkText('Health'));
  adminLogsMuen = element(by.linkText('Logs'));
  adminConfigurationMuen = element(by.linkText('Configuration'));
  adminSwaggerMuen = element(by.linkText('API'));
  adminCodeTableMuen = element(by.linkText('Code Table'));
  adminAuditMuen = element(by.linkText('Audits'));
  adminEventAuditMuen = element(by.linkText('Login Audit'));
  adminEntityAuditMuen = element(by.linkText('Entity Audit'));
  adminEmailTemplate = element(by.linkText('Email Template'));
  adminHoliday = element(by.linkText('Holiday'));
  entityMenu = element(by.linkText('Entities'));
  entitiesAddress = element(by.linkText('Address'));
  approvalTemplate = element(by.linkText('Approval Template'));
  workflowMenu = element(by.linkText('Approval Workflow'));
  generalApproval = element(by.linkText('General Approvals'));
  search = element(by.linkText('Search'));
  workflow = element(by.linkText('WorkFlows'));
  process = element(by.linkText('Process'));
  report = element(by.linkText('Report'));
  reportTemplate = element(by.linkText('Report Template'));
  showcase = element(by.linkText('Show Case'));
  batch = element(by.linkText('Batch'));
  batchJobTrigger = element(by.linkText('Batch Job Trigger'));

  async clickOnBatchJobTrigger(): Promise<void> {
    await this.batchJobTrigger.click();
  }

  async clickOnBatch(): Promise<void> {
    await this.batch.click();
  }

  async clickOnShowCase(): Promise<void> {
    await this.showcase.click();
  }

  async clickOnReportTemplate(): Promise<void> {
    await this.reportTemplate.click();
  }

  async clickOnReport(): Promise<void> {
    await this.report.click();
  }

  async clickOnProcess(): Promise<void> {
    await this.process.click();
  }

  async clickOnWorkflow(): Promise<void> {
    await this.workflow.click();
  }

  async clickOnSearch(): Promise<void> {
    await this.search.click();
  }

  async clickOnApprovalTemplate(): Promise<void> {
    await this.approvalTemplate.click();
  }

  async clickOnEntitiesAddress(): Promise<void> {
    await this.entitiesAddress.click();
  }

  async clickOnAdminMainMuen(): Promise<void> {
    await this.adminMainMuen.click();
  }

  async clickOnSystemMainMuen(): Promise<void> {
    await this.adminSystemMuen.click();
  }

  async clickOnAccessControlMuen(): Promise<void> {
    await this.adminAccessControlMuen.click();
  }

  async clickOnUserMuen(): Promise<any> {
    await this.adminUserMuen.click();
    return new UserPage();
  }

  async clickOnGroupMuen(): Promise<any> {
    await this.adminGroupMuen.click();
    return new GroupPage();
  }

  async clickOnRoleMuen(): Promise<any> {
    await this.adminRoleMuen.click();
    return new RolePage();
  }

  async clickOnFunctionMuen(): Promise<any> {
    await this.adminFunctionMuen.click();
    return new FunctionPage();
  }

  async clickOnResourceMuen(): Promise<any> {
    await this.adminResourceMuen.click();
    return new ResourcePage();
  }

  async clickOnApplicationMuen(): Promise<any> {
    await this.adminApplicationMuen.click();
    return new ApplicationPage();
  }

  async clickOnTokenMuen(): Promise<void> {
    await this.adminTokenMuen.click();
  }

  async clickOnMetricsMuen(): Promise<void> {
    await this.adminMetricsMuen.click();
  }

  async clickOnHealthMuen(): Promise<void> {
    await this.adminHelathMuen.click();
  }

  async clickOnadminLogsMuen(): Promise<void> {
    await this.adminLogsMuen.click();
  }

  async clickOnadminConfigurationMuen(): Promise<void> {
    await this.adminConfigurationMuen.click();
  }

  async clickOnadminSwaggerMuen(): Promise<void> {
    await this.adminSwaggerMuen.click();
  }

  async clickOnCodeTableMuen(): Promise<any> {
    await this.adminCodeTableMuen.click();
    return new CodeTypePage();
  }

  async clickOnAuditMuen(): Promise<void> {
    await this.adminAuditMuen.click();
  }

  async clickOnEmailTemplateMuen(): Promise<any> {
    await this.adminEmailTemplate.click();
    return new EmailTemplatePage();
  }

  async clickOnHolidayMuen(): Promise<any> {
    await this.adminHoliday.click();
    return new HolidayPage();
  }

  async clickOnEventAudit(): Promise<any> {
    await this.adminEventAuditMuen.click();
    return new EventAuditPage();
  }

  async clickOnEntityAudit(): Promise<any> {
    await this.adminEntityAuditMuen.click();
    return new EntityAuditPage();
  }

  async clickOnEntityMenu(): Promise<any> {
    await this.entityMenu.click();
  }

  async clickOnEntity(entityName: string): Promise<any> {
    await element(by.css('[ng-reflect-router-link="' + entityName + '"]')).click();
  }

  async goToEntity(entityName: string): Promise<any> {
    await this.clickOnEntityMenu();
    await this.clickOnEntity(entityName);
  }

  async clickOnWorkflowMenu(): Promise<any> {
    await this.workflowMenu.click();
  }

  async clickOnGeneralApproval(): Promise<any> {
    await this.generalApproval.click();
  }
}

export class UserPage {
  creatNewUserButton = element(by.id('createnewuser'));
  firstName = element(by.id('firstName'));
  seachUserButton = element(by.id('seachUserButton'));

  async clickCreateNewUserButton(): Promise<any> {
    await this.creatNewUserButton.click();
    return new CreateNewUserPage();
  }

  async setSearchFirstName(firstName): Promise<any> {
    await this.firstName.sendKeys(firstName);
  }

  async autoSearchUserByFirstName(firstName): Promise<any> {
    await this.setSearchFirstName(firstName);
    await this.seachUserButton.click();
  }

  async clickEditButton(loginName): Promise<any> {
    await element(by.id(loginName)).click();
    return new EditUserPage();
  }
}

export class CreateNewUserPage {
  firstName = element(by.id('field_firstName'));
  email = element(by.id('field_email'));
  status = element(by.id('field_status'));
  loginType = element(by.id('field_loginType'));
  loginId = element(by.id('field_loginId'));
  saveButton = element(by.id('saveNewUser'));

  async setFirstName(firstName): Promise<any> {
    await this.firstName.sendKeys(firstName);
  }

  async setEmail(mail): Promise<any> {
    await this.email.sendKeys(mail);
  }

  async setStatus(): Promise<any> {
    await this.status.click();
    await element(by.css("#field_status [value='1: A']")).click();
  }

  async setLoginType(): Promise<any> {
    await this.loginType.click();
    await element(by.css("#field_loginType [value='1: PASSWORD']")).click();
  }

  async setLoginId(loginId): Promise<any> {
    await this.loginId.sendKeys(loginId);
  }

  async autoCreateUser(firstName, mail, loginId): Promise<any> {
    await this.setFirstName(firstName);
    await this.setEmail(mail);
    await this.setStatus();
    await this.setLoginType();
    await this.setLoginId(loginId);
    await this.saveButton.click();
  }
}

export class EditUserPage {
  field_lastName = element(by.id('field_lastName'));
  saveButton = element(by.id('saveEditButton'));

  async setLastName(lastName): Promise<any> {
    await this.field_lastName.sendKeys(lastName);
  }

  async autoEditUser(lastName): Promise<any> {
    await this.setLastName(lastName);
    await this.saveButton.click();
  }
}

export class GroupPage {
  createNewGroup = element(by.id('createNewGroupButton'));
  groupId = element(by.id('groupId'));
  searchButton = element(by.id('searchGroup'));
  deleteButton = element(by.id('E2E-Group-test'));

  async clickCreatButton(): Promise<any> {
    await this.createNewGroup.click();
    return new CreateNewGroupPage();
  }

  async setGroupId(groupId): Promise<any> {
    await this.groupId.sendKeys(groupId);
  }

  async autoSearchByGroupId(groupId): Promise<any> {
    await this.setGroupId(groupId);
    await this.searchButton.click();
  }

  async deleteGroup(): Promise<any> {
    await this.deleteButton.click();
    return new ConfirmDeletGroupPage();
  }
}

export class CreateNewGroupPage {
  groupId = element(by.id('field_groupId'));
  groupName = element(by.id('field_groupName'));
  saveButton = element(by.id('saveNewGroup'));

  async setGroupId(groupId): Promise<any> {
    await this.groupId.sendKeys(groupId);
  }

  async setGroupName(groupName): Promise<any> {
    await this.groupName.sendKeys(groupName);
  }

  async autoCreateNewGroup(groupId, groupName): Promise<any> {
    await this.setGroupId(groupId);
    await this.setGroupName(groupName);
    await this.saveButton.click();
  }
}

export class ConfirmDeletGroupPage {
  confirmDeleteGroup = element(by.id('confirmDeleteGroup'));

  async confirm(): Promise<any> {
    await this.confirmDeleteGroup.click();
  }
}

export class RolePage {
  createNewRoleButton = element(by.id('createNewRole'));
  roleName = element(by.id('roleName'));
  searchRole = element(by.id('searchRole'));
  deleteButton = element(by.id('E2E Role'));

  async clickCreate(): Promise<any> {
    await this.createNewRoleButton.click();
    return new CreateNewRolePage();
  }

  async searchByRoleName(roleName): Promise<any> {
    await this.roleName.sendKeys(roleName);
    await this.searchRole.click();
  }

  async deleteRole(): Promise<any> {
    await this.deleteButton.click();
    return new ConfirmDeleteRolePage();
  }
}

export class ConfirmDeleteRolePage {
  confirmDeleteRole = element(by.id('deleteRole'));

  async confirDelete(): Promise<any> {
    await this.confirmDeleteRole.click();
  }
}

export class CreateNewRolePage {
  application = element(by.id('field_application'));
  roleId = element(by.id('field_resourceId'));
  roleName = element(by.id('field_resourceName'));
  saveButton = element(by.id('saveNewRole'));

  async setApplication(): Promise<any> {
    await this.application.click();
    await element(by.css("#field_application  [value='1: Object']")).click();
  }

  async setRoleId(roleId): Promise<any> {
    await this.roleId.sendKeys(roleId);
  }

  async setRoleName(roleName): Promise<any> {
    await this.roleName.sendKeys(roleName);
  }

  async autoCreateNewRole(roleId, roleName): Promise<any> {
    await this.setApplication();
    await this.setRoleId(roleId);
    await this.setRoleName(roleName);
    await this.saveButton.click();
  }
}

export class FunctionPage {
  createNewFunctionButton = element(by.id('createNewFunction'));
  functionName = element(by.id('functionName'));
  searchButton = element(by.id('searchFunction'));
  deleteButton = element(by.id('FUNCTION-E2E'));

  async createNewFunction(): Promise<any> {
    await this.createNewFunctionButton.click();
    return new CreateNewFunctionPage();
  }
  async setFunctionName(functionName): Promise<any> {
    await this.functionName.sendKeys(functionName);
  }

  async searchFunction(functionName): Promise<any> {
    await this.setFunctionName(functionName);
    await this.searchButton.click();
  }

  async deleteFunction(): Promise<any> {
    await this.deleteButton.click();
    return new ConfirmDeleteFunctionPage();
  }
}

export class ConfirmDeleteFunctionPage {
  deleteFunctionButton = element(by.id('deleteFunction'));

  async confirmDelete(): Promise<any> {
    await this.deleteFunctionButton.click();
  }
}

export class CreateNewFunctionPage {
  application = element(by.id('field_application'));
  functionName = element(by.id('field_resourceName'));
  saveButton = element(by.id('saveNewFunction'));
  async setApplication(): Promise<any> {
    await this.application.click();
    await element(by.css("#field_application [value='1: Object']")).click();
  }

  async setFunctionName(functionName): Promise<any> {
    await this.functionName.sendKeys(functionName);
  }

  async autoCreateNewFunction(functionName): Promise<any> {
    await this.setApplication();
    await this.setFunctionName(functionName);
    await this.saveButton.click();
  }
}

export class ResourcePage {
  createNewResourceButton = element(by.id('createNewResource'));
  resourceName = element(by.id('resourceName'));
  searchResorce = element(by.id('searchResource'));
  deleteResourceButton = element(by.id('ACM-E2E-Test'));

  async createNewResource(): Promise<any> {
    await this.createNewResourceButton.click();
    return new CreateNewResourcePage();
  }

  async searchResource(resourceName): Promise<any> {
    await this.resourceName.sendKeys(resourceName);
    await this.searchResorce.click();
  }

  async deleteResource(): Promise<any> {
    await this.deleteResourceButton.click();
    return new ConfirmDeleteResource();
  }
}

export class ConfirmDeleteResource {
  deleteResourceButton = element(by.id('deleteResource'));

  async confirmDelete(): Promise<any> {
    await this.deleteResourceButton.click();
  }
}

export class CreateNewResourcePage {
  application = element(by.id('field_application'));
  resourceType = element(by.id('field_resourceType'));
  resourceName = element(by.id('field_resourceName'));
  resourcePath = element(by.id('field_resourcePath'));
  saveNewResourceButton = element(by.id('saveNewResource'));

  async setApplication(): Promise<any> {
    await this.application.click();
    await element(by.css("#field_application [value='1: Object']")).click();
  }

  async setResourceType(): Promise<any> {
    await this.resourceType.click();
    await element(by.css("#field_resourceType [value='1: URI']")).click();
  }

  async setResourceName(resourceName): Promise<any> {
    await this.resourceName.sendKeys(resourceName);
  }

  async setResourcePath(resourcePath): Promise<any> {
    await this.resourcePath.sendKeys(resourcePath);
  }

  async autoCreateNewResource(resourceName, resourcePath): Promise<any> {
    await this.setApplication();
    await this.setResourceType();
    await this.setResourceName(resourceName);
    await this.setResourcePath(resourcePath);
    await this.saveNewResourceButton.click();
  }
}

export class ApplicationPage {
  createNewApplicationButton = element(by.id('createNewApplication'));
  appName = element(by.id('appName'));
  searchAppButton = element(by.id('searchApp'));
  appDesc = element(by.id('appDesc'));

  async createNewApplication(): Promise<any> {
    await this.createNewApplicationButton.click();
    return new CreateOrEditApplicationPage();
  }

  async setAppName(appName): Promise<any> {
    await this.appName.sendKeys(appName);
  }

  async setAppDesc(appDesc): Promise<any> {
    await this.appDesc.sendKeys(appDesc);
  }

  async searchApp(appName): Promise<any> {
    await this.setAppName(appName);
    await this.searchAppButton.click();
  }

  async searchAppByDesc(appDesc): Promise<any> {
    await this.setAppDesc(appDesc);
    await this.searchAppButton.click();
  }

  async editApp(appCode): Promise<any> {
    await element(by.id(appCode)).click();
    return new CreateOrEditApplicationPage();
  }
}

export class CreateOrEditApplicationPage {
  applicationCode = element(by.id('field_appCode1'));
  applicationName = element(by.id('field_appName'));
  applicationDesc = element(by.id('field_appDesc'));
  saveAPP = element(by.id('saveApp'));

  async setAppCode(appCode): Promise<any> {
    await this.applicationCode.sendKeys(appCode);
  }

  async setAppName(appName): Promise<any> {
    await this.applicationName.sendKeys(appName);
  }

  async setAppDesc(appDesc): Promise<any> {
    await this.applicationDesc.sendKeys(appDesc);
  }

  async autoCreateNewAPP(appCode, appName): Promise<any> {
    await this.setAppCode(appCode);
    await this.setAppName(appName);
    await this.saveAPP.click();
  }

  async autoEditAPP(appDesc): Promise<any> {
    await this.setAppDesc(appDesc);
    await this.saveAPP.click();
  }
}

export class CreateOrEditAddressPage {
  name = element(by.id(''));
  location = element(by.id(''));
}

export class CodeTypePage {
  codeTypeDesc = element(by.id('codeTypeDesc'));
  SearchButton = element(by.id('search'));

  async setCodeTypeDesc(desc): Promise<any> {
    await this.codeTypeDesc.sendKeys(desc);
  }

  async search(): Promise<any> {
    await this.SearchButton.click();
  }

  async autoSearchByCodeTypeDesc(desc): Promise<any> {
    await this.setCodeTypeDesc(desc);
    await this.search();
  }
}

export class EventAuditPage {
  user = element(by.id('user'));
  SearchButton = element(by.id('search'));

  async setUser(user): Promise<any> {
    await this.user.sendKeys(user);
  }

  async search(): Promise<any> {
    await this.SearchButton.click();
  }

  async autoSearchByUser(user): Promise<any> {
    await this.setUser(user);
    await this.search();
  }
}

export class EntityAuditPage {
  funcName = element(by.id('funcName'));
  SearchButton = element(by.id('search'));

  async setFuncName(funcname): Promise<any> {
    await this.funcName.sendKeys(funcname);
  }

  async Search(): Promise<any> {
    await this.SearchButton.click();
  }

  async autoSearchByFuncName(funcname): Promise<any> {
    await this.setFuncName(funcname);
    await this.Search();
  }
}

export class SignInPage {
  username = element(by.id('username'));
  password = element(by.id('password'));
  loginButton = element(by.css('button[type=submit]'));
  closeButton = element(by.css('button[type=close]'));

  async closeSignPage(): Promise<any> {
    await this.closeButton.click();
  }

  async setUserName(username): Promise<any> {
    await this.username.sendKeys(username);
  }

  async getUserName(): Promise<any> {
    return this.username.getAttribute('value');
  }

  async clearUserName(): Promise<any> {
    await this.username.clear();
  }

  async setPassword(password): Promise<any> {
    await this.password.sendKeys(password);
  }

  async getPassword(): Promise<any> {
    return this.password.getAttribute('value');
  }

  async clearPassword(): Promise<any> {
    await this.password.clear();
  }

  async autoSignInUsing(username: string, password: string): Promise<any> {
    await this.setUserName(username);
    await this.setPassword(password);
    await this.login();
  }

  async login(): Promise<any> {
    await this.loginButton.click();
  }
}

export class ResetPasswordPage {
  oldPassword = element(by.id('oldPwd'));
  newPassword = element(by.id('newPwd'));
  confirmPassword = element(by.id('confirmPwd'));
  recallQuestion = element(by.id('pwdRecallQuestion'));
  recallAnswer = element(by.id('pwdRecallAnswer'));
  changePWDBtn = element(by.id('changPWDBtn'));

  async resetPassword(): Promise<any> {
    await this.oldPassword.sendKeys('password1');
    await this.newPassword.sendKeys('PASSpass123$');
    await this.confirmPassword.sendKeys('PASSpass123$');
    await this.recallQuestion.sendKeys('1+3=');
    await this.recallAnswer.sendKeys('1111');
    await this.changePWDBtn.click();
  }
}

export class PasswordPage {
  currentPassword = element(by.id('currentPassword'));
  password = element(by.id('newPassword'));
  confirmPassword = element(by.id('confirmPassword'));
  saveButton = element(by.css('button[type=submit]'));
  title = element.all(by.css('h2')).first();

  async setCurrentPassword(password): Promise<any> {
    await this.currentPassword.sendKeys(password);
  }

  async setPassword(password): Promise<any> {
    await this.password.sendKeys(password);
  }

  async getPassword(): Promise<any> {
    return this.password.getAttribute('value');
  }

  async clearPassword(): Promise<any> {
    await this.password.clear();
  }

  async setConfirmPassword(confirmPassword): Promise<any> {
    await this.confirmPassword.sendKeys(confirmPassword);
  }

  async getConfirmPassword(): Promise<any> {
    return this.confirmPassword.getAttribute('value');
  }

  async clearConfirmPassword(): Promise<any> {
    await this.confirmPassword.clear();
  }

  async getTitle(): Promise<any> {
    return this.title.getAttribute('jhiTranslate');
  }

  async save(): Promise<any> {
    await this.saveButton.click();
  }
}

export class EmailTemplatePage {
  createTemplateButton = element(by.id('jh-create-entity'));

  firstEditButton = element.all(by.css('ic-email-template div table .btn-group .btn-primary')).first();

  deleteButtons = element.all(by.css('ic-email-template div table .btn-group .btn-danger'));
  firstDeleteButton = element.all(by.css('ic-email-template div table .btn-group .btn-danger')).first();

  async createTemplate(): Promise<any> {
    await this.createTemplateButton.click();
    return new CreateOrEditEmailTempPage();
  }

  async edit(): Promise<any> {
    await this.firstEditButton.click();
    return new CreateOrEditEmailTempPage();
  }

  async delete(): Promise<any> {
    await this.firstDeleteButton.click();
    return new ConfirmDeleteOperPage();
  }
}

export class HolidayPage {
  createNewHolidayButton = element(by.id('createHoliday'));
  deleteButtons = element.all(by.css('ic-holiday div table .btn-danger'));

  async creatHoliday(): Promise<any> {
    await this.createNewHolidayButton.click();
    return new CreateHolidayPage();
  }

  async countDeletButtons(): Promise<any> {
    return this.deleteButtons.count();
  }

  async ClickFirstDeleteButton(): Promise<any> {
    await this.deleteButtons.first().click();
    return new ConfirmDeteHolidayPage();
  }
}

export class CreateHolidayPage {
  title = element.all(by.css('ic-holiday-maintain div h2 span')).first();
  holidayName = element(by.id('holidayName'));
  holidayType = element(by.id('holidayType'));
  holidayDesc = element(by.id('holidayDesc'));
  country = element(by.id('countryCD'));
  startDate = element(by.id('startDate'));
  endDate = element(by.id('endDate'));
  saveButton = element(by.id('saveBtn'));

  async getPageTitle(): Promise<any> {
    return await this.title.getAttribute('jhitranslate');
  }

  async setHolidayName(holidayName): Promise<any> {
    await this.holidayName.sendKeys(holidayName);
  }

  async setHolidayType(): Promise<any> {
    await this.holidayType.click();
    await element
      .all(by.css('p-dropdown p-dropdownitem li span'))
      .first()
      .click();
  }

  async setHolidayDesc(holidayDesc): Promise<any> {
    await this.holidayDesc.sendKeys(holidayDesc);
  }

  async setCountry(): Promise<any> {
    await this.country.click();
    await element
      .all(by.css('p-dropdown p-dropdownitem li span'))
      .last()
      .click();
  }

  async setStartDate(): Promise<any> {
    await this.startDate.click();
    await element
      .all(by.css('a[draggable=false]'))
      .first()
      .click();
  }

  async setEndDate(): Promise<any> {
    await this.endDate.click();
    await element
      .all(by.css('a[draggable=false]'))
      .first()
      .click();
  }

  async saveHoilday(): Promise<any> {
    await this.saveButton.click();
  }
}

export class ConfirmDeteHolidayPage {
  deleteButton = element.all(by.css('button[ng-reflect-label=Yes]')).first();

  async confirmDelte(): Promise<any> {
    await this.deleteButton.click();
  }
}

export class ConfirmDeleteOperPage {
  deleteButton = element.all(by.css('ic-email-template-delete-dialog div .btn-danger')).first();

  async confirmDelte(): Promise<any> {
    await this.deleteButton.click();
  }
}

export class CreateOrEditEmailTempPage {
  title = element.all(by.css('ic-email-template-update div form h2')).first();
  emailTempName = element(by.id('field_emailTemplateName'));
  fromEmail = element(by.id('field_fromEmail'));
  fromName = element(by.id('field_fromName'));
  emailSubject = element(by.id('field_emailSubject'));
  emailContent = element(by.id('field_emailContent'));
  saveButton = element(by.id('save-entity'));

  async getPageTitle(): Promise<any> {
    return this.title.getAttribute('jhiTranslate');
  }

  async setEmailTempName(): Promise<any> {
    await this.emailTempName.click();
    await element(by.css("#field_emailTemplateName [value='1: email-activated']")).click();
  }

  async setFromEmail(fromEmail): Promise<any> {
    await this.fromEmail.sendKeys(fromEmail);
  }

  async setFromName(fromName): Promise<any> {
    await this.fromName.sendKeys(fromName);
  }

  async setEmailSubject(emailSubject): Promise<any> {
    await this.emailSubject.sendKeys(emailSubject);
  }

  async setEmailContent(emailContent): Promise<any> {
    await this.emailContent.sendKeys(emailContent);
  }

  async save(): Promise<any> {
    await this.saveButton.click();
  }
}

export class SettingsPage {
  firstName = element(by.id('firstName'));
  lastName = element(by.id('lastName'));
  email = element(by.id('email'));
  saveButton = element(by.css('button[type=submit]'));
  title = element.all(by.css('h2')).first();

  async setFirstName(firstName): Promise<any> {
    await this.firstName.sendKeys(firstName);
  }

  async getFirstName(): Promise<any> {
    return this.firstName.getAttribute('value');
  }

  async clearFirstName(): Promise<any> {
    await this.firstName.clear();
  }

  async setLastName(lastName): Promise<any> {
    await this.lastName.sendKeys(lastName);
  }

  async getLastName(): Promise<any> {
    return this.lastName.getAttribute('value');
  }

  async clearLastName(): Promise<any> {
    await this.lastName.clear();
  }

  async setEmail(email): Promise<any> {
    await this.email.sendKeys(email);
  }

  async getEmail(): Promise<any> {
    return this.email.getAttribute('value');
  }

  async clearEmail(): Promise<any> {
    await this.email.clear();
  }

  async getTitle(): Promise<any> {
    return this.title.getAttribute('jhiTranslate');
  }

  async save(): Promise<any> {
    await this.saveButton.click();
  }
}
