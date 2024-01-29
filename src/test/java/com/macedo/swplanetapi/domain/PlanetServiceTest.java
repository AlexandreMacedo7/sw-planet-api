package com.macedo.swplanetapi.domain;

import static com.macedo.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

//@SpringBootTest(classes = PlanetService.class)
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    // @Autowired
    @InjectMocks
    private PlanetService planetService;

    // @MockBean
    @Mock
    private PlanetRepository planetRepository;

    // operacao_estado_retorno

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {

        // AAA

        // Arrange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        // Act
        // system under test
        Planet sut = planetService.create(PLANET);

        // Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        when(planetService.create(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(1l)).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.get(1l);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsPlanet() {

        when(planetRepository.findById(1l)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.get(1l);

        assertThat(sut).isEmpty();

    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {

        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.getByName(PLANET.getName());

        assertThat(sut.get()).isEqualTo(PLANET);

    }

    @Test
    public void getPlanet_ByExistingName_ReturnsEmpty() {

        final String name = "Unexisting name";

        when(planetRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet> sud = planetService.getByName(name);

        assertThat(sud).isEmpty();

    }

    @Test
    public void listPlanets_ReturnAllPlanets() {

        List<Planet> planets = new ArrayList<>() {
            {
                add(PLANET);
            }
        };
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimente(), PLANET.getTerrain()));
        when(planetRepository.findAll(query)).thenReturn(planets);


        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimente());


        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {

        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimente());

        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_doesNotThrowAnyException() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WhithUexistingId_ThrowsException() {

        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

        assertThatThrownBy(() -> planetService.remove(99l)).isInstanceOf(RuntimeException.class);
    }
}
