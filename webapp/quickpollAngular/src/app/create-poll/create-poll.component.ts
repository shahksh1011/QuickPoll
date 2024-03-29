import {Component, OnInit} from '@angular/core';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {Question} from '../model/question';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../model/user';
import {Poll} from '../model/poll';
import {AuthenticateService} from '../service/authenticate.service';
import {Router} from '@angular/router';
import {PollService} from '../service/poll.service';
import {MatChipInputEvent, MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-create-poll',
  templateUrl: './create-poll.component.html',
  styleUrls: ['./create-poll.component.css']
})
export class CreatePollComponent implements OnInit {
  displayCard = false;
  displayCardData: any = {};
  cardViewData = [
    {
      title: 'Add Single Choice Poll Question',
      note: 'This question will allow user to select one option from all the available options.',
      type: 'Single'
    },
    {
      title: 'Add Multiple Choice Poll Question',
      note: 'This question will allow user to select multiple options from all the available options.',
      type: 'Multiple'
    }
  ];

  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  options: string[] = [];
  questions: Question[] = [];
  pollQuestionForm: FormGroup;
  pollDetailsForm: FormGroup;
  poll: Poll;
  user: User;

  constructor(private formBuilder: FormBuilder, private authenticateService: AuthenticateService,
              private pollService: PollService, private router: Router, private snackBar: MatSnackBar) {
    this.pollQuestionForm = formBuilder.group({
      question: formBuilder.control('', Validators.required)
    });
    this.pollDetailsForm = formBuilder.group({
      pollName: formBuilder.control('', Validators.required),
      pollDescription: formBuilder.control('', Validators.required)
    });
    this.user = authenticateService.currentUserValue();
  }

  ngOnInit() {
    this.pollQuestionForm.reset({question: ''});
    this.pollDetailsForm.reset({
      pollName: '',
      pollDescription: ''
    });
    this.poll = new Poll();
  }

  showCard(type: string) {
    if (type === 'Single') {
      this.displayCardData = this.cardViewData[0];
    } else if (type === 'Multiple') {
      this.displayCardData = this.cardViewData[1];
    }
    this.displayCard = true;
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.options.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(option: string): void {
    const index = this.options.indexOf(option);

    if (index >= 0) {
      this.options.splice(index, 1);
    }
  }

  onSubmit() {
    if (this.options.length < 2 && this.displayCardData.type !== 'Text') {
      this.snackBar.open('Please select some options', 'Close', {
        duration: 2000,
      });
    } else if (this.pollQuestionForm.valid) {
      const question = new Question();
      question.name = this.pollQuestionForm.controls.question.value;
      question.options = this.options;
      question.type = this.displayCardData.type;
      console.log(question);
      this.questions.push(question);
      this.displayCard = false;
      this.displayCardData = {};
      this.pollQuestionForm.reset({question: ''});
      this.options = [];
    }
  }

  submitDetails() {
    if (this.pollDetailsForm.valid) {
      this.poll.pollName = this.pollDetailsForm.controls.pollName.value;
      this.poll.description = this.pollDetailsForm.controls.pollDescription.value;
      this.poll.createdBy = this.user.id;
      this.poll.questions = this.questions;
      this.poll.createdDate = new Date();
      this.poll.displayQuestion = -1;
      this.poll.lockQuestion = true;
      console.log(this.poll);
      this.pollService.createPoll(this.poll).subscribe(
        res => {
          console.log(res);
          this.router.navigateByUrl('/');
        },
        error => {
          console.log(error);
        }
      );
    }
  }

}
