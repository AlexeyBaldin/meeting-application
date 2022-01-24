package com.model.office;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @Column(name = "office_id")
    private int officeId;

    @Column(name = "room_cabinet")
    private int cabinet;

    @Column(name = "room_capacity")
    private int capacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomInventory> roomInventory;

    public int countItemInRoom(Integer itemId) {
        for (RoomInventory rItem : roomInventory) {
            if (rItem.getItemId() == itemId) {
                return rItem.getItemCount();
            }
        }
        return 0;
    }

    public void addItem(Integer itemId, Integer itemCount) {
        for (RoomInventory rItem : roomInventory) {
            if (rItem.getItemId() == itemId) {
                rItem.setItemCount(rItem.getItemCount() + itemCount);
                return;
            }
        }
        roomInventory.add(new RoomInventory(id, itemId, itemCount));
    }

    public int fullRemoveItem(Integer itemId) {
        int count = 0;
        RoomInventory deleted = null;
        for (RoomInventory rItem : roomInventory) {
            if (rItem.getItemId() == itemId) {
                count = rItem.getItemCount();
                deleted = rItem;
            }
        }
        roomInventory.remove(deleted);
        return count;
    }
}
