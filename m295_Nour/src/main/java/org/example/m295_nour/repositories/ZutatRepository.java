package org.example.m295_nour.repositories;

import org.example.m295_nour.models.Zutat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ZutatRepository extends JpaRepository<Zutat, Long> {
    List<Zutat> findByNameContainingIgnoreCase(String name);
}
