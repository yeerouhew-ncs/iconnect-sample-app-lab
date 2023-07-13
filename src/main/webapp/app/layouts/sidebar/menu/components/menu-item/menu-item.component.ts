import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';

@Component({
  selector: 'ic-menu-item',
  templateUrl: './menu-item.html',
  styleUrls: ['./menu-item.scss']
})
export class IcMenuItemComponent implements OnInit {
  @Input() menuItem: any;
  @Input() child = false;

  @Output() itemHover = new EventEmitter<any>();
  @Output() toggleSubMenu = new EventEmitter<any>();

  ngOnInit(): void {
    this.menuItem.expanded = false;
  }

  public onHoverItem($event: any): void {
    this.itemHover.emit($event);
  }

  public onToggleSubMenu($event: { item: any }, item: any): boolean {
    $event.item = item;
    this.toggleSubMenu.emit($event);
    return false;
  }
}
