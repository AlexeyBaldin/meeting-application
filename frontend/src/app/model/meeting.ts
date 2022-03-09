export class Meeting {
  id: number;
  name: string;
  roomId: number;
  officeId: number;
  start: Date | string;
  end: Date | string;


  constructor(meeting: Meeting) {
    this.id = meeting.id;
    this.name = meeting.name;
    this.roomId = meeting.roomId;
    this.officeId = meeting.officeId;
    this.start = meeting.start;
    this.end = meeting.end;
  }

  public toString = () => {
    return this.id + '/' + this.name;
  }
}
