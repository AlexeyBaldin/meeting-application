import {map, Observable} from "rxjs";
import {ResponseMap} from "../model/response-map";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient, public authService: AuthService, private router: Router) { }

  changePassword(employeeId: string | null, password: string) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.mainUrl + 'user/' + employeeId, {password})
      .pipe(map(response=> {
        return response;
      }));
  }

}

