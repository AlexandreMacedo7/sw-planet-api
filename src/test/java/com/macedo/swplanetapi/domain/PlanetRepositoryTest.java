package com.macedo.swplanetapi.domain;

import static com.macedo.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    // possui tarefas comuns de teste, como persistir, atualizar, encontrar e
    // remover entidades.
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();

        // A constante não possui id, é preciso validar cada propriedade

        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimente()).isEqualTo(PLANET.getClimente());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());

    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {

        /*É necessário setar o id, para o método "save" salva e atualiza dados, ele confere o id, se existir um, ele apenas atualiza
         * nesse caso, é preciso colocá-lo como nulo, para poder haver uma nova conferencia de dados.
         * 
         * Utiliza-se o detach, para tirar o gerenciamento do Entity Manager sobre a entidade, do contrário, o 
         * Entity Manager, saberá que aquele objeto esta salvo no banco de dados
         */

        Planet planet = testEntityManager.persistAndFlush(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);

    }
}
