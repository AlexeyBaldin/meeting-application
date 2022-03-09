import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import {HomeComponent} from './component/home/home.component';
import {AppRoutingModule} from "./app-roating-module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Interceptor} from "./security/interceptor";
import {AuthGuard} from "./security/auth-guard";
import { ProfileComponent } from './component/profile/profile.component';
import { RegistrationComponent } from './component/registration/registration.component';
import {NgbAlertModule, NgbDatepickerModule, NgbTimepickerModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import { NewMeetingComponent } from './component/new-meeting/new-meeting.component';
import {DayPipe} from "./pipe/custom-date-pipes";
import { AdminOfficeComponent } from './component/admin-office/admin-office.component';
import { AdminMeetingComponent } from './component/admin-meeting/admin-meeting.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProfileComponent,
    RegistrationComponent,
    NewMeetingComponent,
    DayPipe,
    AdminOfficeComponent,
    AdminMeetingComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        NgbAlertModule,
        NgbDatepickerModule,
        NgbTimepickerModule,
        NgbTypeaheadModule
    ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Interceptor,
      multi: true,
    },
    AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
