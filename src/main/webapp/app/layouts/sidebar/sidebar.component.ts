import { Component, Input, ElementRef, HostListener, OnInit, AfterViewInit } from '@angular/core';
import { GlobalState } from './global.state';
import * as jQuery from 'jquery';
export const layoutSizes = {
  resWidthCollapseSidebar: 1200,
  resWidthHideSidebar: 500
};

@Component({
  selector: 'ic-sidebar',
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss']
})
export class IcSidebarComponent implements OnInit, AfterViewInit {
  public menuHeight: number | undefined;
  public isMenuCollapsed = false;
  public isMenuShouldCollapsed = false;
  public showLeftBar = false;

  constructor(private _elementRef: ElementRef, private _state: GlobalState) {
    this._state.subscribe('menu.isCollapsed', (isCollapsed: boolean) => {
      this.isMenuCollapsed = isCollapsed;
    });
  }

  public ngOnInit(): void {
    if (this._shouldMenuCollapse()) {
      this.menuCollapse();
    }
  }

  public ngAfterViewInit(): void {
    setTimeout(() => this.updateSidebarHeight());
  }

  @Input()
  set showMenu(isShowMenu: boolean) {
    this.showLeftBar = isShowMenu;
  }

  @HostListener('window:resize')
  public onWindowResize(): void {
    const isMenuShouldCollapsed = this._shouldMenuCollapse();

    if (this.isMenuShouldCollapsed !== isMenuShouldCollapsed) {
      this.menuCollapseStateChange(isMenuShouldCollapsed);
    }
    this.isMenuShouldCollapsed = isMenuShouldCollapsed;
    this.updateSidebarHeight();
  }

  public menuExpand(): void {
    this.menuCollapseStateChange(false);
  }

  public menuCollapse(): void {
    this.menuCollapseStateChange(true);
  }

  public menuCollapseStateChange(isCollapsed: boolean): void {
    this.isMenuCollapsed = isCollapsed;
    this._state.notifyDataChanged('menu.isCollapsed', this.isMenuCollapsed);
  }

  public updateSidebarHeight(): void {
    // TODO: get rid of magic 84 constant
    if (!this.menuHeight) {
      this.menuHeight = jQuery(window).height() - 84;
    } else {
      if (!this._elementRef.nativeElement.childNodes[0].clientHeight) {
        this.menuHeight = jQuery(window).height() - 84;
      } else {
        this.menuHeight = this._elementRef.nativeElement.childNodes[0].clientHeight - 84;
      }
    }
  }

  private _shouldMenuCollapse(): boolean {
    return window.innerWidth <= layoutSizes.resWidthCollapseSidebar;
  }
}
