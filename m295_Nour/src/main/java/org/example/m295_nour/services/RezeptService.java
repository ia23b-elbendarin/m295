package org.example.m295_nour.services;


import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.repositories.RezeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RezeptService {

    private final RezeptRepository rezeptRepository;

    @Autowired
    public RezeptService(RezeptRepository rezeptRepository) {
        this.rezeptRepository = rezeptRepository;
    }

    public Rezept findById(Long id) {
        return rezeptRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return rezeptRepository.existsById(id);
    }

    public List<Rezept> findAll() {
        return rezeptRepository.findAll();
    }

    public List<Rezept> findByNameContains(String name) {
        return rezeptRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Rezept> findByIstVegetarisch(boolean istVegetarisch) {
        return rezeptRepository.findByIstVegetarisch(istVegetarisch);
    }

    public Rezept save(Rezept rezept) {
        return rezeptRepository.save(rezept);
    }

    public List<Rezept> saveAll(List<Rezept> rezepte) {
        return rezeptRepository.saveAll(rezepte);
    }

    public void deleteById(Long id) {
        rezeptRepository.deleteById(id);
    }

    public void deleteBeforeDate(LocalDate datum) {
        List<Rezept> toDelete = rezeptRepository.findByErfasstAmBefore(datum);
        rezeptRepository.deleteAll(toDelete);
    }

    public void deleteAll() {
        rezeptRepository.deleteAll();
    }
}
