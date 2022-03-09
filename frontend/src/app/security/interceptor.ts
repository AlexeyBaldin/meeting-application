import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {Observable} from "rxjs";

@Injectable()
export class Interceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(this.authService.auth) {
      const token = localStorage.getItem('token');
      if(token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer_${token}`
          }
        });
      }
    }
    return next.handle(request);
  }
}
