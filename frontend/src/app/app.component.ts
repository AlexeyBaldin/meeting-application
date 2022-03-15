import { Component } from '@angular/core';
import {AuthService} from "./service/auth.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Meeting Application Frontend';

  constructor(public authService: AuthService) {
  }

  logout(): void {
    this.authService.logout();
    console.log('logout');
  }
}
