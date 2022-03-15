import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Observable, map} from "rxjs";
import {Office} from "../model/office";
import {environment} from "../../environments/environment";
import {Room} from "../model/room";
import {ResponseMap} from "../model/response-map";
import {ItemInRoom} from "../model/item-in-room";
import {Item} from "../model/item";


@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  constructor(private httpClient: HttpClient,
              public authService: AuthService) {
  }

  getOfficeById(officeId: number) : Observable<Office> {
    return this.httpClient.get<Office>(environment.forAllUrl + 'office/' + officeId)
      .pipe(map(office => {
        return new Office(office);
      }))
  }

  getAllOffices() : Observable<Office[]> {
    return this.httpClient.get<Office[]>(environment.forAllUrl + 'office/all')
      .pipe(map(offices => {
        return offices.map((office) => {
          return new Office(office);
        });
      }));
  }

  getRoomsByOfficeId(officeId: number) : Observable<Room[]> {
    return this.httpClient.get<Room[]>(environment.forAllUrl + 'office/' + officeId + '/room')
      .pipe(map(rooms => {
        return rooms.map((room) => {
          return new Room(room);
        });
      }));
  }

  getRoomById(roomId: number) : Observable<Room> {
    return this.httpClient.get<Room>(environment.forAllUrl + 'office/room/' + roomId)
      .pipe(map(room => {
        return new Room(room);
      }))
  }

  getAllItemsInRoom(roomId: number) : Observable<ItemInRoom[]> {
    return this.httpClient.get<ItemInRoom[]>(environment.forAllUrl + 'office/room/' + roomId + '/item/all')
      .pipe(map(items => {
        return items.map(item => {
          return new ItemInRoom(item);
        })
      }))
  }

  createNewOffice(office: Office) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'office/save/office', office)
      .pipe(map(response => {
        return response;
      }));
  }

  editOffice(office: Office) : Observable<ResponseMap> {
    return this.httpClient.put<ResponseMap>(environment.adminUrl + 'office/' + office.id, office)
      .pipe(map(response => {
        return response;
      }));
  }

  deleteOffice(officeId: number) : Observable<ResponseMap> {
    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'office/' + officeId)
      .pipe(map(response => {
        return response;
      }));
  }

  getAllItemsInOffice(officeId: number) : Observable<Item[]> {
    return this.httpClient.get<Item[]>(environment.adminUrl + 'office/' + officeId + '/item/all')
      .pipe(map(items => {
        return items.map(item => {
          return new Item(item);
        })
      }));
  }

  addNewItem(item: Item) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'office/save/' + item.officeId + '/item', item)
      .pipe(map(response => {
        return response;
      }));
  }

  editItem(item: Item) : Observable<ResponseMap> {
    return this.httpClient.put<ResponseMap>(environment.adminUrl + 'office/item/' + item.id, item)
      .pipe(map(response => {
        return response;
      }));
  }

  deleteItem(itemId: number) : Observable<ResponseMap> {
    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'office/item/' + itemId)
      .pipe(map(response => {
        return response;
      }));
  }

  addNewRoom(room: Room) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'office/save/' + room.officeId + '/room', room)
      .pipe(map(response => {
        return response;
      }));
  }

  editRoom(room: Room) : Observable<ResponseMap> {
    return this.httpClient.put<ResponseMap>(environment.adminUrl + 'office/room/' + room.id, room)
      .pipe(map(response => {
        return response;
      }));
  }

  deleteRoom(roomId: number) : Observable<ResponseMap> {
    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'office/room/' + roomId)
      .pipe(map(response => {
        return response;
      }));
  }

  addItemToRoom(officeId: number, roomId: number, itemName: String) : Observable<ResponseMap> {
    return this.httpClient.post<ResponseMap>(environment.adminUrl + 'office/' + officeId + '/room/' + roomId + '/item/' + itemName + '/1', null)
      .pipe(map(response => {
        return response;
      }));
  }

  removeItemFromRoom(officeId: number, roomId: number, itemName: String) : Observable<ResponseMap> {

    return this.httpClient.delete<ResponseMap>(environment.adminUrl + 'office/' + officeId + '/room/' + roomId + '/item/' + itemName)
      .pipe(map(response => {
        return response;
      }));
  }
}
