import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PollService} from '../service/poll.service';
import {Poll} from '../model/poll';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-poll-info',
  templateUrl: './poll-info.component.html',
  styleUrls: ['./poll-info.component.css']
})
export class PollInfoComponent implements OnInit {
  poll: Poll;
  pollId: string;
  private routeSub: Subscription;

  constructor(private router: Router, private pollService: PollService,  private route: ActivatedRoute) {

  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      if (params && params.hasOwnProperty('id')) {
        this.pollService.getPollDataById(params.id).subscribe((data: Poll) => {
          this.poll = data;
        });
      }
    });
  }

}
