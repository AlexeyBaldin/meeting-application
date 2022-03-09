import { Component, OnInit } from '@angular/core';
import {Office} from "../../model/office";
import {debounceTime, distinctUntilChanged, map, Observable} from "rxjs";
import {Employee} from "../../model/employee";
import {AuthService} from "../../service/auth.service";
import {OfficeService} from "../../service/office.service";
import {HaveAlert} from "../../model/have-alert";
import {Router} from "@angular/router";
import {Item} from "../../model/item";
import {Room} from "../../model/room";


@Component({
  selector: 'app-admin-office',
  templateUrl: './admin-office.component.html',
  styleUrls: ['./admin-office.component.scss']
})
export class AdminOfficeComponent extends HaveAlert implements OnInit {

  offices: Office[] = [];
  foundOffice: Office;

  officeId: number = 0;
  city: string = '';
  address: string = '';
  phone: string = '';
  openTime = {hour: 6, minute: 0};
  closeTime = {hour: 22, minute: 0};


  itemsInOffice: Item[] = [];
  foundItemInOffice: Item;
  itemId: number = 0;
  itemCount: number = 0;
  itemName: string = '';

  roomsInOffice: Room[] = [];
  foundRoomInOffice: Room;
  roomId: number = 0;
  roomCabinet: number = 0;
  roomCapacity: number = 0;
  itemsInRoom: string;


  constructor(private officeService: OfficeService,
              private router: Router,
              public authService: AuthService) {
    super();
  }

  ngOnInit(): void {
    if(this.authService.checkExpire()) {
      this.router.navigate(['login']);
    } else {
      this.officeService.getAllOffices().subscribe(offices => {
        this.offices = offices;
      })
    }


  }

  searchOffice: (text$: Observable<string>) => Observable<Office[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.offices.filter(v => v.toString().toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  searchItemInOffice: (text$: Observable<string>) => Observable<Item[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.itemsInOffice.filter(v => v.toString().toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  searchRoomInOffice: (text$: Observable<string>) => Observable<Room[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.roomsInOffice.filter(v => v.toString().toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  chooseOffice() {
    this.officeId = this.foundOffice.id;
    this.city = this.foundOffice.city;
    this.address = this.foundOffice.address;
    this.phone = this.foundOffice.phone;
    this.openTime = {hour: parseInt(this.foundOffice.openTime.slice(0, 2)), minute: parseInt(this.foundOffice.openTime.slice(3, 5))};
    this.closeTime = {hour: parseInt(this.foundOffice.closeTime.slice(0, 2)), minute: parseInt(this.foundOffice.closeTime.slice(3, 5))};

    this.officeService.getAllItemsInOffice(this.officeId).subscribe(items => {
      this.itemsInOffice = items;
    });

    this.officeService.getRoomsByOfficeId(this.officeId).subscribe(rooms => {
      this.roomsInOffice = rooms;
    });
  }

  chooseItemInOffice() {
    this.itemId = this.foundItemInOffice.id;
    this.itemCount = this.foundItemInOffice.count;
    this.itemName = this.foundItemInOffice.name;
  }

  chooseRoomInOffice() {
    this.roomId = this.foundRoomInOffice.id;
    this.roomCabinet = this.foundRoomInOffice.cabinet;
    this.roomCapacity = this.foundRoomInOffice.capacity;

    this.itemsInRoom = '';

    this.officeService.getAllItemsInRoom(this.roomId).subscribe(itemsInRoom => {
      itemsInRoom.forEach((itemInRoom) => {
        this.itemsInRoom += '|' + itemInRoom.toStringNameAndCount() + '|';
      });
    });
  }

  checkOfficeData() : boolean {
    return this.city == '' || this.address == '' || this.phone == '';
  }

  timeToString(time: {hour: number, minute: number}) {
    let string = '';
    if (time.hour < 10) {
      string += '0' + time.hour + ':'
    } else {
      string += time.hour + ':'
    }

    if (time.minute < 10) {
      string += '0' + time.minute
    } else {
      string += time.minute;
    }

    return string;
  }

  createNewOffice() : void {
    this.officeService.createNewOffice({id: 0, city: this.city, address: this.address,
       phone: this.phone, openTime: this.timeToString(this.openTime), closeTime: this.timeToString(this.closeTime)})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Office was registered"
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

  editOffice() : void {
    this.officeService.editOffice({id: this.officeId, city: this.city, address: this.address,
      phone: this.phone, openTime: this.timeToString(this.openTime), closeTime: this.timeToString(this.closeTime)})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Success edit office data"
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

  deleteOffice() : void {
    this.officeService.deleteOffice(this.officeId).subscribe(() => {
      this.alerts.push({
        type: "success",
        message: "Office was deleted"
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

  reset() : void {
    this.officeId = 0;
    this.city = '';
    this.address = '';
    this.phone = '';
    this.openTime = {hour: 6, minute: 0};
    this.closeTime = {hour: 22, minute: 0};

    this.officeService.getAllOffices().subscribe(offices => {
      this.offices = offices;
    })
  }

  checkItemData() : boolean {
    return this.itemName == '' || this.itemCount < 0;
  }

  addNewItem() : void {
    this.officeService.addNewItem({id: 0, officeId: this.officeId, name: this.itemName, count: this.itemCount})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Item was added"
        });
        this.resetItem();
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

  editItem() : void {
    this.officeService.editItem({id: this.itemId, officeId: this.officeId, name: this.itemName, count: this.itemCount})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Success item edit"
        });
        this.resetItem();
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

  deleteItem() : void {
    this.officeService.deleteItem(this.itemId).subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Item was delete"
        });
        this.resetItem();
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

  resetItem() : void {
    this.itemCount = 0;
    this.itemName = '';
    this.itemId = 0;

    this.officeService.getAllItemsInOffice(this.officeId).subscribe(items => {
      this.itemsInOffice = items;
    });
  }

  checkRoomData() : boolean {
    return this.roomCabinet < 1 || this.roomCapacity < 1;
  }

  resetRoom() : void {
    this.roomId = 0;
    this.roomCapacity = 0;
    this.roomCabinet = 0;
    this.itemsInRoom = '';

    this.officeService.getRoomsByOfficeId(this.officeId).subscribe(rooms => {
      this.roomsInOffice = rooms;
    });
  }

  addNewRoom() : void {
    this.officeService.addNewRoom({id: 0, officeId: this.officeId, capacity: this.roomCapacity, cabinet: this.roomCabinet, itemsInRoom: ''})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Room was added"
        });
        this.resetRoom();
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

  editRoom() : void {
    this.officeService.editRoom({id: this.roomId, officeId: this.officeId, capacity: this.roomCapacity, cabinet: this.roomCabinet, itemsInRoom: ''})
      .subscribe(() => {
        this.alerts.push({
          type: "success",
          message: "Success edit room"
        });
        this.resetRoom();
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

  deleteRoom() : void {
    this.officeService.deleteRoom(this.roomId).subscribe(() => {
      this.alerts.push({
        type: "success",
        message: "Room was delete"
      });
      this.resetRoom();
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

  addItemToRoom() : void {
    this.officeService.addItemToRoom(this.officeId, this.roomId, this.itemName).subscribe(() => {
      this.alerts.push({
        type: "success",
        message: "Item was added to room"
      });


      this.itemsInRoom = '';
      this.officeService.getAllItemsInRoom(this.roomId).subscribe(itemsInRoom => {
        itemsInRoom.forEach((itemInRoom) => {
          this.itemsInRoom += '|' + itemInRoom.toStringNameAndCount() + '|';
        });
      });
      this.itemCount--;
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

  removeItemFromRoom() : void {
    this.officeService.removeItemFromRoom(this.officeId, this.roomId, this.itemName).subscribe((response) => {
      this.alerts.push({
        type: "success",
        message: "Item was removed from room"
      });

      this.itemsInRoom = '';
      this.officeService.getAllItemsInRoom(this.roomId).subscribe(itemsInRoom => {
        itemsInRoom.forEach((itemInRoom) => {
          this.itemsInRoom += '|' + itemInRoom.toStringNameAndCount() + '|';
        });
      });
      this.itemCount += Number(response['count']);

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
