package com.macedo.swplanetapi.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {


    @Autowired
    private PlanetService planetService;


    //operacao_estado_retorno

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){

        //system under test
        Planet sut = planetService.create(PLANET);

        assertThat(sut).isEqualTo(PLANET);
    }
    
}
