import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { IcMenuService } from './services';
import { GlobalState } from '../global.state';
import * as jQuery from 'jquery';

@Component({
  selector: 'ic-menu',
  templateUrl: './menu.html',
  styleUrls: ['./menu.scss']
})
export class IcMenuComponent implements OnInit, OnDestroy {
  @Input() sidebarCollapsed = false;
  @Input() menuHeight: number | undefined;

  @Output() expandMenu = new EventEmitter<any>();

  public menuItems: any;
  protected _menuItemsSub: Subscription | undefined;
  public showHoverElem: boolean | undefined;
  public hoverElemHeight: number | undefined;
  public hoverElemTop: number | undefined;
  protected _onRouteChange: Subscription | undefined;
  public outOfArea = -200;

  constructor(private _router: Router, private _service: IcMenuService, private _state: GlobalState) {}

  public updateMenu(newMenuItems: any): void {
    this.menuItems = newMenuItems;
    this.selectMenuAndNotify();
  }

  public selectMenuAndNotify(): void {
    if (this.menuItems) {
      this.menuItems = this._service.selectMenuItem(this.menuItems);
      this._state.notifyDataChanged('menu.activeLink', this._service.getCurrentItem());
    }
  }

  public ngOnInit(): void {
    this._onRouteChange = this._router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (this.menuItems) {
          this.selectMenuAndNotify();
        } else {
          // on page load we have to wait as event is fired before menu elements are prepared
          setTimeout(() => this.selectMenuAndNotify());
        }
      }
    });
    this._menuItemsSub = this._service.menuItems.subscribe(this.updateMenu.bind(this));
  }

  public ngOnDestroy(): void {
    if (this._onRouteChange) this._onRouteChange.unsubscribe();
    if (this._menuItemsSub) this._menuItemsSub.unsubscribe();
  }

  public hoverItem($event): void {
    this.showHoverElem = true;
    this.hoverElemHeight = $event.currentTarget.clientHeight;
    this.hoverElemTop = $event.currentTarget.getBoundingClientRect().top - 66;
  }

  public toggleSubMenu($event): boolean {
    const submenu = jQuery($event.currentTarget).next();

    if (this.sidebarCollapsed) {
      this.expandMenu.emit(null);
      if (!$event.item.expanded) {
        $event.item.expanded = true;
        submenu.slideToggle();
      }
    } else {
      $event.item.expanded = !$event.item.expanded;
      submenu.slideToggle();
    }
    return false;
  }
}
