package org.example.m295_nour.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.m295_nour.controllers.RezeptController;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.services.RezeptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RezeptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RezeptService rezeptService;

    @InjectMocks
    private RezeptController rezeptController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rezeptController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // ✅ wichtig für LocalDate
    }

    @Test
    void testFindAll() throws Exception {
        Rezept r1 = new Rezept("Pizza", "Käse", 20, true, 4.5, LocalDate.now());
        Rezept r2 = new Rezept("Salat", "leicht", 10, true, 4.0, LocalDate.now());

        when(rezeptService.findAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/rezepte"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));

        verify(rezeptService).findAll();
    }

    @Test
    void testSave() throws Exception {
        Rezept rezept = new Rezept(
                "Burger",
                "Leckerer Burger mit viel Käse", // ✅ >10 Zeichen
                15,
                false,
                4.2,
                LocalDate.now()
        );

        when(rezeptService.save(any(Rezept.class))).thenReturn(rezept);

        mockMvc.perform(post("/api/rezepte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rezept)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Burger"));
    }


    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/api/rezepte/1"))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(rezeptService).deleteById(1L);
    }

    @Test
    void testUpdateWhenRezeptExists() throws Exception {
        Rezept updated = new Rezept(
                "Tacos",
                "Würzige mexikanische Spezialität", // ✅ gültige Beschreibung
                25,
                false,
                4.0,
                LocalDate.now()
        );
        updated.setId(1L);

        when(rezeptService.existsById(1L)).thenReturn(true);
        when(rezeptService.save(any())).thenReturn(updated);

        mockMvc.perform(put("/api/rezepte/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tacos"));
    }


    @Test
    void testUpdateWhenRezeptDoesNotExist() throws Exception {
        Rezept updated = new Rezept(
                "Tacos",
                "Eine würzige mexikanische Spezialität",
                25,
                false,
                4.0,
                LocalDate.now()
        );
        updated.setId(1L);

        when(rezeptService.existsById(1L)).thenReturn(false);

        mockMvc.perform(put("/api/rezepte/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }


    @Test
    void testFindByIdFound() throws Exception {
        Rezept rezept = new Rezept("Lasagne", "klassisch", 60, true, 5.0, LocalDate.now());
        rezept.setId(1L);

        when(rezeptService.findById(1L)).thenReturn(rezept);

        mockMvc.perform(get("/api/rezepte/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lasagne"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(rezeptService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/rezepte/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveAll() throws Exception {
        Rezept r1 = new Rezept("Suppe", "warme Suppe", 15, true, 3.5, LocalDate.now());
        Rezept r2 = new Rezept("Toast", "schnell", 5, false, 2.5, LocalDate.now());

        when(rezeptService.saveAll(anyList())).thenReturn(List.of(r1, r2));

        mockMvc.perform(post("/api/rezepte/alle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(r1, r2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testExistsById() throws Exception {
        when(rezeptService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/rezepte/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testFindByIstVegetarischTrue() throws Exception {
        Rezept r1 = new Rezept("Gemüsepfanne", "lecker", 20, true, 4.0, LocalDate.now());

        when(rezeptService.findByIstVegetarisch(true)).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/rezepte/vegetarisch").param("wert", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].istVegetarisch").value(true));
    }

    @Test
    void testDeleteBefore() throws Exception {
        mockMvc.perform(delete("/api/rezepte/vorDatum").param("datum", "2023-01-01"))
                .andExpect(status().isNoContent());

        verify(rezeptService).deleteBefore(LocalDate.of(2023, 1, 1));
    }

    @Test
    void testDeleteAll() throws Exception {
        mockMvc.perform(delete("/api/rezepte"))
                .andExpect(status().isNoContent());

        verify(rezeptService).deleteAll();
    }

}
