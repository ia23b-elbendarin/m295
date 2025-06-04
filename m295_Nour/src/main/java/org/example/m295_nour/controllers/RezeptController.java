package org.example.m295_nour.controllers;

import jakarta.validation.Valid;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.services.RezeptService;
import org.example.m295_nour.services.ZutatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rezepte")
@Validated
public class RezeptController {

    private final RezeptService rezeptService;
    private final ZutatService zutatService;

    @Autowired
    public RezeptController(RezeptService rezeptService, ZutatService zutatService) {
        this.rezeptService = rezeptService;
        this.zutatService = zutatService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Rezept> create(@Valid @RequestBody Rezept rezept) {
        Rezept gespeichert = rezeptService.save(rezept);
        return new ResponseEntity<>(gespeichert, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/alle")
    public ResponseEntity<List<Rezept>> saveAll(@RequestBody List<Rezept> rezepte) {
        return ResponseEntity.ok(rezeptService.saveAll(rezepte));
    }


    @GetMapping
    public ResponseEntity<List<Rezept>> getAll() {
        return ResponseEntity.ok(rezeptService.findAll());
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(rezeptService.existsById(id));
    }

    @GetMapping("/vegetarisch")
    public ResponseEntity<List<Rezept>> findByVegetarisch(@RequestParam boolean wert) {
        return ResponseEntity.ok(rezeptService.findByIstVegetarisch(wert));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rezept> getById(@PathVariable Long id) {
        Rezept rezept = rezeptService.findById(id);
        return rezept != null
                ? ResponseEntity.ok(rezept)
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Rezept> updateRezept(@PathVariable Long id, @Valid @RequestBody Rezept rezept) {
        if (!rezeptService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rezept.setId(id);
        Rezept updated = rezeptService.save(rezept);
        return ResponseEntity.ok(updated);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rezeptService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/vorDatum")
    public ResponseEntity<Void> deleteBefore(@RequestParam("datum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum) {
        rezeptService.deleteBefore(datum);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        rezeptService.deleteAll();
        return ResponseEntity.noContent().build();
    }


}
