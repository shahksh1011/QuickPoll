import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthenticateService} from './authenticate.service';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user: User;

  constructor(private http: HttpClient, private authenticateService: AuthenticateService) {
    this.user = authenticateService.currentUserValue();
  }

  fetchUserProfile(userId: string) {
    return this.http.post('http://localhost:3000/api/v1/user/profile', {userId});
  }
}
