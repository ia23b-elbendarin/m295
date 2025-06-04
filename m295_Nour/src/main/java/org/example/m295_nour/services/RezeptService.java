package org.example.m295_nour.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.repositories.RezeptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class RezeptService {

    private final RezeptRepository rezeptRepository;
    private final Validator validator;

    public RezeptService(RezeptRepository rezeptRepository, Validator validator) {
        this.rezeptRepository = rezeptRepository;
        this.validator = validator;
    }

    public Rezept save(Rezept rezept) {
        validateEntity(rezept);
        return rezeptRepository.save(rezept);
    }

    public Rezept findById(Long id) {
        return rezeptRepository.findById(id).orElse(null);
    }

    public List<Rezept> findAll() {
        return rezeptRepository.findAll();
    }

    public void deleteById(Long id) {
        rezeptRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return rezeptRepository.existsById(id);
    }

    private void validateEntity(Rezept rezept) {
        Set<ConstraintViolation<Rezept>> violations = validator.validate(rezept);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Ungültiges Rezept: " + violations);
        }

    }

    public List<Rezept> saveAll(List<Rezept> rezepte) {
        rezepte.forEach(this::validateEntity);
        return rezeptRepository.saveAll(rezepte);
    }

    public List<Rezept> findByIstVegetarisch(boolean istVegetarisch) {
        return rezeptRepository.findByIstVegetarisch(istVegetarisch);
    }

    public void deleteBefore(LocalDate datum) {
        rezeptRepository.deleteByErfasstAmBefore(datum);
    }


    public void deleteAll() {
        rezeptRepository.deleteAll();
    }


}
