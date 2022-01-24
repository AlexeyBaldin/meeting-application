package com.repository.office;

import com.model.office.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByOfficeIdAndName(Integer officeId, String name);

    Boolean existsByOfficeIdAndName(@Param("office_id")Integer officeId, @Param("item_name")String name);
}
