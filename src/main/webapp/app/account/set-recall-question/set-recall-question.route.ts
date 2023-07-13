import { Route } from '@angular/router';

import { SetRecallQuestionComponent } from './set-recall-question.component';

export const setRecallQuestionRoute: Route = {
  path: 'password/recall',
  component: SetRecallQuestionComponent,
  data: {
    authorities: [],
    pageTitle: 'password.menu.account.recall'
  }
};
