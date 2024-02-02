package com.macedo.swplanetapi.domain;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    //possui tarefas comuns de teste, como persistir, atualizar, encontrar e remover entidades.
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createPlanet_WithInvalidData_ReturnsPlanet(){
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();

        //A constante não possui id, é preciso validar cada propriedade

        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimente()).isEqualTo(PLANET.getClimente());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    
    }
}
