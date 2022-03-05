import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {Router} from "@angular/router";
import {MeetingWithInvite} from "../../model/meeting-with-invite";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";
import {MeetingService} from "../../service/meeting.service";
import {ResponseMap} from "../../model/response-map";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  meetings: MeetingWithInvite[];

  constructor(private httpClient: HttpClient,
              private router: Router,
              private meetingService: MeetingService,
              public authService: AuthService) { }

  ngOnInit(): void {
    console.log("init home");

    this.meetingService.getEmployeeMeetings(Number(localStorage.getItem('id'))).subscribe(meetings => {
      for(let i = 0; i < meetings.length; i++) {
        meetings[i].start = meetings[i].start.slice(0, 16);
        meetings[i].end = meetings[i].end.slice(0, 16);
      }
      console.log(meetings);
      this.meetings = meetings;
    });
  }

  acceptMeeting(index: number): void {
    console.log(index);
    if(this.meetings[index].accept != 1) {
      this.meetingService.acceptMeeting(Number(localStorage.getItem('id')), this.meetings[index].id).subscribe(() => {
        this.meetings[index].accept = 1;
      });
    }
  }

  declineMeeting(index: number): void {
    console.log(index);
    if(this.meetings[index].accept != 2) {
      this.meetingService.declineMeeting(Number(localStorage.getItem('id')), this.meetings[index].id).subscribe(() => {
        this.meetings[index].accept = 2;
      });
    }
  }

}
