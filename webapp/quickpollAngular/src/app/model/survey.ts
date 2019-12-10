import {Question} from './question';

export class Survey {
  id: string;
  surveyName: string;
  createdBy: string;
  createdDate: Date;
  expiry: Date;
  description: string;
  timeToComplete: string;
  questions: Question[];
}
