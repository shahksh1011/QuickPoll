import { Injectable } from '@angular/core';
import {User} from '../model/user';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {map} from 'rxjs/operators';

@Injectable()
export class AuthenticateService {
  private user: User;
  authenticated = false;
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;
  constructor(private http: HttpClient, private router: Router) {
    this.user = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string) {
    console.log(email + ' ' + password);
    return this.http.post( 'http://localhost:3000/auth/login', {email, password})
      .pipe(map((response: any) => {
      // store user details and basic auth credentials in local storage to keep user logged in between page refreshes
      console.log(response);
      localStorage.setItem('currentUser', JSON.stringify(response.user));
      this.currentUserSubject.next(response.user);
      return response.user;
    }));
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  createNewUser(user: User) {
    return this.http.post( 'http://localhost:3000/auth/register', {user})
      .pipe(map((response: any) => {
        // store user details and basic auth credentials in local storage to keep user logged in between page refreshes
        console.log(response);
        localStorage.setItem('currentUser', JSON.stringify(response.user));
        this.currentUserSubject.next(response.user);
        return response.user;
      }));
  }
}
