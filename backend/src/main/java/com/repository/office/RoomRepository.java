package com.repository.office;

import com.model.office.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAllByOfficeId(Integer officeId);

    Optional<Room> findByOfficeIdAndId(Integer officeId, Integer roomId);

    Boolean existsByOfficeIdAndCabinet(@Param("office_id")Integer officeId, @Param("room_cabinet")Integer cabinet);
}
