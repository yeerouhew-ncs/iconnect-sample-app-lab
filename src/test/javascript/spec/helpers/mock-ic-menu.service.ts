import { SpyObject } from './spyobject';
import { IcMenuService } from 'app/layouts/sidebar/menu/services/menu-service/menu.service';
import Spy = jasmine.Spy;

export class MockIcMenuService extends SpyObject {
  updateMenuByRoutesSpy: Spy;
  fakeResponse: any;

  constructor() {
    super(IcMenuService);

    this.fakeResponse = null;
    this.updateMenuByRoutesSpy = this.spy('updateMenuByRoutes').andReturn(this);
  }

  subscribe(callback: any): any {
    callback(this.fakeResponse);
  }

  setResponse(json: any): void {
    this.fakeResponse = json;
  }
}
