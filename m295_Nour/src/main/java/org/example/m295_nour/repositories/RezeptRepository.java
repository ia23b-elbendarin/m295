package org.example.m295_nour.repositories;

import org.example.m295_nour.models.Rezept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RezeptRepository extends JpaRepository<Rezept, Long> {
    List<Rezept> findByNameContainingIgnoreCase(String name);
    List<Rezept> findByIstVegetarisch(boolean istVegetarisch);
    List<Rezept> findByErfasstAmBefore(LocalDate date);
}
