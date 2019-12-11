import {Question} from './question';
import {Location} from './location';

export class Survey {
  id: string;
  surveyName: string;
  createdBy: string;
  createdDate: Date;
  expiry: Date;
  description: string;
  timeToComplete: string;
  questions: Question[];
  location: Location;
}
