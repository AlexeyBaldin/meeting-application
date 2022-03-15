package com.service.office;

import com.dto.query.ItemInRoom;
import com.model.meeting.Meeting;
import com.model.office.Item;
import com.model.office.Office;
import com.model.office.Room;
import com.repository.meeting.MeetingRepository;
import com.repository.office.ItemRepository;
import com.repository.office.OfficeRepository;
import com.repository.office.RoomRepository;
import com.util.FieldChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OfficeService {

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MeetingRepository meetingRepository;


    public List<Office> findAllOffices() {
        return officeRepository.findAll();
    }

    public Office findOfficeById(Integer officeId) {
        return officeRepository.findById(officeId).orElse(null);
    }

    public boolean isOfficeExists(Integer officeId) {
        return officeRepository.existsById(officeId);
    }

    private String checkCityAndGetError(String city) {
        return FieldChecker.checkNullStringAndGetError(city);
    }

    private String checkAddressAndGetError(String address) {
        return FieldChecker.checkNullStringAndGetError(address);
    }

    private String checkPhoneAndGetError(String phone) {
        return FieldChecker.checkNullStringAndGetError(phone);
    }

    private String checkOpenTimeAndGetError(LocalTime open) {
        return FieldChecker.checkNullLocalTimeAndGetError(open);
    }

    private String checkCloseTimeAndGetError(LocalTime close) {
        return FieldChecker.checkNullLocalTimeAndGetError(close);
    }

    private String checkRoomCabinetAndGetError(Integer officeId, Integer cabinet) {
        String error = FieldChecker.checkPositiveIntegerAndGetError(cabinet);

        if(error == null && roomRepository.existsByOfficeIdAndCabinet(officeId, cabinet)) {
            error = "room with cabinet = " + cabinet + " in office with office id = " + officeId + " already exist";
        }
        return error;
    }

    private String checkRoomCapacityAndGetError(Integer capacity) {
        return FieldChecker.checkPositiveIntegerAndGetError(capacity);
    }

    public Map<String, Object> checkOfficeAndGetErrorsMap(Office newOffice, boolean update) {
        Map<String, Object> errors = new HashMap<>();

        String check = checkCityAndGetError(newOffice.getCity());
        if(check != null) {
            errors.put("field(city) error", check);
        }

        check = checkAddressAndGetError(newOffice.getAddress());
        if(check != null) {
            errors.put("field(address) error", check);
        }

        check = checkPhoneAndGetError(newOffice.getPhone());
        if(check != null) {
            errors.put("field(phone) error", check);
        }

        check = checkOpenTimeAndGetError(newOffice.getOpenTime());
        if(check != null) {
            errors.put("field(open) error", check);
        }

        check = checkCloseTimeAndGetError(newOffice.getCloseTime());
        if(check != null) {
            errors.put("field(close) error", check);
        }

        if(newOffice.getOpenTime().isAfter(newOffice.getCloseTime())) {
            errors.put("time error", "open time (" + newOffice.getOpenTime() + ") > close time (" + newOffice.getCloseTime() + ")");
        }

        if(update) {
            check = checkOfficeMeetingsAndGetError(newOffice);
            if(check != null) {
                errors.put("meetings error", check);
            }
        }

        return errors;
    }

    public String checkOfficeMeetingsAndGetError(Office newOffice) {
        List<Meeting> meetings = meetingRepository.findAllByOfficeId(newOffice.getId());

        StringBuilder error = new StringBuilder();
        int intersects = 0;
        if(!meetings.isEmpty()) {
            for (Meeting meeting : meetings) {

                Timestamp start = new Timestamp(meeting.getStart().getTime());
                start.setHours(start.getHours() + start.getTimezoneOffset()/60);
                Timestamp end = new Timestamp(meeting.getEnd().getTime());
                end.setHours(end.getHours() + end.getTimezoneOffset()/60);

                LocalTime startTime = start.toLocalDateTime().toLocalTime();
                LocalTime endTime = end.toLocalDateTime().toLocalTime();

                if(!(startTime.isAfter(newOffice.getOpenTime()) && startTime.isBefore(newOffice.getCloseTime())) ||
                        !(endTime.isAfter(newOffice.getOpenTime()) && endTime.isBefore(newOffice.getCloseTime()))) {
                    error.append(meeting.getName()).append("|");
                    intersects++;
                }
            }
        }

        if(intersects != 0) {
            return error.toString().trim();
        } else {
            return null;
        }
    }

    public Map<String, Object> checkRoomAndGetErrorsMap(Integer officeId, Room newRoom) {
        Map<String, Object> errors = new HashMap<>();

        if(!isOfficeExists(officeId)) {
            errors.put("field(officeId) error", "office does`t exist for id = " + officeId);
        }

        String check = checkRoomCabinetAndGetError(officeId ,newRoom.getCabinet());
        if(check != null) {
            errors.put("field(cabinet) error", check);
        }

        check = checkRoomCapacityAndGetError(newRoom.getCapacity());
        if(check != null) {
            errors.put("field(capacity) error", check);
        }

        return errors;
    }

    public Map<String, Object> checkItemCountAndGetErrorsMap(Integer officeId, String itemName, Integer itemCount) {
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        Map<String, Object> errors = new HashMap<>();

        if(item.getCount() < itemCount) {
            errors.put("item count error", "there are no " + itemCount + " " + itemName + " in the office");
        }

        return errors;
    }
    public Map<String, Object> checkItemInTheRoomAndGetErrorsMap(Integer officeId, Integer roomId, String itemName) {
        Item item = findItemByOfficeIdAndName(officeId, itemName);
        Room room = findRoomById(roomId);
        int count = room.countItemInRoom(item.getId());

        Map<String, Object> errors = new HashMap<>();

        if(count == 0) {
            errors.put("item error", "there are no " + itemName + " in the room");
        }

        return errors;
    }

    public void saveOffice(Office newOffice) {
        officeRepository.save(newOffice);
    }

    public void deleteOffice(Integer officeId) {
        Office office = findOfficeById(officeId);
        officeRepository.delete(office);
    }

    public Room findRoomById(Integer roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    public Room findRoomByRoomId(Integer roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    public boolean isRoomExists(Integer roomId) {
        return roomRepository.existsById(roomId);
    }

    public void saveRoom(Integer officeId, Room newRoom) {
        newRoom.setOfficeId(officeId);
        roomRepository.save(newRoom);
    }

    public void updateRoom(Integer officeId ,Integer roomId, Room newRoom) {
        Room room = findRoomById(roomId);

        newRoom.setId(roomId);
        newRoom.setOfficeId(officeId);
        newRoom.setRoomInventory(room.getRoomInventory());

        roomRepository.save(newRoom);
    }

    public void deleteRoom(Integer roomId) {
        Room room = findRoomById(roomId);

        List<ItemInRoom> itemsInRoom = findAllItemsInRoom(roomId);
        itemsInRoom.forEach(itemInRoom -> fullRemoveItemFromRoom(itemInRoom.getRoomId(), itemInRoom.getName()));

        roomRepository.delete(room);
    }

    public List<Room> findAllRoomsInOffice(Integer officeId) {
        return roomRepository.findAllByOfficeId(officeId);
    }



    public int countItemInRoom(Integer officeId, Integer roomId, String itemName) {
        Room room = findRoomByRoomId(roomId);
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        return room.countItemInRoom(item.getId());
    }


    public void addItemInRoom(Integer officeId, Integer roomId, String itemName, Integer itemCount) {

        Room room = findRoomByRoomId(roomId);
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        room.addItem(item.getId(), itemCount);
        item.setCount(item.getCount() - itemCount);

        saveRoom(officeId, room);
        saveItem(officeId, item);
    }

    public int fullRemoveItemFromRoom(Integer roomId, String itemName) {
        Room room = findRoomByRoomId(roomId);
        Item item = findItemByOfficeIdAndName(room.getOfficeId(), itemName);

        int count = room.fullRemoveItem(item.getId());
        item.setCount(item.getCount() + count);

        saveRoom(room.getOfficeId(), room);
        saveItem(room.getOfficeId(), item);

        return count;
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Boolean isItemExists(Integer itemId) {
        return itemRepository.existsById(itemId);
    }

    private String checkNewItemCountAndGetError(Integer newItemCount) {
        return FieldChecker.checkPositiveIntegerAndGetError(newItemCount);
    }

    public Map<String, Object> checkItemCountAndGetErrorsMap(Integer itemCount) {

        Map<String, Object> errors = new HashMap<>();

        String check = checkNewItemCountAndGetError(itemCount);
        if(check != null) {
            errors.put("item count error", check);
        }

        return errors;
    }

    private String checkItemNameAndGetError(Integer officeId , String newItemName) {
        String error = FieldChecker.checkNullStringAndGetError(newItemName);

        if(error == null && itemRepository.existsByOfficeIdAndName(officeId, newItemName)) {
            error = "item with name = " + newItemName + " in office with office id = " + officeId + " already exist";
        }

        return error;
    }

    public Map<String, Object> checkItemAndGetErrorsMap(Integer officeId, Item newItem, boolean update) {

        Map<String, Object> errors = new HashMap<>();

        if(!isOfficeExists(officeId)) {
            errors.put("field(officeId) error", "office does`t exist for id = " + officeId);
        }

        String check = checkNewItemCountAndGetError(newItem.getCount());
        if(check != null) {
            errors.put("field(count) error", check);
        }

        if(update) {
            check = FieldChecker.checkNullStringAndGetError(newItem.getName());
        } else {
            check = checkItemNameAndGetError(officeId, newItem.getName());
        }
        if(check != null) {
            errors.put("field(name) error", check);
        }


        return errors;
    }

    public Item findItemById(Integer id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Item findItemByOfficeIdAndName(Integer officeId, String name) {
        return itemRepository.findByOfficeIdAndName(officeId, name).orElse(null);
    }

    public boolean isItemExistsByOfficeIdAndName(Integer officeId, String itemName) {
        return itemRepository.existsByOfficeIdAndName(officeId, itemName);
    }

    public void saveItem(Integer officeId, Item newItem) {
        newItem.setOfficeId(officeId);
        itemRepository.save(newItem);
    }

    public void updateItem(Item newItem) {
        Item item = findItemById(newItem.getId());

        newItem.setRoomInventory(item.getRoomInventory());

        itemRepository.save(newItem);
    }

    public void updateItemCount(Integer itemId, Integer newItemCount) {
        Item item = findItemById(itemId);
        item.setCount(newItemCount);
        itemRepository.save(item);
    }

    public void deleteItem(Integer itemId) {
        Item item = findItemById(itemId);
        itemRepository.delete(item);
    }

    public List<ItemInRoom> findAllItemsInRoom(Integer roomId) {
        return itemRepository.findAllItemsInRoom(roomId);
    }

    public List<Item> findAllItemsInOffice(Integer officeId) {
        return itemRepository.findAllByOfficeId(officeId);
    }

}
