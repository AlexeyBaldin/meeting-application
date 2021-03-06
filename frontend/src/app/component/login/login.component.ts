import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {HaveAlert} from "../../model/have-alert";
import {Encrypter} from "../../util/encrypter";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends HaveAlert implements OnInit {

  username: string;
  password: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) {
    super();
  }

  ngOnInit(): void {
    if(this.authService.expire) {
      this.alerts.push({
        type: 'warning',
        message: 'Token was expire. Please login again.'
      })
      this.authService.logout();
    }
  }


  onSubmit() {
    this.authService.login(this.username, Encrypter.encryptPassword(this.password)).subscribe(() => this.goToHomePage(this.router));
  }

  goToHomePage(router: Router) {
    router.navigate(['']);
  }

}
