package com.repository.office;

import com.model.office.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
}
