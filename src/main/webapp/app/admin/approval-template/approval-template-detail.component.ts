import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { ApprovalTemplate } from 'app/admin/approval-template/approval-template.model';
import { ApprovalTemplateService } from 'app/admin/approval-template/approval-template.service';

@Component({
  selector: 'ic-approval-template-detail',
  templateUrl: './approval-template-detail.component.html'
})
export class ApprovalTemplateDetailComponent implements OnInit, OnDestroy {
  approvalTemplate: ApprovalTemplate;
  private subscription: Subscription;
  private eventSubscriber: Subscription;

  constructor(
    private eventManager: JhiEventManager,
    private approvalTemplateService: ApprovalTemplateService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscription = this.route.params.subscribe(params => {
      this.load(params['id']);
    });
    this.registerChangeInApprovalTemplate();
  }

  load(id): void {
    this.approvalTemplateService.find(id).subscribe(param => {
      this.approvalTemplate = param;
    });
  }

  previousState(): void {
    this.router.navigate(['admin/ic-approvalTemplate']);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInApprovalTemplate(): void {
    this.eventSubscriber = this.eventManager.subscribe('approvalTemplateListModification', () => this.load(this.approvalTemplate.id));
  }
}
