package org.example.m295_nour.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Rezept {

    public Rezept() {
    }

    public Rezept(String name, String beschreibung, int dauerInMinuten, boolean istVegetarisch, double bewertung, LocalDate erfasstAm) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.dauerInMinuten = dauerInMinuten;
        this.istVegetarisch = istVegetarisch;
        this.bewertung = bewertung;
        this.erfasstAm = erfasstAm;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name darf nicht leer sein")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Size(min = 10, max = 500, message = "Beschreibung muss zwischen 10 und 500 Zeichen sein")
    private String beschreibung;

    @Column(nullable = false)
    @Min(value = 1, message = "Dauer muss mindestens 1 Minute betragen")
    private int dauerInMinuten;

    @Column(nullable = false)
    private boolean istVegetarisch;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Bewertung darf nicht negativ sein")
    @DecimalMax(value = "5.0", inclusive = true, message = "Bewertung darf maximal 5.0 sein")
    private double bewertung;

    @Column(nullable = false)
    @PastOrPresent(message = "Datum darf nicht in der Zukunft liegen")
    private LocalDate erfasstAm;

    @OneToMany(mappedBy = "rezept", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zutat> zutaten;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getDauerInMinuten() {
        return dauerInMinuten;
    }

    public void setDauerInMinuten(int dauerInMinuten) {
        this.dauerInMinuten = dauerInMinuten;
    }

    public boolean isIstVegetarisch() {
        return istVegetarisch;
    }

    public void setIstVegetarisch(boolean istVegetarisch) {
        this.istVegetarisch = istVegetarisch;
    }

    public double getBewertung() {
        return bewertung;
    }

    public void setBewertung(double bewertung) {
        this.bewertung = bewertung;
    }

    public LocalDate getErfasstAm() {
        return erfasstAm;
    }

    public void setErfasstAm(LocalDate erfasstAm) {
        this.erfasstAm = erfasstAm;
    }

    public List<Zutat> getZutaten() {
        return zutaten;
    }

    public void setZutaten(List<Zutat> zutaten) {
        this.zutaten = zutaten;
    }
}
