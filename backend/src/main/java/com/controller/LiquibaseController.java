package com.controller;

import com.model.Liquibase;
import com.repository.LiquibaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/liquibase")
public class LiquibaseController {

    @Autowired
    LiquibaseRepository liquibaseRepository;

    @GetMapping("/all")
    public List<Liquibase> getAll() {
        return liquibaseRepository.findAll();
    }

}
