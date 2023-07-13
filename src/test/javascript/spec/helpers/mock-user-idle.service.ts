import { SpyObject } from './spyobject';
import Spy = jasmine.Spy;
import { UserIdleService } from 'app/shared/login/user-idle.service';

export class MockUserIdleService extends SpyObject {
  setUserIdleSpy: Spy;
  fakeResponse: any;

  constructor() {
    super(UserIdleService);
    this.fakeResponse = null;
    this.setUserIdleSpy = this.spy('setUserIdle').andReturn(this);
  }

  subscribe(callback: any): any {
    callback(this.fakeResponse);
  }

  setResponse(json: any): void {
    this.fakeResponse = json;
  }
}
