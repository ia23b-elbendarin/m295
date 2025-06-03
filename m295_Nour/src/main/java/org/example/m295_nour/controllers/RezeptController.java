package org.example.m295_nour.controllers;

import jakarta.validation.Valid;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.services.RezeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/rezepte")
@Validated

public class RezeptController {

    private static final Logger logger = LoggerFactory.getLogger(RezeptController.class);

    private final RezeptService rezeptService;

    @Autowired
    public RezeptController(RezeptService rezeptService) {
        this.rezeptService = rezeptService;
    }

    // 1. GET: Rezept mit ID lesen
    @GetMapping("/{id}")
    public ResponseEntity<Rezept> getById(@PathVariable Long id) {
        Rezept rezept = rezeptService.findById(id);
        return rezept != null ? ResponseEntity.ok(rezept) : ResponseEntity.notFound().build();
    }

    // 2. GET: Prüfen, ob Rezept existiert
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(rezeptService.existsById(id));
    }

    // 3. GET: Alle Rezepte lesen
    @GetMapping
    public ResponseEntity<List<Rezept>> getAll() {
        return ResponseEntity.ok(rezeptService.findAll());
    }

    // 4. GET: Rezepte nach Text (z. B. Name)
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Rezept>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(rezeptService.findByNameContains(name));
    }

    // 5. GET: Rezepte nach Boolean (Vegetarisch)
    @GetMapping("/vegetarisch/{wert}")
    public ResponseEntity<List<Rezept>> findByVegetarisch(@PathVariable boolean wert) {
        return ResponseEntity.ok(rezeptService.findByIstVegetarisch(wert));
    }

    // 6. POST: Ein neues Rezept speichern
    @PostMapping
    public ResponseEntity<Rezept> create(@Valid @RequestBody Rezept rezept) {
        logger.info("Neues Rezept wird gespeichert: {}", rezept.getName());
        Rezept gespeichert = rezeptService.save(rezept);
        return new ResponseEntity<>(gespeichert, HttpStatus.CREATED);
    }

    // 7. POST: Mehrere neue Rezepte speichern
    @PostMapping("/batch")
    public ResponseEntity<List<Rezept>> createBatch(@RequestBody List<@Valid Rezept> rezepte) {
        return new ResponseEntity<>(rezeptService.saveAll(rezepte), HttpStatus.CREATED);
    }

    // 8. PUT: Rezept aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Rezept> update(@PathVariable Long id, @Valid @RequestBody Rezept updated) {
        if (!rezeptService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updated.setId(id);
        return ResponseEntity.ok(rezeptService.save(updated));
    }

    // 9. DELETE: Rezept mit ID löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rezeptService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 10. DELETE: Rezepte vor einem bestimmten Datum löschen
    @DeleteMapping("/before/{datum}")
    public ResponseEntity<Void> deleteBeforeDate(@PathVariable LocalDate datum) {
        rezeptService.deleteBeforeDate(datum);
        return ResponseEntity.noContent().build();
    }

    // 11. DELETE: Alle löschen
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        rezeptService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
