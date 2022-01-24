package com.controller;

import com.model.office.Item;
import com.model.office.Office;
import com.model.office.Room;
import com.service.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/office")
public class OfficeRestController {

    @Autowired
    private OfficeService officeService;

    @GetMapping("/all")
    public List<Office> findAll() {
        return officeService.findAllOffices();
    }

    @GetMapping("/{office_id}")
    public ResponseEntity<Office> findById(@PathVariable(value = "office_id") Integer officeId) {
        if(officeService.isOfficeExists(officeId)) {
            Office office = officeService.findOfficeById(officeId);
            return ResponseEntity.ok().body(office);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{office_id}/room")
    public ResponseEntity<List<Room>> findAllRoomsInOffice(@PathVariable(value = "office_id") Integer officeId) {
        if(officeService.isOfficeExists(officeId)) {
            List<Room> rooms = officeService.findAllRoomsInOffice(officeId);
            return ResponseEntity.ok().body(rooms);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{office_id}/room/{room_id}")
    public ResponseEntity<Room> findByOfficeIdAndRoomId(@PathVariable(value = "office_id") Integer officeId,
                                                        @PathVariable(value = "room_id") Integer roomId) {
        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId)) {
            Room room = officeService.findRoomByOfficeIdAndRoomId(officeId, roomId);
            return ResponseEntity.ok().body(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{office_id}/room/{room_id}/{item_name}")
    public ResponseEntity<Integer> countItemInRoom(@PathVariable(value = "office_id") Integer officeId,
                                                   @PathVariable(value = "room_id") Integer roomId,
                                                   @PathVariable(name = "item_name") String itemName) {
        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId) &&
           officeService.isItemExistsByOfficeIdAndName(officeId, itemName)) {

            Integer count = officeService.countItemInRoom(officeId, roomId, itemName);
            return ResponseEntity.ok().body(count);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save/office")
    public ResponseEntity<Map<String, Object>> saveOffice(@RequestBody Office newOffice) {

        Map<String, Object> responseMap = officeService.checkOfficeAndGetErrorsMap(newOffice);

        if(responseMap.isEmpty()) {
            officeService.saveOffice(newOffice);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            responseMap.put("success", false);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PostMapping("/save/{office_id}/room")
    public ResponseEntity<Map<String, Object>> saveRoom(@PathVariable(value = "office_id") Integer officeId,
                                                        @RequestBody Room newRoom) {

        if(officeService.isOfficeExists(officeId)) {
            Map<String, Object> responseMap = officeService.checkRoomAndGetErrorsMap(officeId, newRoom);

            if(responseMap.isEmpty()) {
                officeService.saveRoom(officeId, newRoom);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @PostMapping("{office_id}/room/{room_id}/item/{item_name}/{item_count}")
    public ResponseEntity<Map<String, Object>> addItemInRoom(@PathVariable(value = "office_id") Integer officeId,
                                                             @PathVariable(value = "room_id") Integer roomId,
                                                             @PathVariable(value = "item_name") String itemName,
                                                             @PathVariable(value = "item_count") Integer itemCount) {

        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId) &&
           officeService.isItemExistsByOfficeIdAndName(officeId, itemName)) {

            Map<String, Object> responseMap = officeService.checkItemCountAndGetErrorsMap(officeId, itemName, itemCount);

            if(responseMap.isEmpty()) {
                officeService.addItemInRoom(officeId, roomId, itemName, itemCount);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{office_id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable(value = "office_id") Integer officeId,
                                                      @RequestBody Office newOffice) {

        if(officeService.isOfficeExists(officeId)) {
            Map<String, Object> responseMap = officeService.checkOfficeAndGetErrorsMap(newOffice);

            if(responseMap.isEmpty()) {
                newOffice.setId(officeId);
                officeService.saveOffice(newOffice);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{office_id}/room/{room_id}")
    public ResponseEntity<Map<String, Object>> updateRoom(@PathVariable(value = "office_id") Integer officeId,
                                                          @PathVariable(value = "room_id") Integer roomId, @RequestBody Room newRoom) {

        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId)) {
            Map<String, Object> responseMap = officeService.checkRoomAndGetErrorsMap(officeId, newRoom);

            if(responseMap.isEmpty()) {
                officeService.updateRoom(officeId, roomId, newRoom);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{office_id}")
    public ResponseEntity<Map<String, Object>> deleteOffice(@PathVariable(value = "office_id") Integer officeId) {

        Map<String, Object> responseMap = new HashMap<>();

        if(officeService.isOfficeExists(officeId)) {
            officeService.deleteOffice(officeId);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{office_id}/room/{room_id}")
    public ResponseEntity<Map<String, Object>> deleteRoom(@PathVariable(value = "office_id") Integer officeId,
                                                          @PathVariable(value = "room_id") Integer roomId) {

        Map<String, Object> responseMap = new HashMap<>();

        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId)) {
            officeService.deleteRoom(officeId, roomId);
            responseMap.put("success", true);
            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{office_id}/room/{room_id}/item/{item_name}")
    public ResponseEntity<Map<String, Object>> fullRemoveItemFromRoom(@PathVariable(value = "office_id") Integer officeId,
                                                                      @PathVariable(value = "room_id") Integer roomId,
                                                                      @PathVariable(value = "item_name") String itemName) {

        if(officeService.isOfficeExists(officeId) && officeService.isRoomExists(roomId) &&
           officeService.isItemExistsByOfficeIdAndName(officeId, itemName)) {

            Map<String, Object> responseMap = officeService.checkItemInTheRoomAndGetErrorsMap(officeId, roomId, itemName);

            if(responseMap.isEmpty()) {
                officeService.fullRemoveItemFromRoom(officeId, roomId, itemName);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/items/all")
    public List<Item> findAllItems() {
        return officeService.findAllItems();
    }

    @GetMapping("/item/{item_id}")
    public ResponseEntity<Item> findItemById(@PathVariable(value = "item_id") Integer itemId) {
        if(officeService.isItemExists(itemId)) {
            Item item = officeService.findItemById(itemId);
            return ResponseEntity.ok().body(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save/{office_id}/item")
    public ResponseEntity<Map<String, Object>> saveItem(@PathVariable(value = "office_id") Integer officeId, @RequestBody Item newItem) {

        if(officeService.isOfficeExists(officeId)) {
            Map<String, Object> responseMap = officeService.checkItemAndGetErrorsMap(officeId, newItem);

            if(responseMap.isEmpty()) {
                officeService.saveItem(officeId, newItem);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @PutMapping("/item/{item_id}")
    public ResponseEntity<Map<String, Object>> updateItemCount(@PathVariable(value = "item_id") Integer itemId, @RequestBody Integer newCount) {

        if(officeService.isItemExists(itemId)) {
            Map<String, Object> responseMap = officeService.checkItemCountAndGetErrorsMap(newCount);

            if(responseMap.isEmpty()) {
                officeService.updateItemCount(itemId, newCount);
                responseMap.put("success", true);
                return ResponseEntity.ok().body(responseMap);
            } else {
                responseMap.put("success", false);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/item/{item_id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable(value = "item_id") Integer itemId) {
        if(officeService.isItemExists(itemId)) {
            officeService.deleteItem(itemId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);

            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
