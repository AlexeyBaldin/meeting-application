import {Time} from "@angular/common";

export class Office {
  id: number;
  city: string;
  address: string;
  phone: string;
  openTime: string;
  closeTime: string;


  constructor(office: Office) {
    this.id = office.id;
    this.city = office.city;
    this.address = office.address;
    this.phone = office.phone;
    this.openTime = office.openTime;
    this.closeTime = office.closeTime;
  }

  public toString = () => {
    return this.id + '/' + this.city + '/' + this.address;
  }
}
