import { BrowserModule } from '@angular/platform-browser';
import {Injectable, NgModule} from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterComponent } from './register/register.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CustomMaterialModule} from './core/material.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import {AuthenticateService} from './service/authenticate.service';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {LayoutComponent} from './layout/layout.component';
import { CreateSurveyComponent } from './create-survey/create-survey.component';
import { CreatePollComponent } from './create-poll/create-poll.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import {SurveyService} from './service/survey.service';
import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete';
import { AgmCoreModule } from '@agm/core';
import {ExtendedModule} from '@angular/flex-layout';
import {environment} from '../environments/environment';
import {PollService} from './service/poll.service';
import {UserService} from './service/user.service';
import { SurveyDataComponent } from './survey-data/survey-data.component';
import { PollInfoComponent } from './poll-info/poll-info.component';
import {NgxQRCodeModule} from 'ngx-qrcode2';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(xhr);
  }
}

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent,
    CreateSurveyComponent,
    CreatePollComponent,
    MyProfileComponent,
    SurveyDataComponent,
    PollInfoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CustomMaterialModule,
    NgxQRCodeModule,
    MatGoogleMapsAutocompleteModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: environment.GOOGLE_MAPS_API_KEY,
      libraries: ['places']
    }),
    ExtendedModule
  ],
  providers: [
    AuthenticateService,
    SurveyService,
    PollService,
    UserService,
    { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
