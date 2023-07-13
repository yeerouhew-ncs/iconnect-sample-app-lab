import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class GlobalState {
  private _data = new Subject<Object>();
  private _dataStream$ = this._data.asObservable();

  private _subscriptions: Map<string, Array<Function>> = new Map<string, Array<Function>>();

  constructor() {
    this._dataStream$.subscribe(data => this._onEvent(data));
  }

  notifyDataChanged(event: string, value: boolean): void {
    const current = this._data[event];
    if (current !== value) {
      this._data[event] = value;

      this._data.next({
        /* tslint:disable:object-literal-shorthand */
        event,
        data: this._data[event]
      });
    }
  }

  subscribe(event: string, callback: Function): void {
    const subscribers = this._subscriptions.get(event) || [];
    subscribers.push(callback);

    this._subscriptions.set(event, subscribers);
  }

  _onEvent(data: any): void {
    const subscribers = this._subscriptions.get(data['event']) || [];

    subscribers.forEach(callback => {
      callback.call(null, data['data']);
    });
  }
}
