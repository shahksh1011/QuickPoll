import { Component, OnInit } from '@angular/core';
import {AuthenticateService} from '../service/authenticate.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
  user: {};
  constructor(private authenticateService: AuthenticateService) { }

  ngOnInit() {
    this.user = this.authenticateService.currentUserValue();
  }

  logout() {
      console.log('Logout called');
      this.authenticateService.logout();
  }
}
