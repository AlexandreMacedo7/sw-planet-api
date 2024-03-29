package com.macedo.swplanetapi.web;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static com.macedo.swplanetapi.common.PlanetConstants.PLANETS;
import static com.macedo.swplanetapi.common.PlanetConstants.TATOOINE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macedo.swplanetapi.domain.Planet;
import com.macedo.swplanetapi.domain.PlanetService;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PlanetService planetService;

        @Test
        public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
                var planetJson = objectMapper.writeValueAsString(PLANET); // Utilizado para transformar objetos em
                                                                          // strings Json

                when(planetService.create(PLANET)).thenReturn(PLANET);

                mockMvc.perform(post("/planets").content(planetJson).contentType(MediaType.APPLICATION_JSON))// Informa
                                                                                                             // que é um
                                                                                                             // json
                                                                                                             // sendo
                                                                                                             // passado
                                .andExpect(status().isCreated())// Verficia status
                                .andExpect(jsonPath("$").value(PLANET));// Verifica o objeto passado na raiz do Json
        }

        @Test
        public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
                var emptyPlanet = objectMapper.writeValueAsString(new Planet());
                var invalidPlanet = objectMapper.writeValueAsString(new Planet("", "", ""));

                mockMvc.perform(post("/planets").content(emptyPlanet).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnprocessableEntity());

                mockMvc.perform(post("/planets").content(invalidPlanet).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
                when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

                mockMvc.perform(
                                post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isConflict());
        }

        @Test
        public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
                when(planetService.get(1L)).thenReturn(Optional.of(PLANET));
                mockMvc.perform(
                                get("/planets/1"))
                                .andExpect(status().isOk())// Verficia status
                                .andExpect(jsonPath("$").value(PLANET));
        }

        @Test
        public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
                mockMvc.perform(get("/planets/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
                when(planetService.getByName("name")).thenReturn(Optional.of(PLANET));

                mockMvc.perform(
                                get("/planets/name/" + PLANET.getName()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").value(PLANET));
        }

        @Test
        public void getPlanet_ByUnexistingName_ReturnsPlanet() throws Exception {
                mockMvc.perform(get("/planets/name/name"))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void listPlanets_ReturnsFilteredPlanets() throws Exception {

                when(planetService.list(null, null)).thenReturn(PLANETS);
                when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

                mockMvc
                        .perform(
                                get("/planets"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(3)));// tamanho da lista de retorno

                        mockMvc.perform(
                                get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0]").value(TATOOINE)); // veficia se na posição inicial da lista,
                                                                              // o objeto é o esperado.
        }
        @Test
        public void listPlanets_ReturnsNoPlanets() throws Exception {
                when(planetService.list(null, null)).thenReturn(Collections.emptyList());
        }

        @Test
        public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
                mockMvc.perform(
                        delete("/planets/1"))
                .andExpect(status().isNoContent());
        }

         @Test
        public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        var planetId = 1L;

                Mockito.doThrow(new EmptyResultDataAccessException(1))
                .when(planetService).remove(planetId);

                mockMvc.perform(
                        delete("/planets/" + planetId))
                .andExpect(status().isNotFound());
        }

}
