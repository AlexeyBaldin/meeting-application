package com.repository.employee;

import com.model.employee.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Boolean existsByName(String name);

    Role findByName(String name);
}
