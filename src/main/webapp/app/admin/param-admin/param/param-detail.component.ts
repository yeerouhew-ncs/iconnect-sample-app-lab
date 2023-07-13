import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { ParamModel } from './param.model';
import { ParamService } from './param.service';

@Component({
  selector: 'ic-param-detail',
  templateUrl: './param-detail.component.html'
})
export class ParamDetailComponent implements OnInit, OnDestroy {
  param: ParamModel;
  private subscription: Subscription;
  private eventSubscriber: Subscription;

  constructor(
    private eventManager: JhiEventManager,
    private paramService: ParamService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscription = this.route.params.subscribe(params => {
      this.load(params['appId'], params['paramKey']);
    });
    this.registerChangeInParams();
  }

  load(appId, paramKey): void {
    this.paramService.find(appId, paramKey).subscribe(param => {
      this.paramService.getParamTypes().forEach(paramType => {
        if (param.paramType === paramType.codeId) {
          param.paramTypeDesc = paramType.label;
        }
      });
      this.param = param;
    });
  }

  previousState(): void {
    this.router.navigate(['admin/ic-param']);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInParams(): void {
    this.eventSubscriber = this.eventManager.subscribe('paramListModification', () => this.load(this.param.appId, this.param.paramKey));
  }
}
