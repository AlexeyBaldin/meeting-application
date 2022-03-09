package com.dto.query;

import org.springframework.beans.factory.annotation.Value;

public interface ItemInRoom {

    @Value("#{target.item_id}")
    int getId();

    @Value("#{target.room_id}")
    int getRoomId();

    @Value("#{target.item_name}")
    String getName();

    @Value("#{target.item_count}")
    int getItemCount();
}
