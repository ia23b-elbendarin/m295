package org.example.m295_nour.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Zutat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double menge;

    @NotNull
    private String einheit;

    @ManyToOne
    @JoinColumn(name = "rezept_id", nullable = false)
    private Rezept rezept;

    public Zutat() {}

    public Zutat(String name, double menge, String einheit, Rezept rezept) {
        this.name = name;
        this.menge = menge;
        this.einheit = einheit;
        this.rezept = rezept;
    }

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

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }


    public Rezept getRezept() {
        return rezept;
    }

    public void setRezept(Rezept rezept) {
        this.rezept = rezept;
    }

}

