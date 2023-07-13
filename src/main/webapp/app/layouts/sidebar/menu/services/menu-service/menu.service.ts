import { Injectable } from '@angular/core';
import { Router, Routes } from '@angular/router';
import * as _ from 'lodash';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class IcMenuService {
  menuItems = new BehaviorSubject<any[]>([]);

  protected _currentMenuItem = {};

  constructor(private _router: Router) {}

  /**
   * Updates the routes in the menu
   *
   * @param {Routes} routes Type compatible with app.menu.ts
   */
  public updateMenuByRoutes(routes: Routes): any {
    const convertedRoutes = this.convertRoutesToMenus(_.cloneDeep(routes));
    this.menuItems.next(convertedRoutes);
  }

  public convertRoutesToMenus(routes: Routes): any[] {
    const items = this._convertArrayToItems(routes);
    return this._skipEmpty(items);
  }

  public getCurrentItem(): any {
    return this._currentMenuItem;
  }

  public selectMenuItem(menuItems: any[]): any[] {
    const items: any[] = [];
    menuItems.forEach(item => {
      this._selectItem(item);

      if (item.selected) {
        this._currentMenuItem = item;
      }

      if (item.submenus && item.submenus.length > 0) {
        item.submenus = this.selectMenuItem(item.submenus);
      }
      items.push(item);
    });
    return items;
  }

  protected _skipEmpty(items: any[]): any[] {
    const menu: any[] = [];
    items.forEach(item => {
      let menuItem;
      if (item.skip) {
        if (item.submenus && item.submenus.length > 0) {
          menuItem = item;
        }
      } else {
        menuItem = item;
      }

      if (menuItem) {
        menu.push(menuItem);
      }
    });

    // eslint-disable-next-line prefer-spread
    return [].concat.apply([], menu);
  }

  protected _convertArrayToItems(routes: any[], parent?: any): any[] {
    const items: any[] = [];
    routes.forEach(route => {
      items.push(this._convertObjectToItem(route, parent));
    });
    return items;
  }

  protected _convertObjectToItem(object: { data: { menu: any }; submenus: any[] }, parent?: any): any {
    let item: any = {};
    if (object.data && object.data.menu) {
      // this is a menu object
      item = object.data.menu;
      item.route = object;
      delete item.route.data.menu;
    } else {
      item.route = object;
      item.skip = true;
    }

    // we have to collect all paths to correctly build the url then
    if (Array.isArray(item.route.url)) {
      item.route.paths = item.route.url;
    } else {
      item.route.paths = parent && parent.route && parent.route.paths ? parent.route.paths.slice(0) : ['/'];
      if (item.route.url) {
        item.route.paths.push(item.route.url);
      }
    }

    if (object.submenus && object.submenus.length > 0) {
      item.submenus = this._convertArrayToItems(object.submenus, item);
    }

    const prepared = this._prepareItem(item);

    // if current item is selected or expanded - then parent is expanded too
    if ((prepared.selected || prepared.expanded) && parent) {
      parent.expanded = true;
    }
    return prepared;
  }

  protected _prepareItem(object: any): any {
    if (!object.skip) {
      object.target = object.target || '';
      object.pathMatch = object.pathMatch || 'full';
      return this._selectItem(object);
    }
    return object;
  }

  protected _selectItem(object: any): any {
    object.selected = this._router.isActive(this._router.createUrlTree(object.route.paths), object.pathMatch === 'full');
    return object;
  }
}
