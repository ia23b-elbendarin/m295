package org.example.m295_nour.repositoriesTest;

import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.repositories.RezeptRepository;
import org.example.m295_nour.repositories.ZutatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ZutatRepositoryTest {

    @Autowired
    private ZutatRepository zutatRepository;

    @Autowired
    private RezeptRepository rezeptRepository;

    private Rezept rezept;

    @BeforeEach
    void setUp() {
        rezeptRepository.deleteAll();

        rezept = rezeptRepository.save(
                new Rezept("Test-Rezept", "Beschreibung", 15, true, 4.0, LocalDate.now())
        );

        zutatRepository.save(new Zutat("Tomate", 100, "g", rezept));
        zutatRepository.save(new Zutat("Gurke", 150, "g", rezept));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Zutat> result = zutatRepository.findByNameContainingIgnoreCase("gurke");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Gurke");
    }

    @Test
    void testFindByRezeptId() {
        List<Zutat> result = zutatRepository.findByRezeptId(rezept.getId());
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Zutat::getName)
                .containsExactlyInAnyOrder("Tomate", "Gurke");
    }
}
