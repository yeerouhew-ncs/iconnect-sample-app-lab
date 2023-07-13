import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { IconnectSampleAppLabSharedModule } from 'app/shared/shared.module';
import { IconnectSampleAppLabCoreModule } from 'app/core/core.module';
import { IconnectSampleAppLabAppRoutingModule } from './app-routing.module';
import { IconnectSampleAppLabHomeModule } from './home/home.module';
import { IconnectSampleAppLabEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { IcSidebarComponent } from './layouts/sidebar/sidebar.component';
import { IcMenuComponent } from './layouts/sidebar/menu/menu.component';
import { IcScrollPositionDirective, IcSlimScrollDirective } from './layouts/sidebar/menu/directives';
import { IcMenuItemComponent } from './layouts/sidebar/menu/components/menu-item/menu-item.component';
import { IcMenuService } from './layouts/sidebar/menu/services/menu-service';
import { GlobalState } from './layouts/sidebar/global.state';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserModule,
    IconnectSampleAppLabSharedModule,
    IconnectSampleAppLabCoreModule,
    IconnectSampleAppLabHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    IconnectSampleAppLabEntityModule,
    IconnectSampleAppLabAppRoutingModule,
    BrowserAnimationsModule
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    IcSidebarComponent,
    IcMenuComponent,
    IcScrollPositionDirective,
    IcSlimScrollDirective,
    IcMenuItemComponent
  ],
  exports: [IcScrollPositionDirective, IcSlimScrollDirective, IcSidebarComponent, IcMenuComponent, IcMenuItemComponent],
  bootstrap: [MainComponent],
  providers: [GlobalState, IcMenuService]
})
export class IconnectSampleAppLabAppModule {}
