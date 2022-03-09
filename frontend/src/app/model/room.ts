
export class Room {
  id: number;
  officeId: number;
  cabinet: number;
  capacity: number;
  itemsInRoom: string = '';


  constructor(room: Room) {
    this.id = room.id;
    this.officeId = room.officeId;
    this.cabinet = room.cabinet;
    this.capacity = room.capacity;
  }

  public toString = () => {
    return this.id + '/' + this.officeId + '/cabinet=' + this.cabinet;
  }
}
