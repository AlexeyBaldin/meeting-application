import { Component } from '@angular/core';
import {AuthService} from "./service/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Meeting Application Frontend';

  constructor(public authService: AuthService, private router: Router) {
  }

  logout(): void {
    this.authService.logout();
    console.log('logout');
  }
}
