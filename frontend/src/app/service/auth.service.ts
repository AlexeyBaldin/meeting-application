import {Injectable} from "@angular/core";
import {LoginResponse} from "../model/login-response";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) {
  }

  public loginResponse: LoginResponse;
  public auth: boolean = false;


  login(username: string, password: string) {

    return this.httpClient.post<LoginResponse>(environment.mainUrl + 'auth/login', {username, password})
      .pipe(
        map(
          response => {
            console.log(123);
            localStorage.setItem('token', response.token);
            localStorage.setItem('username', response.username);
            localStorage.setItem('admin', String(response.admin));
            localStorage.setItem('id', String(response.id));
            this.loginResponse = response;
            this.auth = true;
            console.log("login");
            return response;
          }
        ));


  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('admin');
    localStorage.removeItem('id');

    this.auth = false;
  }

}
