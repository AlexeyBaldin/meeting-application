import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) { }

  ngOnInit(): void {
    console.log("init login");
    //========================
    this.authService.login('admin', 'admin').subscribe(() => this.goToHomePage(this.router));
    //========================

  }

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe(() => this.goToHomePage(this.router));
  }

  goToHomePage(router: Router) {
    router.navigate(['']);
  }

}
