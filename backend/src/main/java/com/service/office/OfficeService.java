package com.service.office;

import com.model.office.Item;
import com.model.office.Office;
import com.model.office.Room;
import com.repository.office.ItemRepository;
import com.repository.office.OfficeRepository;
import com.repository.office.RoomRepository;
import com.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    CheckService checkService;

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
        return checkService.checkNullStringAndGetError(city);
    }

    private String checkAddressAndGetError(String address) {
        return checkService.checkNullStringAndGetError(address);
    }

    private String checkPhoneAndGetError(String phone) {
        return checkService.checkNullStringAndGetError(phone);
    }

    private String checkOpenTimeAndGetError(LocalTime open) {
        return checkService.checkNullLocalTimeAndGetError(open);
    }

    private String checkCloseTimeAndGetError(LocalTime close) {
        return checkService.checkNullLocalTimeAndGetError(close);
    }

    private String checkRoomCabinetAndGetError(Integer officeId, Integer cabinet) {
        String error = checkService.checkPositiveIntegerAndGetError(cabinet);

        if(error == null && roomRepository.existsByOfficeIdAndCabinet(officeId, cabinet)) {
            error = "room with cabinet = " + cabinet + " in office with office id = " + officeId + " already exist";
        }
        return error;
    }

    private String checkRoomCapacityAndGetError(Integer capacity) {
        return checkService.checkPositiveIntegerAndGetError(capacity);
    }

    public Map<String, Object> checkOfficeAndGetErrorsMap(Office newOffice) {
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

        check = checkOpenTimeAndGetError(newOffice.getOpen());
        if(check != null) {
            errors.put("field(open) error", check);
        }

        check = checkCloseTimeAndGetError(newOffice.getClose());
        if(check != null) {
            errors.put("field(close) error", check);
        }

        return errors;
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

    public Room findRoomByOfficeIdAndRoomId(Integer officeId, Integer roomId) {
        return roomRepository.findByOfficeIdAndId(officeId, roomId).orElse(null);
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

    public void deleteRoom(Integer officeId, Integer roomId) {
        Room room = findRoomByOfficeIdAndRoomId(officeId, roomId);

        roomRepository.delete(room);
    }

    public List<Room> findAllRoomsInOffice(Integer officeId) {
        return roomRepository.findAllByOfficeId(officeId);
    }



    public int countItemInRoom(Integer officeId, Integer roomId, String itemName) {
        Room room = findRoomByOfficeIdAndRoomId(officeId, roomId);
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        return room.countItemInRoom(item.getId());
    }


    public void addItemInRoom(Integer officeId, Integer roomId, String itemName, Integer itemCount) {

        Room room = findRoomByOfficeIdAndRoomId(officeId, roomId);
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        room.addItem(item.getId(), itemCount);
        item.setCount(item.getCount() - itemCount);

        saveRoom(officeId, room);
        saveItem(officeId, item);
    }

    public void fullRemoveItemFromRoom(Integer officeId, Integer roomId, String itemName) {
        Room room = findRoomByOfficeIdAndRoomId(officeId, roomId);
        Item item = findItemByOfficeIdAndName(officeId, itemName);

        int count = room.fullRemoveItem(item.getId());
        item.setCount(item.getCount() + count);

        saveRoom(officeId, room);
        saveItem(officeId, item);
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Boolean isItemExists(Integer itemId) {
        return itemRepository.existsById(itemId);
    }

    private String checkNewItemCountAndGetError(Integer newItemCount) {
        return checkService.checkPositiveIntegerAndGetError(newItemCount);
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
        String error = checkService.checkNullStringAndGetError(newItemName);

        if(error == null && itemRepository.existsByOfficeIdAndName(officeId, newItemName)) {
            error = "item with name = " + newItemName + " in office with office id = " + officeId + " already exist";
        }

        return error;
    }

    public Map<String, Object> checkItemAndGetErrorsMap(Integer officeId, Item newItem) {

        Map<String, Object> errors = new HashMap<>();

        if(!isOfficeExists(officeId)) {
            errors.put("field(officeId) error", "office does`t exist for id = " + officeId);
        }

        String check = checkNewItemCountAndGetError(newItem.getCount());
        if(check != null) {
            errors.put("field(count) error", check);
        }

        check = checkItemNameAndGetError(officeId, newItem.getName());
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

    public void updateItemCount(Integer itemId, Integer newItemCount) {
        Item item = findItemById(itemId);
        item.setCount(newItemCount);
        itemRepository.save(item);
    }

    public void deleteItem(Integer itemId) {
        Item item = findItemById(itemId);
        itemRepository.delete(item);
    }

}
