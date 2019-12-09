import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormControl, Validators} from '@angular/forms';
import {User} from '../model/user';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticateService} from '../service/authenticate.service';
import {PasswordMatch} from '../helpers/password-match.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  signUpForm: FormGroup;
  user: User;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private route: ActivatedRoute,
              private router: Router, private authenticateService: AuthenticateService) {
    this.user = new User();
  }

  onSubmit() {
    this.submitted = true;
    if (this.signUpForm.invalid) {
      return;
    } else {
      this.user.firstName = this.signUpForm.controls.firstName.value;
      this.user.lastName = this.signUpForm.controls.lastName.value;
      this.user.email = this.signUpForm.controls.email.value;
      this.user.phoneNumber = this.signUpForm.controls.phone.value;
      this.user.password = this.signUpForm.controls.password.value;
      this.authenticateService.createNewUser(this.user).subscribe(result => this.goHome());
    }
  }

  goHome() {
    this.router.navigate(['/']);
  }

  ngOnInit() {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('[0-9]+'), Validators.maxLength(10), Validators.minLength(10)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, {
      validator: PasswordMatch('password', 'confirmPassword')
    });
  }

  get f() {
    return this.signUpForm.controls;
  }

}
