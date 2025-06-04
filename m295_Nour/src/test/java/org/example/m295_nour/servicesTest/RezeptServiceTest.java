package org.example.m295_nour.servicesTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.repositories.RezeptRepository;
import org.example.m295_nour.services.RezeptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RezeptServiceTest {

    @Mock
    private RezeptRepository rezeptRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private RezeptService rezeptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Rezept createRezept(String name) {
        Rezept rezept = new Rezept();
        rezept.setName(name);
        rezept.setBeschreibung("gut");
        rezept.setDauerInMinuten(30);
        rezept.setIstVegetarisch(true);
        rezept.setBewertung(4.0);
        rezept.setErfasstAm(LocalDate.now());
        return rezept;
    }

    @Test
    void testSaveValidRezept() {
        Rezept rezept = createRezept("Lasagne");

        when(rezeptRepository.save(any())).thenReturn(rezept);

        Rezept result = rezeptService.save(rezept);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Lasagne");
    }

    @Test
    void testFindById() {
        Rezept rezept = createRezept("Suppe");
        rezept.setId(1L);
        when(rezeptRepository.findById(1L)).thenReturn(Optional.of(rezept));

        Rezept result = rezeptService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Suppe");
    }

    @Test
    void testFindAll() {
        Rezept r1 = createRezept("Pizza");
        Rezept r2 = createRezept("Burger");

        when(rezeptRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Rezept> result = rezeptService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void testDeleteById() {
        rezeptService.deleteById(1L);
        verify(rezeptRepository).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(rezeptRepository.existsById(1L)).thenReturn(true);

        boolean exists = rezeptService.existsById(1L);

        assertThat(exists).isTrue();
        verify(rezeptRepository).existsById(1L);
    }

    @Test
    void testSaveValidRezeptWithEmptyViolations() {
        Rezept rezept = createRezept("Gulasch");

        when(validator.validate(any(Rezept.class))).thenReturn(Set.of());
        when(rezeptRepository.save(any())).thenReturn(rezept);

        Rezept result = rezeptService.save(rezept);

        assertThat(result.getName()).isEqualTo("Gulasch");
        verify(validator).validate(rezept);
        verify(rezeptRepository).save(rezept);
    }


    @Test
    void testSaveInvalidRezeptThrowsException() {
        Rezept rezept = createRezept("");
        ConstraintViolation<Rezept> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Rezept>> violations = Set.of(violation);

        when(validator.validate(any(Rezept.class))).thenReturn(violations); // sollte jetzt gehen

        assertThrows(IllegalArgumentException.class, () -> rezeptService.save(rezept));

        verify(validator).validate(rezept);
        verifyNoInteractions(rezeptRepository);
    }

    @Test
    void testSaveAllValidRezepte() {
        Rezept r1 = createRezept("Chili");
        Rezept r2 = createRezept("Curry");

        when(validator.validate(r1)).thenReturn(Set.of());
        when(validator.validate(r2)).thenReturn(Set.of());
        when(rezeptRepository.saveAll(anyList())).thenReturn(List.of(r1, r2));

        List<Rezept> result = rezeptService.saveAll(List.of(r1, r2));

        assertThat(result).hasSize(2);
        verify(validator).validate(r1);
        verify(validator).validate(r2);
        verify(rezeptRepository).saveAll(List.of(r1, r2));
    }

    @Test
    void testFindByIstVegetarisch() {
        Rezept vegi = createRezept("Salat");
        Rezept nonVegi = createRezept("Schnitzel");
        nonVegi.setIstVegetarisch(false);

        when(rezeptRepository.findByIstVegetarisch(true)).thenReturn(List.of(vegi));

        List<Rezept> result = rezeptService.findByIstVegetarisch(true);

        assertThat(result).containsExactly(vegi);
        verify(rezeptRepository).findByIstVegetarisch(true);
    }

    @Test
    void testDeleteBefore() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        rezeptService.deleteBefore(date);
        verify(rezeptRepository).deleteByErfasstAmBefore(date);
    }

    @Test
    void testDeleteAll() {
        rezeptService.deleteAll();
        verify(rezeptRepository).deleteAll();
    }

}
