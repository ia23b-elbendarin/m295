package org.example.m295_nour.servicesTest;

import java.util.Arrays;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.repositories.RezeptRepository;
import org.example.m295_nour.repositories.ZutatRepository;
import org.example.m295_nour.services.ZutatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ZutatServiceTest {

    @Mock
    private ZutatRepository zutatRepository;

    @Mock
    private RezeptRepository rezeptRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private ZutatService zutatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(validator.validate(any())).thenReturn(Set.of());
    }

    @Test
    void testSaveZutat() {
        Rezept rezept = new Rezept();
        rezept.setId(1L);
        Zutat zutat = new Zutat("Salz", 5.0, "g", rezept);

        when(rezeptRepository.findById(1L)).thenReturn(Optional.of(rezept));
        when(zutatRepository.save(any(Zutat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Zutat gespeichert = zutatService.save(zutat);

        assertThat(gespeichert).isNotNull();
        assertThat(gespeichert.getName()).isEqualTo("Salz");
    }

    @Test
    void testSaveZutat_Null() {
        assertThrows(IllegalArgumentException.class, () -> zutatService.save(null));
    }

    @Test
    void testSaveAllZutaten() {
        Rezept rezept = new Rezept();
        rezept.setId(1L);

        Zutat z1 = new Zutat("Mehl", 1.0, "kg", rezept);
        Zutat z2 = new Zutat("Wasser", 0.5, "l", rezept);

        when(rezeptRepository.findById(1L)).thenReturn(Optional.of(rezept));
        when(zutatRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Zutat> result = zutatService.saveAll(List.of(z1, z2));

        assertThat(result).hasSize(2);
    }

    @Test
    void testFindById() {
        Zutat zutat = new Zutat("Butter", 200, "g", new Rezept());
        zutat.setId(2L);

        when(zutatRepository.findById(2L)).thenReturn(Optional.of(zutat));

        Zutat result = zutatService.findById(2L);

        assertThat(result.getName()).isEqualTo("Butter");
    }

    @Test
    void testFindById_NotFound() {
        when(zutatRepository.findById(99L)).thenReturn(Optional.empty());

        Zutat result = zutatService.findById(99L);
        assertThat(result).isNull();
    }

    @Test
    void testExistsById() {
        when(zutatRepository.existsById(1L)).thenReturn(true);
        assertThat(zutatService.existsById(1L)).isTrue();
    }

    @Test
    void testFindAll() {
        Zutat z1 = new Zutat("Eier", 3, "Stk", new Rezept());
        Zutat z2 = new Zutat("Milch", 0.25, "l", new Rezept());

        when(zutatRepository.findAll()).thenReturn(List.of(z1, z2));

        List<Zutat> result = zutatService.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void testFindByNameContains() {
        Zutat zutat = new Zutat("Zucker", 100, "g", new Rezept());

        when(zutatRepository.findByNameContainingIgnoreCase("zuck")).thenReturn(List.of(zutat));

        List<Zutat> result = zutatService.findByNameContains("zuck");
        assertThat(result).hasSize(1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(zutatRepository).deleteById(1L);
        zutatService.deleteById(1L);
        verify(zutatRepository).deleteById(1L);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(zutatRepository).deleteAll();
        zutatService.deleteAll();
        verify(zutatRepository).deleteAll();
    }

    @Test
    void testFindByRezeptId() {
        Rezept rezept = new Rezept();
        rezept.setId(1L);
        Zutat zutat = new Zutat("Honig", 2.5, "EL", rezept);
        zutat.setId(99L);

        when(zutatRepository.findAll()).thenReturn(List.of(zutat));

        List<Zutat> result = zutatService.findByRezeptId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Honig");
    }

    @Test
    void testSaveAllZutatenWithNullList() {
        assertThrows(IllegalArgumentException.class, () -> zutatService.saveAll(null));
    }

    @Test
    void testSaveAllZutatenWithNullElement() {
        Zutat z1 = new Zutat("Öl", 2.0, "ml", new Rezept());
        List<Zutat> listeMitNull = Arrays.asList(z1, null);

        assertThrows(IllegalArgumentException.class, () -> zutatService.saveAll(listeMitNull));
    }

    @Test
    void testSaveZutatWithValidationError() {
        Zutat zutat = new Zutat(); // ungültig
        when(validator.validate(any())).thenReturn(Set.of(mock(ConstraintViolation.class)));

        assertThrows(ConstraintViolationException.class, () -> zutatService.save(zutat));
    }

    @Test
    void testSaveZutatWithMissingRezeptId() {
        Zutat zutat = new Zutat("Öl", 2.0, "ml", new Rezept());

        assertThrows(IllegalArgumentException.class, () -> zutatService.save(zutat));
    }

    @Test
    void testSaveZutatWithInvalidRezeptId() {
        Rezept rezept = new Rezept();
        rezept.setId(999L);
        Zutat zutat = new Zutat("Pfeffer", 1.0, "TL", rezept);

        when(rezeptRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> zutatService.save(zutat));
    }


}
