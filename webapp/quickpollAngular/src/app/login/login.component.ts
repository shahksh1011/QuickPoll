import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticateService} from '../service/authenticate.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, private userService: AuthenticateService, private formBuilder: FormBuilder) {
  }

  get f() { return this.loginForm.controls; }

  username: string;
  password: string;
  loginForm: FormGroup;

  @Input() error: string | null;
  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  submit(): void {
    if (this.loginForm.valid) {
      this.username = this.loginForm.controls.username.value;
      this.password = this.loginForm.controls.password.value;
      this.userService.login(this.username, this.password).subscribe(
        res => {
          this.router.navigateByUrl('/');
        },
        error => {
          console.log(error);
          this.loginForm.controls.username.setErrors({error: 'Invalid'});
          this.loginForm.controls.password.setErrors({error: 'Invalid'});
        }
      );
    }
  }

}
