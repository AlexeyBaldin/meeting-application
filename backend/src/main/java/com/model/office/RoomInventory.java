package com.model.office;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@IdClass(RoomInventoryId.class)
@Table(name="room_inventory")
@Data
public class RoomInventory {

    @Id
    @Column(name = "room_id")
    private int roomId;

    @Id
    @Column(name = "item_id")
    private int itemId;

    @JsonIgnore
    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "item_count")
    private int itemCount;

    public RoomInventory() {}

    public RoomInventory(int roomId, int itemId, int itemCount) {
        this.roomId = roomId;
        this.itemId = itemId;
        this.itemCount = itemCount;
    }
}
