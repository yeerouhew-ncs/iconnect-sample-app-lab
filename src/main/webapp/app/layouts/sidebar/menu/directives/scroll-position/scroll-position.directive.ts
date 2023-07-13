import { Directive, Input, Output, EventEmitter, HostListener, OnInit } from '@angular/core';

@Directive({
  selector: '[icScrollPosition]'
})
export class IcScrollPositionDirective implements OnInit {
  @Input() public maxHeight: number | any;
  @Output() public scrollChange: EventEmitter<boolean> = new EventEmitter<boolean>();

  private _isScrolled: boolean | undefined;

  public ngOnInit(): void {
    this.onWindowScroll();
  }

  @HostListener('window:scroll')
  onWindowScroll(): void {
    const isScrolled = window.scrollY > this.maxHeight;
    if (isScrolled !== this._isScrolled) {
      this._isScrolled = isScrolled;
      this.scrollChange.emit(isScrolled);
    }
  }
}
