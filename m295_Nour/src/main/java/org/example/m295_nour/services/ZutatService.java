package org.example.m295_nour.services;

import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.repositories.ZutatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZutatService {

    private static final Logger logger = LoggerFactory.getLogger(ZutatService.class);

    @Autowired
    private ZutatRepository zutatRepository;

    public Zutat save(Zutat zutat) {
        logger.info("Zutat wird gespeichert: {}", zutat.getName());
        return zutatRepository.save(zutat);
    }

    public List<Zutat> saveAll(List<Zutat> zutaten) {
        logger.info("Mehrere Zutaten werden gespeichert, Anzahl: {}", zutaten.size());
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
}
