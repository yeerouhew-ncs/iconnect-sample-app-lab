import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgIConnectModule } from 'app/ng-iconnect';

@NgModule({
  imports: [
    NgIConnectModule,
    RouterModule.forChild([
      {
        path: 'acm',
        loadChildren: () => import('app/admin/access-control/access-control.module').then(m => m.IcAccessControlModule),
        data: {
          pageTitle: 'acmAdmin.home.title'
        }
      },
      {
        path: 'audits',
        loadChildren: () => import('./audits/audits.module').then(m => m.AuditsModule)
      },
      {
        path: 'configuration',
        loadChildren: () => import('./configuration/configuration.module').then(m => m.ConfigurationModule)
      },
      {
        path: 'docs',
        loadChildren: () => import('./docs/docs.module').then(m => m.DocsModule)
      },
      {
        path: 'health',
        loadChildren: () => import('./health/health.module').then(m => m.HealthModule)
      },
      {
        path: 'logs',
        loadChildren: () => import('./logs/logs.module').then(m => m.LogsModule)
      },
      {
        path: 'metrics',
        loadChildren: () => import('./metrics/metrics.module').then(m => m.MetricsModule)
      },
      {
        path: 'ic-entityaudit',
        loadChildren: () => import('app/admin/entity-audit/entityaudit.module').then(m => m.EntityAuditModule)
      },
      {
        path: 'ic-codetype',
        loadChildren: () => import('app/admin/code-admin/code-admin.module').then(m => m.IcCodeAdminModule)
      },
      {
        path: 'ic-code',
        loadChildren: () => import('app/admin/code-admin/code/code.module').then(m => m.CodeModule)
      },
      {
        path: 'ic-approvalTemplate',
        loadChildren: () => import('app/admin/approval-template/approval-template.module').then(m => m.IcApprovalTemplateModule)
      },
      {
        path: 'ic-param',
        loadChildren: () => import('app/admin/param-admin/param-admin.module').then(m => m.IconnectParamAdminModule)
      }
    ])
  ]
})
export class AdminRoutingModule {}
