import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Survey} from '../model/survey';
import {SurveyService} from '../service/survey.service';

@Component({
  selector: 'app-survey-data',
  templateUrl: './survey-data.component.html',
  styleUrls: ['./survey-data.component.css']
})
export class SurveyDataComponent implements OnInit {
  survey: Survey;
  data: [];
  constructor(private router: Router, private surveyService: SurveyService) {
    this.survey = router.getCurrentNavigation().extras.state.data;
    this.surveyService.getSurveyResponse(this.survey.id).subscribe(
      (res: any) => {
        console.log(res);
        this.data = res;
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnInit() {
  }

}
