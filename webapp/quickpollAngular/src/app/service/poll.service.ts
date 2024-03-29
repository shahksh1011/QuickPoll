import { Injectable } from '@angular/core';
import {Poll} from '../model/poll';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class PollService {

  constructor(private http: HttpClient) { }

  createPoll(poll: Poll) {
    return this.http.post('http://localhost:3000/api/v1/poll/create', {poll});
  }

  getPollDataById(pollId: string) {
    return this.http.post('http://localhost:3000/api/v1/poll/get-data', {pollId});
  }

  getPollResponse(pollId: string) {
    return this.http.post('http://localhost:3000/api/v1/poll/response-data', {pollId});
  }
}
