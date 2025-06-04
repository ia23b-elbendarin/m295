package org.example.m295_nour.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Zutat {

    public Zutat() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name darf nicht leer sein")
    @Pattern(regexp = "^[A-Za-zÄÖÜäöüß\\s-]+$", message = "Name darf nur Buchstaben und Leerzeichen enthalten")
    private String name;

    @Column(nullable = false)
    @DecimalMin(value = "0.1", message = "Menge muss mindestens 0.1 betragen")
    private double menge;

    @Column(nullable = false)
    @NotBlank(message = "Einheit darf nicht leer sein")
    private String einheit;

    @ManyToOne
    @JoinColumn(name = "rezept_id", nullable = false)
    private Rezept rezept;


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

