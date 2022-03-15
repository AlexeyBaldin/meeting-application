import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {EmployeeService} from "../../service/employee.service";
import {debounceTime, distinctUntilChanged, map, Observable} from "rxjs";
import {Employee} from "../../model/employee";
import {HaveAlert} from "../../model/have-alert";
import {Router} from "@angular/router";


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent extends HaveAlert implements OnInit {


  employees: Employee[] = [];
  foundEmployee: Employee;


  employeeId: number = 0;
  employeeName: string = '';
  officeId: number = 0;
  position: string = '';
  email: string = '';
  username: string = '';
  password: string = '';
  repeatPassword: string = '';


  constructor(public authService: AuthService,
              private router: Router,
              private employeeService: EmployeeService) {
    super();
  }

  ngOnInit(): void {
    if(this.authService.checkExpire()) {
      this.router.navigate(['login']);
    } else {
      this.employeeService.getAllEmployees().subscribe(employees => {
        this.employees = employees;
      }, (error) => {
        console.log(error);
      });
    }
  }

  searchEmployee: (text$: Observable<string>) => Observable<Employee[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => term.length < 2 ? []
        : this.employees.filter(v => v.name.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )


  enterEmployee() : void {
    if(this.foundEmployee instanceof Employee) {
      this.employeeId = this.foundEmployee.id
      this.employeeName = this.foundEmployee.name;
      this.officeId = this.foundEmployee.officeId
      this.position = this.foundEmployee.position;
      this.email = this.foundEmployee.email;

      this.employeeService.getUser(this.employeeId).subscribe(user => {
        this.username = user.username;
      })
    }

  }


  reset() : void {
    this.employeeId = 0;
    this.employeeName = '';
    this.officeId = 0;
    this.position = '';
    this.email = '';
    this.username = '';
    this.password = '';
    this.repeatPassword = '';

    this.employeeService.getAllEmployees().subscribe(employees => {
      this.employees = employees;
    }, (error) => {
      console.log(error);
    });
  }

  checkEmployeeData() : boolean {
    return this.officeId < 1 || this.employeeName == '' || this.position == '';
  }

  checkUserData() : boolean {
    return this.employeeId < 1 || this.username == '' || this.password != this.repeatPassword || this.password.length < 5;
  }

  createNewEmployee() : void {
    this.employeeService.createNewEmployee(
      {id: 0, name: this.employeeName, email: this.email, position: this.position, officeId: this.officeId})
      .subscribe((response) => {
        this.alerts.push({
          type: 'success',
          message: 'New employee was registered! Login: ' + response['login']
        });
      }, (error) => {
        let errorMap = error['error'];
        for (const errorMapElement in errorMap) {
          if (errorMapElement != 'success') {
            this.alerts.push({
              type: 'warning',
              message: errorMapElement + ': ' + errorMap[errorMapElement]
            });
          }
        }
      });
  }

  editEmployee() : void {
    this.employeeService.editEmployee(
      {id: this.employeeId, name: this.employeeName, email: this.email, position: this.position, officeId: this.officeId})
      .subscribe(() => {
        this.alerts.push({
          type: 'success',
          message: 'Success edit employee data'
        });
      }, (error) => {
        let errorMap = error['error'];
        for (const errorMapElement in errorMap) {
          if (errorMapElement != 'success') {
            this.alerts.push({
              type: 'warning',
              message: errorMapElement + ': ' + errorMap[errorMapElement]
            });
          }
        }
      });
  }

  editUser() : void {
    this.employeeService.editUser(this.employeeId, this.username, this.password)
      .subscribe(() => {
        this.alerts.push({
          type: 'success',
          message: 'Success edit user data'
        });
      }, (error) => {
        let errorMap = error['error'];
        for (const errorMapElement in errorMap) {
          if (errorMapElement != 'success') {
            this.alerts.push({
              type: 'warning',
              message: errorMapElement + ': ' + errorMap[errorMapElement]
            });
          }
        }
      });
  }

  deleteEmployee() : void {
    this.employeeService.deleteEmployee(this.employeeId).subscribe(() => {
      this.alerts.push({
        type: "success",
        message: "Employee was deleted"
      });
      this.reset();

    },(error) => {
      let errorMap = error['error'];
      for (const errorMapElement in errorMap) {
        if (errorMapElement != 'success') {
          this.alerts.push({
            type: 'warning',
            message: errorMapElement + ': ' + errorMap[errorMapElement]
          });
        }
      }
    });
  }
}
