package org.example.m295_nour.controllers;

import jakarta.validation.Valid;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.services.ZutatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zutaten")
public class ZutatController {

    @Autowired
    private ZutatService zutatService;

    // Alle dürfen Zutaten anzeigen
    @GetMapping
    public ResponseEntity<List<Zutat>> findAll() {
        return ResponseEntity.ok(zutatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zutat> findById(@PathVariable Long id) {
        Zutat zutat = zutatService.findById(id);
        return zutat != null ? ResponseEntity.ok(zutat) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(zutatService.existsById(id));
    }

    @GetMapping("/suche")
    public ResponseEntity<List<Zutat>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(zutatService.findByNameContains(name));
    }

    // Nur ADMIN darf neue Zutat speichern
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Zutat> save(@Valid @RequestBody Zutat zutat) {
        return ResponseEntity.ok(zutatService.save(zutat));
    }

    // Nur ADMIN darf mehrere Zutaten speichern
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/alle")
    public ResponseEntity<List<Zutat>> saveAll(@RequestBody List<Zutat> zutaten) {
        return ResponseEntity.ok(zutatService.saveAll(zutaten));
    }

    // Alle dürfen Zutaten zu einem Rezept abrufen
    @GetMapping("/rezept/{rezeptId}")
    public ResponseEntity<List<Zutat>> findByRezeptId(@PathVariable Long rezeptId) {
        return ResponseEntity.ok(zutatService.findByRezeptId(rezeptId));
    }

    // Nur ADMIN darf eine Zutat löschen
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        zutatService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Nur ADMIN darf alle Zutaten löschen
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        zutatService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Zutat> update(@PathVariable Long id, @Valid @RequestBody Zutat zutat) {
        if (!zutatService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        zutat.setId(id);
        Zutat updated = zutatService.save(zutat);
        return ResponseEntity.ok(updated);
    }

}
