import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";
import {MeetingWithInvite} from "../model/meeting-with-invite";
import {catchError, map, Observable} from "rxjs";
import {ResponseMap} from "../model/response-map";
import {environment} from "../../environments/environment";
import {Meeting} from "../model/meeting";
import {Employee} from "../model/employee";

@Injectable({
  providedIn: 'root'
})
export class MeetingService {


  constructor(private httpClient: HttpClient, public authService: AuthService) { }

  getEmployeeMeetingsWithInvite(employeeId: number): Observable<MeetingWithInvite[]> {
    return this.httpClient.get<MeetingWithInvite[]>(environment.forAllUrl + 'meeting/all/' + employeeId)
      .pipe(map(meetings => {
        return meetings;
      }))
  }

  getAllMeetings(): Observable<Meeting[]> {
    return this.httpClient.get<MeetingWithInvite[]>(environment.forAllUrl + 'meeting/all')
      .pipe(map(meetings => {
        return meetings.map(meeting => {
          return new Meeting(meeting);
        });
      }))
  }

  acceptMeeting(employeeId: number, meetingId: number) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.forAllUrl + 'meeting/invite/' + employeeId + '/' + meetingId, true)
      .pipe(map(response => {
        return response;
      }));
  }

  declineMeeting(employeeId: number, meetingId: number) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.forAllUrl + 'meeting/invite/' + employeeId + '/' + meetingId, false)
      .pipe(map(response => {
        return response;
      }));
  }

  getRoomMeetings(roomId: number) : Observable<Meeting[]> {
    return this.httpClient.get<Meeting[]>(environment.forAllUrl + 'meeting/room/' + roomId)
      .pipe(map(meetings => {
        return meetings;
      }))
  }

  createMeeting(meeting: Meeting, employeesId: number[]) : Observable<ResponseMap> {
    return this.httpClient.post<any>(environment.forAllUrl + 'meeting/save', {meeting, employeesId})
      .pipe(map(response => {
        return response;
      }));
  }

  editMeeting(meeting: Meeting) : Observable<ResponseMap> {
    return this.httpClient.put<any>(environment.adminUrl + 'meeting/' + meeting.id, meeting)
      .pipe(map(response => {
        return response;
      }));
  }

  deleteMeeting(meetingId: number) : Observable<ResponseMap> {
    return this.httpClient.delete<any>(environment.adminUrl + 'meeting/' + meetingId)
      .pipe(map(response => {
      return response;
    }));
  }

  getAllEmployeesByMeetingId(meetingId: number) : Observable<Employee[]> {
    return this.httpClient.get<Employee[]>(environment.forAllUrl + 'meeting/' + meetingId + '/employees')
      .pipe(map(employees => {
        return employees.map(employee => {
          return new Employee(employee);
        })
      }))
  }

  inviteEmployee(meetingId: number, employeeId: number) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'meeting/invite/' + employeeId + '/' + meetingId, null)
      .pipe(map(response => {
        return response;
      }));
  }

  deleteInviteEmployee(meetingId: number, employeeId: number) : Observable<ResponseMap> {
    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'meeting/invite/' + employeeId + '/' + meetingId)
      .pipe(map(response => {
        return response;
      }));
  }


}

