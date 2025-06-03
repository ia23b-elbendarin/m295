package org.example.m295_nour.servicesTest;

import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.repositories.RezeptRepository;
import org.example.m295_nour.services.RezeptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RezeptServiceTest {

    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private RezeptService rezeptService;

    // Positiv-Test: findById
    @Test
    void testFindById_validId_returnsRezept() {
        Rezept rezept = new Rezept("Spaghetti", "Lecker!", 20, true, 4.5, LocalDate.now());
        when(rezeptRepository.findById(1L)).thenReturn(Optional.of(rezept));

        Rezept result = rezeptService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Spaghetti");
    }

    // Negativ-Test: findById mit ungültiger ID
    @Test
    void testFindById_invalidId_returnsNull() {
        when(rezeptRepository.findById(99L)).thenReturn(Optional.empty());

        Rezept result = rezeptService.findById(99L);

        assertThat(result).isNull();
    }

    // Randbedingung: findById mit null (wird null-gesichert behandelt)
    @Test
    void testFindById_nullId_returnsNull() {
        Rezept result = rezeptService.findById(null);
        assertThat(result).isNull();
        verify(rezeptRepository, never()).findById(any());
    }

    // Positiv-Test: save()
    @Test
    void testSave_validRezept() {
        Rezept rezept = new Rezept("Salat", "Frisch", 10, true, 3.5, LocalDate.now());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        Rezept result = rezeptService.save(rezept);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Salat");
        verify(rezeptRepository).save(rezept);
    }

    // Randfall: save() mit null
    @Test
    void testSave_nullRezept_throwsException() {
        assertThatThrownBy(() -> rezeptService.save(null))
                .isInstanceOf(NullPointerException.class);
    }

    // Positiv-Test: deleteById()
    @Test
    void testDeleteById_validId() {
        doNothing().when(rezeptRepository).deleteById(1L);

        rezeptService.deleteById(1L);

        verify(rezeptRepository, times(1)).deleteById(1L);
    }

    // Randfall: deleteById mit ungültiger ID
    @Test
    void testDeleteById_invalidId() {
        doNothing().when(rezeptRepository).deleteById(999L);

        rezeptService.deleteById(999L);

        verify(rezeptRepository).deleteById(999L);
    }

    // Positiv-Test: findByIstVegetarisch
    @Test
    void testFindByIstVegetarisch_true() {
        Rezept rezept = new Rezept("Gemüsepfanne", "Veggie", 15, true, 4.0, LocalDate.now());
        when(rezeptRepository.findByIstVegetarisch(true)).thenReturn(List.of(rezept));

        List<Rezept> result = rezeptService.findByIstVegetarisch(true);

        assertThat(result).hasSize(1).contains(rezept);
    }

    // Randfall: leerer Rückgabewert
    @Test
    void testFindByIstVegetarisch_noResults() {
        when(rezeptRepository.findByIstVegetarisch(false)).thenReturn(List.of());

        List<Rezept> result = rezeptService.findByIstVegetarisch(false);

        assertThat(result).isEmpty();
    }
}
