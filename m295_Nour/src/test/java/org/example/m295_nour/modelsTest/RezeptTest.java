package org.example.m295_nour.modelsTest;

import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RezeptTest {

    @Test
    void testKonstruktorUndGetter() {
        LocalDate datum = LocalDate.of(2024, 1, 1);
        Rezept rezept = new Rezept("Pizza", "Mit Käse", 30, true, 4.8, datum);

        assertThat(rezept.getName()).isEqualTo("Pizza");
        assertThat(rezept.getBeschreibung()).isEqualTo("Mit Käse");
        assertThat(rezept.getDauerInMinuten()).isEqualTo(30);
        assertThat(rezept.isIstVegetarisch()).isTrue();
        assertThat(rezept.getBewertung()).isEqualTo(4.8);
        assertThat(rezept.getErfasstAm()).isEqualTo(datum);
    }

    @Test
    void testSetterUndGetter() {
        Rezept rezept = new Rezept();
        LocalDate datum = LocalDate.of(2023, 5, 15);

        rezept.setId(10L);
        rezept.setName("Risotto");
        rezept.setBeschreibung("Mit Pilzen");
        rezept.setDauerInMinuten(45);
        rezept.setIstVegetarisch(false);
        rezept.setBewertung(3.9);
        rezept.setErfasstAm(datum);

        assertThat(rezept.getId()).isEqualTo(10L);
        assertThat(rezept.getName()).isEqualTo("Risotto");
        assertThat(rezept.getBeschreibung()).isEqualTo("Mit Pilzen");
        assertThat(rezept.getDauerInMinuten()).isEqualTo(45);
        assertThat(rezept.isIstVegetarisch()).isFalse();
        assertThat(rezept.getBewertung()).isEqualTo(3.9);
        assertThat(rezept.getErfasstAm()).isEqualTo(datum);
    }

    @Test
    void testZutatenSetterUndGetter() {
        Rezept rezept = new Rezept();
        Zutat zutat1 = new Zutat("Tomaten", 100, "g", rezept);
        Zutat zutat2 = new Zutat("Zwiebeln", 50, "g", rezept);

        rezept.setZutaten(List.of(zutat1, zutat2));

        assertThat(rezept.getZutaten()).hasSize(2);
        assertThat(rezept.getZutaten()).contains(zutat1, zutat2);
    }
}
