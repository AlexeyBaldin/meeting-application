import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";
import {MeetingWithInvite} from "../model/meeting-with-invite";
import {catchError, map, Observable} from "rxjs";
import {ResponseMap} from "../model/response-map";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MeetingService {


  constructor(private httpClient: HttpClient, public authService: AuthService, private router: Router) { }

  getEmployeeMeetings(employeeId: number): Observable<MeetingWithInvite[]> {
    return this.httpClient.get<MeetingWithInvite[]>(environment.mainUrl + 'meeting/all/' + employeeId)
      .pipe(map(meetings => {
        return meetings;
      }))
  }

  acceptMeeting(employeeId: number, meetingId: number) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.mainUrl + 'meeting/invite/' + employeeId + '/' + meetingId, true)
      .pipe(map(response => {
        return response;
      }));
  }

  declineMeeting(employeeId: number, meetingId: number) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.mainUrl + 'meeting/invite/' + employeeId + '/' + meetingId, false)
      .pipe(map(response => {
        return response;
      }));
  }


}

