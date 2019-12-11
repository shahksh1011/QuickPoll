import {Component, OnInit} from '@angular/core';
import {AuthenticateService} from '../service/authenticate.service';
import {Router} from '@angular/router';
import {User} from '../model/user';
import {UserService} from '../service/user.service';


@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {
  currentUserValue: User;
  user: User;
  displayedColumnsSurvey: string[] = ['id', 'name', 'description', 'timeToComplete', 'expiryDate', 'createdDate'];
  displayedColumnsPoll: string[] = ['id', 'name', 'description', 'createdDate'];

  constructor(private authenticateService: AuthenticateService, private router: Router, private userService: UserService) {}


  ngOnInit() {
    this.currentUserValue = this.authenticateService.currentUserValue();
    this.fetchData();
  }

  fetchData() {
    this.userService.fetchUserProfile(this.currentUserValue.id).subscribe(
      (res: User) => {
          console.log(res);
          this.user = res;
        },
        error => {
          console.log(error);
        }
      );
  }

}
