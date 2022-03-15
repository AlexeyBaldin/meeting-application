// noinspection DuplicatedCode

import { Component, OnInit } from '@angular/core';
import {HaveAlert} from "../../model/have-alert";
import {Office} from "../../model/office";
import {Room} from "../../model/room";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Meeting} from "../../model/meeting";
import {Employee} from "../../model/employee";
import {AuthService} from "../../service/auth.service";
import {MeetingService} from "../../service/meeting.service";
import {OfficeService} from "../../service/office.service";
import {EmployeeService} from "../../service/employee.service";
import {debounceTime, distinctUntilChanged, map, Observable} from "rxjs";
import {Router} from "@angular/router";


@Component({
  selector: 'app-admin-meeting',
  templateUrl: './admin-meeting.component.html',
  styleUrls: ['./admin-meeting.component.scss']
})
export class AdminMeetingComponent extends HaveAlert implements OnInit {


  meetings: Meeting[] = [];
  foundMeeting: Meeting = new Meeting({id: 0, name: '', end: '', start: '', roomId: 0, officeId: 0});
  chosenMeeting: Meeting = new Meeting({id: 0, name: '', end: '', start: '', roomId: 0, officeId: 0});

  meetingTheme: string = '';

  offices: Office[] = [];
  chosenOffice: number = 0;

  rooms: Room[] = [];
  chosenRoom: number = 0;

  meetingDate: NgbDateStruct;
  chosenDate: Date = new Date();

  roomMeetings: Meeting[] = [];

  startTime = {hour: 10, minute: 0};
  endTime = {hour: 11, minute: 0};

  employees: Employee[] = [];
  foundEmployee: Employee;
  employeesString: string[] = [];
  inviteEmployees: Employee[] = [];

  constructor(public authService: AuthService,
              private router: Router,
              private meetingService: MeetingService,
              private officeService: OfficeService,
              private employeeService: EmployeeService) {
    super();
  }

  ngOnInit(): void {
    if(this.authService.checkExpire()) {
      this.router.navigate(['login']);
    } else {
      this.meetingDate = {
        year: this.chosenDate.getFullYear(),
        month: this.chosenDate.getMonth() + 1,
        day: this.chosenDate.getDate()
      };

      this.officeService.getAllOffices().subscribe(offices => {
        this.offices = offices;
      }, (error) => {
        console.log(error);
      });

      this.employeeService.getAllEmployees().subscribe(employees => {
        this.employees = employees;

        console.log(this.employees[0] instanceof Employee);
      }, (error) => {
        console.log(error);
      });

      this.meetingService.getAllMeetings().subscribe(meetings => {
        this.meetings = meetings;
        this.meetings.forEach(meeting => {
          meeting.start = (String)(meeting.start).slice(0,16);
          meeting.end = (String)(meeting.end).slice(0,16);
        })
      });
    }
  }

  searchMeeting: (text$: Observable<string>) => Observable<Meeting[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.meetings.filter(v => v.toString().toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  chooseMeeting() : void {
    this.chosenMeeting = this.foundMeeting;


    this.meetingDate = { year: (Number)((String)(this.chosenMeeting.start).slice(0, 4)),
                         month: (Number)((String)(this.chosenMeeting.start).slice(5, 7)),
                         day: (Number)((String)(this.chosenMeeting.start).slice(8, 10))};
    this.startTime = {hour: (Number)((String)(this.chosenMeeting.start).slice(11, 13)),
                      minute: (Number)((String)(this.chosenMeeting.start).slice(14, 16))};
    this.endTime = {hour: (Number)((String)(this.chosenMeeting.end).slice(11, 13)),
                      minute: (Number)((String)(this.chosenMeeting.end).slice(14, 16))};

    this.meetingService.getAllEmployeesByMeetingId(this.chosenMeeting.id).subscribe(employees => {
      this.inviteEmployees = employees;
    })
  }

  chooseOffice(index: number): void {
    console.log('office' + this.offices[index].id);
    this.chosenOffice = this.offices[index].id;
    this.offices = [this.offices[index]];

    this.officeService.getRoomsByOfficeId(this.chosenOffice).subscribe(rooms => {
      this.rooms = rooms;

      this.rooms.forEach((room) => {
        this.officeService.getAllItemsInRoom(room.id).subscribe(itemsInRoom => {
          itemsInRoom.forEach((itemInRoom) => {
            room.itemsInRoom += '|' + itemInRoom.toStringNameAndCount() + '|';
          });
        });
      })

    }, (error) => {
      console.log(error);
    });
  }

  chooseRoom(index: number): void {
    console.log('room' + this.rooms[index].id);
    this.chosenRoom = this.rooms[index].id;
    this.rooms = [this.rooms[index]];


    this.meetingService.getRoomMeetings(this.chosenRoom).subscribe(meetings => {
      this.roomMeetings = meetings;
    }, (error) => {
      console.log(error);
    });
  }

  chooseDate(): void {
    this.chosenDate = new Date(this.meetingDate.year, this.meetingDate.month, this.meetingDate.day);
  }

  reset(): void {
    this.meetingTheme = '';
    this.officeService.getAllOffices().subscribe(offices => {
      this.offices = offices;
    }, (error) => {
      console.log(error);
    });
    this.chosenOffice = 0;
    this.rooms = [];
    this.chosenRoom = 0;
    this.chosenDate = new Date(Date.now());
    this.roomMeetings = [];
    this.meetingDate = {
      year: this.chosenDate.getFullYear(),
      month: this.chosenDate.getMonth() + 1,
      day: this.chosenDate.getDate()
    };

    this.meetingService.getAllMeetings().subscribe(meetings => {
      this.meetings = meetings;
      this.meetings.forEach(meeting => {
        meeting.start = (String)(meeting.start).slice(0,16);
        meeting.end = (String)(meeting.end).slice(0,16);
      })
    });
    this.foundMeeting = new Meeting({id: 0, name: '', end: '', start: '', roomId: 0, officeId: 0});
    this.chosenMeeting = this.foundMeeting;
  }

  searchEmployee: (text$: Observable<string>) => Observable<Employee[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => term.length < 2 ? []
        : this.employees.filter(v => v.name.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  inviteEmployee(): void {
    if (this.employees.includes(this.foundEmployee) && !this.inviteEmployees.includes(this.foundEmployee)) {
      this.meetingService.inviteEmployee(this.chosenMeeting.id, this.foundEmployee.id).subscribe(response => {
        this.inviteEmployees.push(this.foundEmployee);
        console.log(response);
      })
    }
  }

  deleteFromInvites(index: number) {
    this.meetingService.deleteInviteEmployee(this.chosenMeeting.id, this.inviteEmployees[index].id).subscribe(response => {
      this.inviteEmployees.splice(index, 1);
      console.log(response);
    })

  }


  checkData(): boolean {
    return this.chosenMeeting.id == 0;
  }

  formStringTimestamp(date: NgbDateStruct, time: { hour: number, minute: number }): string {

    let start = this.meetingDate.year + '-';

    if (date.month < 10) {
      start += '0' + date.month + '-';
    } else {
      start += date.month + '-';
    }

    if (date.day < 10) {
      start += '0' + date.day + 'T';
    } else {
      start += date.day + 'T';
    }

    if (time.hour < 10) {
      start += '0' + time.hour + ':'
    } else {
      start += time.hour + ':'
    }

    if (time.minute < 10) {
      start += '0' + time.minute
    } else {
      start += time.minute;
    }

    return start;
  }


  editMeeting() : void {
    let meeting = this.chosenMeeting;
    if(this.meetingTheme != '') {
      meeting.name = this.meetingTheme;
    }
    if(this.chosenOffice > 0) {
      meeting.officeId = this.chosenOffice;
    }
    if(this.chosenRoom > 0) {
      meeting.roomId = this.chosenRoom;
    }
    if((String)(this.chosenMeeting.start).slice(0, 16) != this.formStringTimestamp(this.meetingDate, this.startTime)) {
      meeting.start = this.formStringTimestamp(this.meetingDate, this.startTime);
    }
    if((String)(this.chosenMeeting.end).slice(0, 16) != this.formStringTimestamp(this.meetingDate, this.endTime)) {
      meeting.end = this.formStringTimestamp(this.meetingDate, this.endTime);
    }

    this.meetingService.editMeeting(meeting).subscribe((response) => {
      console.log(response);
      this.reset();
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

  deleteMeeting() : void {
    this.meetingService.deleteMeeting(this.chosenMeeting.id).subscribe(() => {
      this.alerts.push({
        type: "success",
        message: "Meeting was deleted"
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
