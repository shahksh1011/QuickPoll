import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Survey} from '../model/survey';
import {SurveyService} from '../service/survey.service';
import {Subscription} from 'rxjs';
import { Angular5Csv } from 'angular5-csv/dist/Angular5-csv';

@Component({
  selector: 'app-survey-data',
  templateUrl: './survey-data.component.html',
  styleUrls: ['./survey-data.component.css']
})
export class SurveyDataComponent implements OnInit {
  survey: Survey;
  data: any[] = [];
  private routeSub: Subscription;

  constructor(private router: Router, private surveyService: SurveyService, private route: ActivatedRoute) {

  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      if (params && params.hasOwnProperty('id')) {
        this.surveyService.getSurveyDataById(params.id).subscribe((survey: Survey) => {
          this.survey = survey;
          this.surveyService.getSurveyResponse(this.survey.id).subscribe(
            (res: any) => {
              console.log(res);
              const keys = Object.keys(res);
              for (const prop of keys) {
                this.data.push(res[prop]);
              }
              console.log(this.data);
            },
            error => {
              console.log(error);
            }
          );
        });
      }
    });
  }

  downloadCSVData() {
    const keys = Object.keys(this.data[0]);
    var options = {
      fieldSeparator: ',',
      showLabels: true,
      headers: keys
    };
    new Angular5Csv(this.data, 'data', options);
  }

}
