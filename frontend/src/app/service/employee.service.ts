import {map, Observable} from "rxjs";
import {ResponseMap} from "../model/response-map";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {Employee} from "../model/employee";
import {User} from "../model/user";
import {Encrypter} from "../util/encrypter";


@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient, public authService: AuthService) { }

  changePassword(employeeId: string | null, password: string) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.forAllUrl + 'user/' + employeeId,
      {password: Encrypter.encryptPassword(password)}).pipe(map(response=> {
        return response;
      }));
  }

  getEmployee(employeeId: string | null) : Observable<Employee> {
    return this.httpClient.get<Employee>(environment.forAllUrl + 'employee/' + employeeId)
      .pipe(map(employee => {
        return new Employee(employee);
      }))
  }

  getUser(employeeId: number) : Observable<User> {
    return this.httpClient.get<User>(environment.adminUrl + 'user/' + employeeId)
      .pipe(map(user => {
        return new User(user);
      }))
  }

  getAllEmployees(): Observable<Employee[]> {
    return this.httpClient.get<Employee[]>(environment.forAllUrl + 'employee/all')
      .pipe(map(employees => {
        return employees.map((employee) => {
          return new Employee(employee);
        });
      }))
  }

  createNewEmployee(employee: Employee) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'employee/register', employee)
      .pipe(map(response => {
        return response;
      }))
  }

  editEmployee(employee: Employee) : Observable<ResponseMap> {
    return this.httpClient.put<ResponseMap>(environment.adminUrl + 'employee/' + employee.id, employee)
      .pipe(map(response => {
        return response;
      }))
  }

  editUser(id: number ,username: string, password: string) {
    let hashString = Encrypter.encryptPassword(password);

    let user: User = new User({id: id, username: username, password: hashString, activation: true});

    return this.httpClient.put<any>(environment.forAllUrl + 'user/' + id, user)
      .pipe(map(response=> {
        return response;
      }));
  }

  deleteEmployee(id: number) : Observable<ResponseMap> {
    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'employee/' + id)
      .pipe(map(response => {
        return response;
      }));
  }
}

