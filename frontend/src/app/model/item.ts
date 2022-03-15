export class Item {
  id: number;
  officeId: number;
  count: number;
  name: string;

  constructor(item: Item) {
    this.id = item.id;
    this.officeId = item.officeId;
    this.count = item.count;
    this.name = item.name;
  }

  public toString = () => {
    return this.id + '/' + this.officeId + '/' + this.name + '(' + this.count + ')';
  }
}
