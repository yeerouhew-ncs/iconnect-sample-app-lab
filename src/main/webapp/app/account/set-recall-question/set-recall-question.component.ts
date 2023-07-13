import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SetRecallQuestionService } from './set-recall-question.service';

@Component({
  selector: 'ic-set-recall-question',
  templateUrl: './set-recall-question.component.html'
})
export class SetRecallQuestionComponent implements OnInit {
  username: string | undefined;
  resetAccount: any;
  success: any;
  error: any;
  constructor(private route: ActivatedRoute, private router: Router, private setRecallQuestionService: SetRecallQuestionService) {}

  ngOnInit(): void {
    this.resetAccount = {
      recallQuestion: '',
      recallAnswer: '',
      email: ''
    };
    this.route.queryParams.subscribe(params => {
      this.username = params['username'];
    });
  }

  setRecallQuestion(): void {
    this.setRecallQuestionService
      .save({
        username: this.username,
        recallQuestion: this.resetAccount.recallQuestion,
        recallAnswer: this.resetAccount.recallAnswer,
        email: this.resetAccount.email
      })
      .subscribe(
        resp => {
          this.error = null;
          this.success = resp.msg;
        },
        error => {
          this.error = error.error;
        }
      );
  }
}
