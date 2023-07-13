## Quick Start

Navigate to project root folder, and open open command prompt.

1.  Execute command `mvn` (`mvn spring-boot:run` if you realdy have maven configured), and web application will be accessible at http://localhost:8080. Project pom.xml is configured to run default maven target "spring-boot:run".

2.  Execute command `npm install` to install npm dependencies, then execute command `npm start` to start http server that hosts client site application at http://localhost:9000.

## Development

### Software Installation

- Download and install Maven 3, please make sure your maven setting is configured to use NCS Nexus Maven repository. You can download maven `setting.xml` from https://codesparks.ncs.com.sg/iconnect-tools/maven/.
- Download and install JDK 8 or JDK 11
- Download and install Node v12.16.1, you can get it from https://codesparks.ncs.com.sg/iconnect-tools/nodejs/v12.16.1/node-v12.16.1-win-x64.zip, Extract node to local folder, e.g. "C:\nodejs", then add "C:\nodejs" to your system path. Verify node installation by type "node -v" to display node version in command line.
- Please make sure your npm setting file `.npmrc` is configured to use NCS npm registry. You need to put `.npmrc` file in user home directory ("C:\users\${myuserid}") and project root directory.

```
### Private NPM Registry
registry=http://projectconnect.ncs.com.sg/nexus/repository/npm-public/
```

### Server Side Development

Java classes under "<project-package>.generated" are automatically generated. Please try not to change files under ".generated" package so that you can upgrade to new version of iConnect in future without need to merge those generated classes.

### Client Side Development

1.  Install angular command line tool (Option `-g` will install Angular command line tool to node root folder)
    `npm install -g @angular/cli`
2.  Create new angular component
    `ng g c <my-component>`
3.  Execute `npm start` to start static http server http://localhost:9000.
4.  Execute `npm run prettier:format` to format TypeScript code
5.  Execute `npm run lint:fix` to fix lint warnings. You may refer to package.json for more npm command options.

### Build Project

1.  Build client bundle
    `npm run webpack:build`
    If you want to build for productin deployment, run `npm run webpack:prod`. `npm run` provides the full list of commands available. Use 'npm run lint:fix" to fix lint related warning/errors.

2.  Build application bundle,
    To package the application for deployment, execute command `mvn -Pprod,war package`

This will generate two bundles. **$ProjectID-$version.jar** file contains embedded undertow server and can be lanuched using `java -jar $ProjectID-$version.jar`. **$ProjectID-$version.war** is standard web application bundle and need to be deployed in java application servr such as Tomcat, JBoss, Weblogic, or Websphere.

### Test Project

iConnect comes with an default set of tests, and each generated application has:

- Unit Tests and Integration tests using the Spring Test Context framework (Run with `mvn test`).
- UI tests with Karma.js (Run with `npm test`).
- Performance tests with Gatling (Run with `mvn gatling:execute`). Gatling scripts assumes application had been started at http://localhost:8080.

## Trouble Shooting

### Has problem running 'npm install'

- Please check npm setting file `.npmrc` under your project directory and your user home directory (e.g '`C:\Users\<your_login_id>`). If you are working in NCS network, make sure `registry` are set to NCS ProjectConnect npm registry.
- Skip install optional packages by run `npm install --no-optional`, as some optional packages are executables that can not be downloaded from internal npm registry directly.
- Due to McAfee Antivirus realtime scan, `npm install` may fail for some users. In this case please download `node_modules` for from https://codesparks.ncs.com.sg/iconnect-tools/nodejs/ based on iConnect version you use, and unzip it to your project root folder (e.g. C:/myproject/node_modules).

### Other problems

Please read https://codesparks.ncs.com.sg/confluence/display/AnnouncementTT/iConnect5+FAQ

## Guidelines

#### Software Quality Assurance (SwQA)

- Follow NCS Software Quality Assurance (SwQA) standard at https://bizkit.ncs.com.sg/MyWork/Deliver%20My%20Project/SwQA/Pages/SwQA.aspx.

### Source Code Repository Policy

- Adopt feature branch development (if you use git, refer to Git User Guide at https://codesparks.ncs.com.sg/confluence/display/AnnouncementTT/Git+User+Guide#GitUserGuide-UseFeatureBranchWorkflow.
- Make sure alll unit tests and integration tests pass before merge your code into main branch (master branch, or release branch).

### Server Side Development

#### REST API Design Guide

- Follow google API design guide at https://cloud.google.com/apis/design/.

Example:

| Action                                                    | HTTP Method | API Path                      |
| --------------------------------------------------------- | ----------- | ----------------------------- |
| Get a Order                                               | GET         | orders/\${orderId}            |
| Get List of Orders                                        | GET         | orders                        |
| Create Order                                              | POST        | orders                        |
| Delete Order                                              | DELETE      | orders/\${orderId}            |
| Update Order                                              | PUT         | orders/\${orderId}            |
| Get all products in a order                               | GET         | orders/\${orderId} /products  |
| **Verify** Order (or other **NON** HTTP standard actions) | POST        | orders:**verify**/\${orderId} |

#### Testing Guideline

- Provide unit tests for all classes.
- Provide integration tests for repository classes, rest controller classes, and client classes. Integration test cases serves as live documentation how your system use other systems, as well as how other systems consume your API.
- Fix all Blocker and Critical issues of your project from sonar scan at https://codesparks.ncs.com.sg/sonar
- For simplicity , we can keep unit tests and integration tests in same code base.
  Please follow below Java Testing Class Naming Conventions:

| Test Class Naming Conventions | Description                                                                                                                                                                                                             | Example                       |
| ----------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------- |
| \*Test.java                   | Normal Unit test cases                                                                                                                                                                                                  | UserServiceTest.java          |
| \*IT.java                     | Integration test cases can run on integraton server (e.g. using in memory database)                                                                                                                                     | UserRepositoryIT.java         |
| \*ITCase.java                 | Integration test cases that need separate setup (e.g. LDAP, e.t.c). Such test cases are configured to run using 'mavenfailsafe' plugin during maven 'integraton-tests' phase only. It is not executed during packaging. | LDAPProviderITcase.java       |
| ----------------              | -------------------------------                                                                                                                                                                                         | ----------------------------- |

### Client Side Development

#### Styling Guide

- Follow angular style guide at https://angular.io/guide/styleguide.

## Known Issues

1. If your project path contains empty spaces, run "mvn spring-boot:run" from that folder will results in empty login page at http://localhost:8080.
2. If your use iConnect command line generator to generate your project, and get some lint related error during production build, please `npm run prettier:format` followed by `npm run lint:fix` to fix it.
