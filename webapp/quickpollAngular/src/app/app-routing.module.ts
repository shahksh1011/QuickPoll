import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LayoutComponent} from './layout/layout.component';
import {AuthGuard} from './auth.guard';
import {CreateSurveyComponent} from './create-survey/create-survey.component';
import {CreatePollComponent} from './create-poll/create-poll.component';
import {MyProfileComponent} from './my-profile/my-profile.component';
import {SurveyDataComponent} from './survey-data/survey-data.component';
import {PollInfoComponent} from './poll-info/poll-info.component';


const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {path: '', component: DashboardComponent, canActivate: [AuthGuard]},
      {path: 'create-survey', component: CreateSurveyComponent, canActivate: [AuthGuard]},
      {path: 'create-poll', component: CreatePollComponent, canActivate: [AuthGuard]},
      {path: 'my-profile', component: MyProfileComponent, canActivate: [AuthGuard]},
      {path: 'survey/:id', component: SurveyDataComponent, canActivate: [AuthGuard]},
      {path: 'poll/:id', component: PollInfoComponent, canActivate: [AuthGuard]},
    ]
  },
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
