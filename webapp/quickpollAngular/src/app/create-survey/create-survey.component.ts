import {Component, OnInit} from '@angular/core';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {MatCheckboxChange, MatChipInputEvent, MatSnackBar} from '@angular/material';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Question} from '../model/question';
import {Survey} from '../model/survey';
import {User} from '../model/user';
import {AuthenticateService} from '../service/authenticate.service';
import {SurveyService} from '../service/survey.service';
import {Router} from '@angular/router';
import {Location, Appearance} from '@angular-material-extensions/google-maps-autocomplete';
import PlaceResult = google.maps.places.PlaceResult;

@Component({
  selector: 'app-create-survey',
  templateUrl: './create-survey.component.html',
  styleUrls: ['./create-survey.component.css']
})
export class CreateSurveyComponent implements OnInit {
  displayCard = false;
  displayCardData: any = {};
  cardViewData = [
    {
      title: 'Add Single Choice Survey Question',
      note: 'This question will allow user to select one option from all the available options.',
      type: 'Single'
    },
    {
      title: 'Add Multiple Choice Survey Question',
      note: 'This question will allow user to select multiple options from all the available options.',
      type: 'Multiple'
    },
    {
      title: 'Add Text Based Survey Question',
      note: 'This question will allow user to enter any text as their answer to the survey question.',
      type: 'Text'
    }
  ];

  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  options: string[] = [];
  questions: Question[] = [];
  surveyQuestionForm: FormGroup;
  surveyDetailsForm: FormGroup;
  survey: Survey;
  user: User;

  public appearance = Appearance;
  public zoom: number;
  public latitude: number;
  public longitude: number;
  public selectedAddress: PlaceResult;
  locationFlag: boolean;

  constructor(private formBuilder: FormBuilder, private authenticateService: AuthenticateService,
              private surveyService: SurveyService, private router: Router, private snackBar: MatSnackBar) {
    this.surveyQuestionForm = formBuilder.group({
      question: formBuilder.control('', Validators.required)
    });
    this.surveyDetailsForm = formBuilder.group({
      surveyName: formBuilder.control('', Validators.required),
      surveyDescription: formBuilder.control('', Validators.required),
      surveyTime: formBuilder.control('', Validators.required),
      surveyExpiryDate: formBuilder.control('', Validators.required),
      surveyRadius: formBuilder.control(''),
      locationFlag: formBuilder.control(false)
    });
    this.user = authenticateService.currentUserValue();
  }

  ngOnInit() {
    this.surveyQuestionForm.reset({question: ''});
    this.surveyDetailsForm.reset({
      surveyName: '',
      surveyDescription: '',
      surveyTime: '',
      surveyExpiryDate: '',
      surveyRadius: '',
      locationFlag: false
    });
    this.survey = new Survey();

    this.zoom = 10;
    this.latitude = 52.520008;
    this.longitude = 13.404954;

    this.setCurrentPosition();
    this.locationFlag = false;
  }


  showCard(type: string) {
    if (type === 'Single') {
      this.displayCardData = this.cardViewData[0];
    } else if (type === 'Multiple') {
      this.displayCardData = this.cardViewData[1];
    } else if (type === 'Text') {
      this.displayCardData = this.cardViewData[2];
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
    } else if (this.surveyQuestionForm.valid) {
      const question = new Question();
      question.name = this.surveyQuestionForm.controls.question.value;
      question.options = this.options;
      question.type = this.displayCardData.type;
      console.log(question);
      this.questions.push(question);
      this.displayCard = false;
      this.displayCardData = {};
      this.surveyQuestionForm.reset({question: ''});
      this.options = [];
    }
  }

  submitDetails() {
    if (this.surveyDetailsForm.valid) {
      this.survey.surveyName = this.surveyDetailsForm.controls.surveyName.value;
      this.survey.description = this.surveyDetailsForm.controls.surveyDescription.value;
      this.survey.timeToComplete = this.surveyDetailsForm.controls.surveyTime.value;
      this.survey.expiry = this.surveyDetailsForm.controls.surveyExpiryDate.value;
      this.survey.createdBy = this.user.id;
      this.survey.questions = this.questions;
      this.survey.createdDate = new Date();
      const lFlag = this.surveyDetailsForm.controls.locationFlag.value;
      if (lFlag) {
        this.survey.location = {
          latitude: this.latitude,
          longitude: this.longitude,
          radius: this.surveyDetailsForm.controls.surveyRadius.value
        };
      } else {
        this.survey.location = null;
      }
      console.log(this.survey);
      this.surveyService.createSurvey(this.survey).subscribe(
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

  private setCurrentPosition() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        this.zoom = 12;
      });
    }
  }

  onAutocompleteSelected(result: PlaceResult) {
    console.log('onAutocompleteSelected: ', result);
  }

  onLocationSelected(location: Location) {
    console.log('onLocationSelected: ', location);
    this.latitude = location.latitude;
    this.longitude = location.longitude;
  }

  onCheckChange($event: MatCheckboxChange) {
    this.locationFlag = $event.checked;
  }
}
