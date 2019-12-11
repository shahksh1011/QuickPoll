import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Survey} from '../model/survey';

@Injectable()
export class SurveyService {

  constructor(private http: HttpClient, private router: Router) {
  }

  createSurvey(survey: Survey) {
    return this.http.post('http://localhost:3000/api/v1/survey/create', {survey});
  }

  getSurveyResponse(surveyId: string) {
    return this.http.post('http://localhost:3000/api/v1/survey/response-data', {surveyId});
  }

  getSurveyDataById(surveyId: string) {
    return this.http.post('http://localhost:3000/api/v1/survey/get-data', {surveyId});
  }
}
