package org.example.m295_nour.repositories;

import org.example.m295_nour.models.Zutat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZutatRepository extends JpaRepository<Zutat, Long> {
    List<Zutat> findByNameContainingIgnoreCase(String name);

    List<Zutat> findByRezeptId(Long rezeptId);

}
