package com.repository.office;

import com.dto.query.ItemInRoom;
import com.model.office.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByOfficeIdAndName(Integer officeId, String name);

    List<Item> findAllByOfficeId(Integer officeId);

    Boolean existsByOfficeIdAndName(@Param("office_id")Integer officeId, @Param("item_name")String name);

    @Query(value = "SELECT items.item_id, room_inventory.room_id, items.item_name, room_inventory.item_count FROM items " +
            "JOIN room_inventory USING(item_id) WHERE room_id = :room_id",
            nativeQuery = true)
    List<ItemInRoom> findAllItemsInRoom(@Param("room_id")Integer roomId);
}
