import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {Router} from "@angular/router";
import {MeetingWithInvite} from "../../model/meeting-with-invite";
import {HttpClient} from "@angular/common/http";
import {MeetingService} from "../../service/meeting.service";
import  { DatePipe } from "@angular/common";
import {HaveAlert} from "../../model/have-alert";
import {OfficeService} from "../../service/office.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [DatePipe]
})
export class HomeComponent extends HaveAlert implements OnInit {

  meetings: MeetingWithInvite[];

  showInfo: boolean = false;
  infoMeeting = { meetingId: 0, officeId: 0, officeAddress: '', roomId: 0, cabinet: 0, roomItems: '',
    meetingName: '', startTime: '', endTime: '', status: '', invited: ''};

  constructor(private httpClient: HttpClient,
              private router: Router,
              private meetingService: MeetingService,
              private officeService: OfficeService,
              public authService: AuthService,
              public datePipe: DatePipe) {
    super();
  }

  ngOnInit(): void {
    console.log("init home");

    if(this.authService.checkExpire()) {
      this.router.navigate(['login']);
    } else {
      this.meetingService.getEmployeeMeetingsWithInvite(Number(localStorage.getItem('id'))).subscribe(meetings => {
        for(let i = 0; i < meetings.length; i++) {
          meetings[i].start = new Date(meetings[i].start);
          meetings[i].end = new Date(meetings[i].end);
        }
        this.meetings = meetings;
      });
    }
  }

  acceptMeeting(index: number): void {
    console.log(index);
    if(this.meetings[index].accept != 1) {
      this.meetingService.acceptMeeting(Number(localStorage.getItem('id')), this.meetings[index].id).subscribe(() => {
        this.meetings[index].accept = 1;
      }, (response) => {
        this.alerts.push({
          type: 'warning',
          message: response['error']['employee error']
        })
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

  meetingInfo(index: number) : void {
    this.infoMeeting.meetingId = this.meetings[index].id;
    this.infoMeeting.officeId = this.meetings[index].officeId;
    this.infoMeeting.roomId = this.meetings[index].roomId;
    this.infoMeeting.startTime = String(this.meetings[index].start).slice(0, 16);
    this.infoMeeting.endTime = String(this.meetings[index].end).slice(0, 16);
    this.infoMeeting.meetingName = this.meetings[index].name;

    if(this.meetings[index].accept == 0) {
      this.infoMeeting.status = 'Not chosen';
    } else if(this.meetings[index].accept == 1) {
      this.infoMeeting.status = 'Accepted';
    } else {
      this.infoMeeting.status = 'Declined';
    }

    this.officeService.getOfficeById(this.infoMeeting.officeId).subscribe(office => {
      this.infoMeeting.officeAddress = office.city + ', ' + office.address;
    })

    this.infoMeeting.invited = '';

    this.meetingService.getAllEmployeesByMeetingId(this.infoMeeting.meetingId).subscribe(employees => {
      employees.forEach(employee => {
        this.infoMeeting.invited += "|" + employee.name + '|';
      })
    })

    this.officeService.getRoomById(this.infoMeeting.roomId).subscribe(room => {
      this.infoMeeting.cabinet = room.cabinet;
    })

    this.infoMeeting.roomItems = '';
    this.officeService.getAllItemsInRoom(this.infoMeeting.roomId).subscribe(items => {
      items.forEach(item => {
        this.infoMeeting.roomItems += '|' + item.toStringNameAndCount() + '|';
      });
    });

    this.showInfo = true;
  }

  closeInfo() : void {
    this.showInfo = false;
  }


}
