package com.repository;

import com.model.Liquibase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquibaseRepository extends JpaRepository<Liquibase, Integer> {
}
