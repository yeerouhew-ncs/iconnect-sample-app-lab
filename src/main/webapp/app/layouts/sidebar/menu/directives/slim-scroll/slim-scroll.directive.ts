import { Directive, Input, ElementRef, OnChanges } from '@angular/core';

import 'jquery-slimscroll';

@Directive({
  selector: '[icSlimScroll]'
})
export class IcSlimScrollDirective implements OnChanges {
  @Input() public slimScrollOptions: Object | any;

  constructor(private _elementRef: ElementRef) {}

  ngOnChanges(): void {
    this._scroll();
  }

  private _scroll(): void {
    this._destroy();
    this._init();
  }

  private _init(): void {
    $(this._elementRef.nativeElement).slimScroll(this.slimScrollOptions);
  }

  private _destroy(): void {
    $(this._elementRef.nativeElement).slimScroll({ destroy: true });
  }
}
