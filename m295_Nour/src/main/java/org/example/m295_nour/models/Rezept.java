package org.example.m295_nour.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Rezept {

    // 🔹 Standard-Konstruktor (Pflicht für JPA)
    public Rezept() {}

    // 🔹 Konstruktor für Tests (ohne Zutatenliste)
    public Rezept(String name, String beschreibung, int dauerInMinuten, boolean istVegetarisch, double bewertung, LocalDate erfasstAm) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.dauerInMinuten = dauerInMinuten;
        this.istVegetarisch = istVegetarisch;
        this.bewertung = bewertung;
        this.erfasstAm = erfasstAm;
    }

    // 🔹 Optional: zusätzlicher Konstruktor mit Zutatenliste
    public Rezept(String name, String beschreibung, int dauerInMinuten, boolean istVegetarisch,
                  double bewertung, LocalDate erfasstAm, List<Zutat> zutaten) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.dauerInMinuten = dauerInMinuten;
        this.istVegetarisch = istVegetarisch;
        this.bewertung = bewertung;
        this.erfasstAm = erfasstAm;
        this.zutaten = zutaten;
    }

    // 🔸 Felder mit JPA & Validierung
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(columnDefinition = "TEXT")
    private String beschreibung;

    @NotNull
    private int dauerInMinuten;

    @NotNull
    private boolean istVegetarisch;

    @NotNull
    private double bewertung;

    @NotNull
    private LocalDate erfasstAm;

    @OneToMany(mappedBy = "rezept", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zutat> zutaten;

    // 🔸 Getter & Setter
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
