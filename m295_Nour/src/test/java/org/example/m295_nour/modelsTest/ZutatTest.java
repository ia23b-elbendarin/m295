package org.example.m295_nour.modelsTest;

import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ZutatTest {

    @Test
    void testZutatConstructorAndGetters() {
        Rezept rezept = new Rezept("Spaghetti", "lecker", 25, true, 4.5, LocalDate.now());
        Zutat zutat = new Zutat("Salz", 5.0, "g", rezept);

        assertThat(zutat.getName()).isEqualTo("Salz");
        assertThat(zutat.getMenge()).isEqualTo(5.0);
        assertThat(zutat.getEinheit()).isEqualTo("g");
        assertThat(zutat.getRezept()).isEqualTo(rezept);
    }

    @Test
    void testSetters() {
        Rezept rezept = new Rezept();
        Zutat zutat = new Zutat();

        zutat.setId(1L);
        zutat.setName("Wasser");
        zutat.setMenge(100.0);
        zutat.setEinheit("ml");
        zutat.setRezept(rezept);

        assertThat(zutat.getId()).isEqualTo(1L);
        assertThat(zutat.getName()).isEqualTo("Wasser");
        assertThat(zutat.getMenge()).isEqualTo(100.0);
        assertThat(zutat.getEinheit()).isEqualTo("ml");
        assertThat(zutat.getRezept()).isEqualTo(rezept);
    }
}
