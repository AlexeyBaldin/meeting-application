import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {EmployeeService} from "../../service/employee.service";
import {timeout} from "rxjs";
import {Employee} from "../../model/employee";
import {HaveAlert} from "../../model/have-alert";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent  extends HaveAlert implements OnInit {


  username: string | null;
  password: string = "";
  repeatPassword: string = "";
  employee: Employee = new Employee({id: 0, officeId: 0, name: '', position: '', email: ''});

  constructor(public authService: AuthService,
              private router: Router,
              private employeeService: EmployeeService) {
    super();
  }

  ngOnInit(): void {
    if(this.authService.checkExpire()) {
      this.router.navigate(['login']);
    } else {

      this.username = localStorage.getItem("username");
      this.employeeService.getEmployee(localStorage.getItem("id")).subscribe((employee) => {
        this.employee = employee;
      });
    }
    console.log("init profile");
  }

  checkPasswords(): boolean {
    return this.password == this.repeatPassword && this.password.length >= 5;
  }

  changePassword() : void {
    console.log("change password");

    this.employeeService.changePassword(localStorage.getItem("id"), this.password).subscribe(() => {
      this.password = '';
      this.repeatPassword = '';
      this.alerts.push({
        type: 'success',
        message: 'Password was changed!'
      });
    })
  }


}
