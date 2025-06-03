package org.example.m295_nour.controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.services.ZutatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zutaten")
public class ZutatController {

    private final ZutatService zutatService;

    @Autowired
    public ZutatController(ZutatService zutatService) {
        this.zutatService = zutatService;
    }

    @PostMapping
    public ResponseEntity<Zutat> create(@RequestBody Zutat zutat) {
        Zutat gespeichert = zutatService.save(zutat);
        return new ResponseEntity<>(gespeichert, HttpStatus.CREATED);
    }
}
