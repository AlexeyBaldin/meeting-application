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
    this.auth = localStorage.getItem('expire') != null && !this.checkExpire();
    if(this.auth) {
      this.username = String(localStorage.getItem('username'));
      this.admin = Boolean(localStorage.getItem('admin'));
    }
  }

  public username: string = '';
  public loginResponse: LoginResponse;
  public auth: boolean = false;
  public expire: boolean = false;
  public admin: boolean = false;


  login(username: string, password: string) {

    return this.httpClient.post<LoginResponse>(environment.loginUrl, {username, password})
      .pipe(
        map(response => {
            console.log(123);
            localStorage.setItem('token', response.token);
            localStorage.setItem('username', response.username);
            localStorage.setItem('admin', String(response.admin));
            localStorage.setItem('id', String(response.id));
            localStorage.setItem('expire', String(response.expire));
            this.loginResponse = response;
            this.username = this.loginResponse.username;
            this.auth = true;
            this.admin = response.admin;
            console.log("login");
            return response;
          }
        ));


  }

  checkExpire() : boolean {
    if(new Date(String(localStorage.getItem('expire'))).getTime() < new Date().getTime()) {
      this.logout();
      this.expire = true;
      console.log(new Date(String(localStorage.getItem('expire'))).getTime());
      console.log(new Date().getTime());
    }
    return this.expire;
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('admin');
    localStorage.removeItem('id');
    localStorage.removeItem('expire');

    this.auth = false;
    this.expire = false;
    this.admin = false;
  }

}
