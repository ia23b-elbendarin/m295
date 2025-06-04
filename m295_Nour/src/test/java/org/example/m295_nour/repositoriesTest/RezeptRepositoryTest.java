package org.example.m295_nour.repositoriesTest;

import jakarta.transaction.Transactional;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.repositories.RezeptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@ActiveProfiles("test")
public class RezeptRepositoryTest {

    @Autowired
    private RezeptRepository rezeptRepository;

    @BeforeEach
    void setUp() {
        rezeptRepository.deleteAll(); // sauberer Zustand
        rezeptRepository.save(new Rezept("Salat", "Frischer Salat", 10, true, 4.5, LocalDate.of(2022, 1, 1)));
        rezeptRepository.save(new Rezept("Schnitzel", "Mit Pommes", 30, false, 4.0, LocalDate.of(2023, 1, 1)));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Rezept> result = rezeptRepository.findByNameContainingIgnoreCase("schnitzel");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Schnitzel");
    }

    @Test
    void testFindByIstVegetarisch() {
        List<Rezept> result = rezeptRepository.findByIstVegetarisch(true);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Salat");
    }

    @Test
    void testFindByErfasstAmBefore() {
        List<Rezept> result = rezeptRepository.findByErfasstAmBefore(LocalDate.of(2023, 1, 1));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Salat");
    }

    @Test
    void testDeleteByErfasstAmBefore() {
        rezeptRepository.deleteByErfasstAmBefore(LocalDate.of(2023, 1, 1));
        List<Rezept> all = rezeptRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Schnitzel");
    }
}
