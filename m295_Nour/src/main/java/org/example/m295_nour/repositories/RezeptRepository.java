package org.example.m295_nour.repositories;

import org.example.m295_nour.models.Rezept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RezeptRepository extends JpaRepository<Rezept, Long> {
    List<Rezept> findByNameContainingIgnoreCase(String name);

    List<Rezept> findByIstVegetarisch(boolean istVegetarisch);

    List<Rezept> findByErfasstAmBefore(LocalDate date);

    void deleteByErfasstAmBefore(LocalDate datum);
}
