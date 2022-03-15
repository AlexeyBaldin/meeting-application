// noinspection DuplicatedCode

import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {EmployeeService} from "../../service/employee.service";
import {MeetingService} from "../../service/meeting.service";
import {Office} from "../../model/office";
import {OfficeService} from "../../service/office.service";
import {Room} from "../../model/room";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Meeting} from "../../model/meeting";
import {Employee} from "../../model/employee";
import {debounceTime, distinctUntilChanged, map, Observable} from "rxjs";
import {HaveAlert} from "../../model/have-alert";
import {Router} from "@angular/router";


@Component({
  selector: 'app-new-meeting',
  templateUrl: './new-meeting.component.html',
  styleUrls: ['./new-meeting.component.scss']
})
export class NewMeetingComponent extends HaveAlert implements OnInit {

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
    if (this.authService.checkExpire()) {
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
    }


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
          })
        })
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
      this.inviteEmployees.push(this.foundEmployee);
    }
  }

  deleteFromInvites(index: number) {
    this.inviteEmployees.splice(index, 1);
  }


  checkData(): boolean {
    return this.meetingTheme == '' || this.chosenOffice == 0 || this.chosenRoom == 0;
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

  createMeeting(): void {
    let meeting = new Meeting({
      id: 0, name: this.meetingTheme,
      end: this.formStringTimestamp(this.meetingDate, this.endTime),
      start: this.formStringTimestamp(this.meetingDate, this.startTime),
      roomId: this.chosenRoom, officeId: this.chosenOffice
    });

    let employeesId: number[] = [];
    this.inviteEmployees.forEach((employee) => {
      employeesId.push(employee.id);
    })


    this.meetingService.createMeeting(meeting, employeesId).subscribe((response) => {
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
}
