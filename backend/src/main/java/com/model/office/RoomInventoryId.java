package com.model.office;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomInventoryId implements Serializable {

    private int itemId;

    private int roomId;

    public RoomInventoryId() {}

    public RoomInventoryId(int itemId, int officeId, int roomId) {
        this.itemId = itemId;
        this.roomId = roomId;
    }
}
