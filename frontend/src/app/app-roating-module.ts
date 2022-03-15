import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {HomeComponent} from "./component/home/home.component";
import {LoginComponent} from "./component/login/login.component";
import {AuthGuard} from "./security/auth-guard";
import {ProfileComponent} from "./component/profile/profile.component";
import {NewMeetingComponent} from "./component/new-meeting/new-meeting.component";
import {RegistrationComponent} from "./component/registration/registration.component";
import {AdminOfficeComponent} from "./component/admin-office/admin-office.component";
import {AdminMeetingComponent} from "./component/admin-meeting/admin-meeting.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'new-meeting', component: NewMeetingComponent, canActivate: [AuthGuard] },
  { path: 'employees', component: RegistrationComponent, canActivate: [AuthGuard] },
  { path: 'offices', component: AdminOfficeComponent, canActivate: [AuthGuard] },
  { path: 'meetings', component: AdminMeetingComponent, canActivate: [AuthGuard] },


  { path: '**', redirectTo: '' }
]


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
