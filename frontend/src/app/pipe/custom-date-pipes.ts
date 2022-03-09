import {Pipe, PipeTransform} from "@angular/core";
import {Meeting} from "../model/meeting";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";

@Pipe({
  name: 'dayPipe',
  pure: false
})
export class DayPipe implements PipeTransform {
  transform(meetings: Meeting[], day: NgbDateStruct) : any {
    return meetings.filter(meeting => {
      let meetingDate = new Date(meeting.start);
      return meetingDate.getFullYear() == day.year && meetingDate.getMonth() + 1 == day.month && meetingDate.getDate() == day.day;
    });
  }
}
