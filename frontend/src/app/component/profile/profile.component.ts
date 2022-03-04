import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {EmployeeService} from "../../service/employee.service";
import {timeout} from "rxjs";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  username: string | null;
  password: string = "";
  repeatPassword: string = "";

  message: string = "";

  constructor(public authService: AuthService, private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.username = localStorage.getItem("username");
    console.log("init profile");
  }

  checkPasswords(): boolean {
    return this.password == this.repeatPassword && this.password.length >= 5;
  }

  changePassword() : void {
    console.log("change password");

    this.employeeService.changePassword(localStorage.getItem("id"), this.password).subscribe(() => {
      this.message = 'Password was changed!';
      this.password = '';
      this.repeatPassword = '';
      setTimeout(() => {
        this.message = '';
      }, 3000);
    })


  }

}
