import {Question} from './question';

export class Poll {
  id: string;
  pollName: string;
  createdBy: string;
  createdDate: Date;
  description: string;
  questions: Question[];
  displayQuestion: number;
  lockQuestion: boolean;
}
