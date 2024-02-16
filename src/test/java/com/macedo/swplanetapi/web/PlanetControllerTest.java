package com.macedo.swplanetapi.web;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.macedo.swplanetapi.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

}
