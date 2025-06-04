package org.example.m295_nour.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.repositories.RezeptRepository;
import org.example.m295_nour.repositories.ZutatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ZutatService {

    private static final Logger logger = LoggerFactory.getLogger(ZutatService.class);

    @Autowired
    private ZutatRepository zutatRepository;

    @Autowired
    private RezeptRepository rezeptRepository;

    @Autowired
    private Validator validator;

    public Zutat save(Zutat zutat) {
        if (zutat == null) {
            throw new IllegalArgumentException("Zutat darf nicht null sein");
        }

        validate(zutat);

        if (zutat.getRezept() == null || zutat.getRezept().getId() == null) {
            throw new IllegalArgumentException("Rezept-Referenz fehlt");
        }

        Rezept rezept = rezeptRepository.findById(zutat.getRezept().getId())
                .orElseThrow(() -> new IllegalArgumentException("Rezept mit ID " + zutat.getRezept().getId() + " nicht gefunden"));

        zutat.setRezept(rezept);
        return zutatRepository.save(zutat);
    }

    public List<Zutat> saveAll(List<Zutat> zutaten) {
        if (zutaten == null || zutaten.stream().anyMatch(z -> z == null)) {
            throw new IllegalArgumentException("Liste enthält null-Zutat oder ist null");
        }

        zutaten.forEach(this::validate);
        zutaten.forEach(z -> {
            if (z.getRezept() == null || z.getRezept().getId() == null) {
                throw new IllegalArgumentException("Rezept-Referenz fehlt");
            }
            Rezept rezept = rezeptRepository.findById(z.getRezept().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Rezept mit ID " + z.getRezept().getId() + " nicht gefunden"));
            z.setRezept(rezept);
        });

        return zutatRepository.saveAll(zutaten);
    }


    public Zutat findById(Long id) {
        return zutatRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return zutatRepository.existsById(id);
    }

    public List<Zutat> findAll() {
        return zutatRepository.findAll();
    }

    public List<Zutat> findByNameContains(String name) {
        return zutatRepository.findByNameContainingIgnoreCase(name);
    }

    public void deleteById(Long id) {
        logger.warn("Zutat mit ID {} wird gelöscht", id);
        zutatRepository.deleteById(id);
    }

    public void deleteAll() {
        logger.warn("Alle Zutaten werden gelöscht");
        zutatRepository.deleteAll();
    }

    public List<Zutat> findByRezeptId(Long rezeptId) {
        return zutatRepository.findAll().stream()
                .filter(z -> z.getRezept() != null && rezeptId.equals(z.getRezept().getId()))
                .collect(Collectors.toList());
    }

    private void validate(Zutat zutat) {
        Set<ConstraintViolation<Zutat>> violations = validator.validate(zutat);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
