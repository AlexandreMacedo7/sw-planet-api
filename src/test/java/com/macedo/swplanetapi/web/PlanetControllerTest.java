package com.macedo.swplanetapi.web;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.macedo.swplanetapi.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        var planetJson = objectMapper.writeValueAsString(PLANET); //Utilizado para transformar objetos em strings Json

        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets").content(planetJson).contentType(MediaType.APPLICATION_JSON))//Informa que é um json sendo passado
                .andExpect(status().isCreated())//Verficia status
                .andExpect(jsonPath("$").value(PLANET));//Verifica o objeto passado na raiz do Json
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
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet()throws Exception{
        when(planetService.get(1L)).thenReturn(Optional.of(PLANET));
        mockMvc.perform(
                get("/planets/1"))
                .andExpect(status().isOk())//Verficia status
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception{
        mockMvc.perform(get("/planets/1"))
        .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception{
        when(planetService.getByName("name")).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                get("/planets/name/name"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsPlanet(){
   
    }

}
