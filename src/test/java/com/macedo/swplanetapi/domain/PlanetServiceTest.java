package com.macedo.swplanetapi.domain;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static com.macedo.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//@SpringBootTest(classes = PlanetService.class)
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {


    //@Autowired
    @InjectMocks
    private PlanetService planetService;

    //@MockBean
    @Mock
    private PlanetRepository planetRepository;


    //operacao_estado_retorno

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){

        //AAA

        //Arrange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        //Act
        //system under test
        Planet sut = planetService.create(PLANET);

        //Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){

       when(planetService.create(INVALID_PLANET)).thenThrow(RuntimeException.class);

       assertThatThrownBy(()-> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);

    }
    
}
