import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PollService} from '../service/poll.service';
import {Poll} from '../model/poll';
import {Subscription} from 'rxjs';
import { Angular5Csv } from 'angular5-csv/dist/Angular5-csv';

@Component({
  selector: 'app-poll-info',
  templateUrl: './poll-info.component.html',
  styleUrls: ['./poll-info.component.css']
})
export class PollInfoComponent implements OnInit {
  poll: Poll;
  pollId: string;
  private routeSub: Subscription;
  data: any[] = [];

  constructor(private router: Router, private pollService: PollService,  private route: ActivatedRoute) {

  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      if (params && params.hasOwnProperty('id')) {
        this.pollService.getPollDataById(params.id).subscribe((dataPoll: Poll) => {
          this.poll = dataPoll;
          this.pollService.getPollResponse(this.poll.id).subscribe(
            (res: any) => {
              console.log(res);
              const keys = Object.keys(res);
              for (const prop of keys) {
                res[prop].userId = prop;
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
