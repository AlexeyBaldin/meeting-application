export class ItemInRoom {
  id: number;
  roomId: number;
  name: string;
  itemCount: number;

  constructor(itemInRoom: ItemInRoom) {
    this.id = itemInRoom.id;
    this.roomId = itemInRoom.roomId;
    this.name = itemInRoom.name;
    this.itemCount = itemInRoom.itemCount;
  }

  toStringNameAndCount() : string {
    return this.name + '(' + this.itemCount + ')';
  }
}
