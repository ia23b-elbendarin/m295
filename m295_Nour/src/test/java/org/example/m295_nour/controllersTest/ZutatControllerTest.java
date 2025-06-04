package org.example.m295_nour.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.m295_nour.configurations.SecurityConfig;
import org.example.m295_nour.controllers.ZutatController;
import org.example.m295_nour.models.Rezept;
import org.example.m295_nour.models.Zutat;
import org.example.m295_nour.services.ZutatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZutatController.class)
@Import(SecurityConfig.class)
class ZutatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private ZutatService zutatService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotSave() throws Exception {
        Zutat zutat = new Zutat("Butter", 200, "g", new Rezept());

        mockMvc.perform(post("/api/zutaten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zutat)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotDelete() throws Exception {
        mockMvc.perform(delete("/api/zutaten/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanSave() throws Exception {
        Zutat zutat = new Zutat("Butter", 200, "g", new Rezept());

        when(zutatService.save(any(Zutat.class))).thenReturn(zutat);

        mockMvc.perform(post("/api/zutaten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zutat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Butter"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanSaveAll() throws Exception {
        Zutat z1 = new Zutat("Mehl", 1.0, "kg", new Rezept());
        Zutat z2 = new Zutat("Milch", 1.0, "l", new Rezept());

        when(zutatService.saveAll(anyList())).thenReturn(List.of(z1, z2));

        mockMvc.perform(post("/api/zutaten/alle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(z1, z2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanDeleteById() throws Exception {
        mockMvc.perform(delete("/api/zutaten/1"))
                .andExpect(status().isOk());

        verify(zutatService).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanDeleteAll() throws Exception {
        mockMvc.perform(delete("/api/zutaten"))
                .andExpect(status().isOk());

        verify(zutatService).deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanGetAllZutaten() throws Exception {
        Zutat z1 = new Zutat("Salz", 0.5, "TL", new Rezept());
        when(zutatService.findAll()).thenReturn(List.of(z1));

        mockMvc.perform(get("/api/zutaten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanFindByRezeptId() throws Exception {
        Zutat zutat = new Zutat("Wasser", 1.0, "l", new Rezept());
        when(zutatService.findByRezeptId(2L)).thenReturn(List.of(zutat));

        mockMvc.perform(get("/api/zutaten/rezept/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Wasser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanFindById() throws Exception {
        Zutat zutat = new Zutat("Pfeffer", 1.0, "g", new Rezept());
        zutat.setId(1L);

        when(zutatService.findById(1L)).thenReturn(zutat);

        mockMvc.perform(get("/api/zutaten/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pfeffer"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanCheckExistsById() throws Exception {
        when(zutatService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/zutaten/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanSearchByName() throws Exception {
        Zutat zutat = new Zutat("Zucker", 100, "g", new Rezept());
        when(zutatService.findByNameContains("zuck")).thenReturn(List.of(zutat));

        mockMvc.perform(get("/api/zutaten/suche").param("name", "zuck"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanUpdateZutat() throws Exception {
        Zutat zutat = new Zutat("Zimt", 2.0, "g", new Rezept());
        zutat.setId(1L);

        when(zutatService.existsById(1L)).thenReturn(true);
        when(zutatService.save(any(Zutat.class))).thenReturn(zutat);

        mockMvc.perform(put("/api/zutaten/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zutat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zimt"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotUpdateZutat() throws Exception {
        Zutat zutat = new Zutat("Zimt", 2.0, "g", new Rezept());
        zutat.setId(1L);

        mockMvc.perform(put("/api/zutaten/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zutat)))
                .andExpect(status().isForbidden());
    }


}
