package br.com.b2w.b2wchallenge.services.impl;

import br.com.b2w.b2wchallenge.config.RestConfig;
import br.com.b2w.b2wchallenge.config.ServiceException;
import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import br.com.b2w.b2wchallenge.data.repositories.PlanetRepository;
import br.com.b2w.b2wchallenge.helpers.PlanetTestHelper;
import br.com.b2w.b2wchallenge.services.StarWarsService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Import({RestConfig.class, StarWarsServiceImpl.class})
class PlanetServiceImplTest {

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    @Autowired
    private StarWarsService starWarsService;

    private PlanetServiceImpl planetService;

    @BeforeEach
    void setUp() {
        this.planetService = new PlanetServiceImpl(this.planetRepository, this.starWarsService);
    }

    @Test
    @DisplayName("Save new planet dto with success")
    void saveNewPlanetDtoSuccess() {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto();
        ObjectId id = ObjectId.get();
        PlanetEntity planetEntityWithId = PlanetTestHelper.createNewPlanetWithId(id);

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(planetEntityWithId);

        PlanetEntity saved = this.planetService.create(planetCreateDto);

        assertNotNull(saved);
        assertEquals(saved, planetEntityWithId);
        assertEquals(saved.getAppearances(), 0);
        assertEquals(saved.getId(), planetEntityWithId.getId());
        assertEquals(saved.getName(), planetEntityWithId.getName());
        assertEquals(saved.getClimate(), planetEntityWithId.getClimate());
        assertEquals(saved.getTerrain(), planetEntityWithId.getTerrain());
    }

    @Test
    @DisplayName("Save new planet dto with rest error")
    void saveNewPlanetDtoWithRestError() {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto();
        ObjectId id = ObjectId.get();
        PlanetEntity planetEntityWithId = PlanetTestHelper.createNewPlanetWithId(id);

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(planetEntityWithId);
        when(this.starWarsService.getMovieAppearancesCount(any(String.class))).thenThrow(new RestClientException("Error when getting response"));

        PlanetEntity saved = this.planetService.create(planetCreateDto);

        assertNotNull(saved);
        assertEquals(saved, planetEntityWithId);
        assertEquals(saved.getAppearances(), 0);
        assertEquals(saved.getId(), planetEntityWithId.getId());
        assertEquals(saved.getName(), planetEntityWithId.getName());
        assertEquals(saved.getClimate(), planetEntityWithId.getClimate());
        assertEquals(saved.getTerrain(), planetEntityWithId.getTerrain());
    }

    @Test
    @DisplayName("Save planet entity with success")
    void savePlanetSuccess() {
        ObjectId id = ObjectId.get();
        PlanetEntity planetEntity = PlanetTestHelper.createNewPlanet();
        PlanetEntity planetEntityWithId = PlanetTestHelper.createNewPlanetWithId(id);

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(planetEntityWithId);

        PlanetEntity saved = this.planetService.save(planetEntity);

        assertNotNull(saved);
        assertEquals(saved, planetEntityWithId);
    }

    @Test
    @DisplayName("Update existing planet with error")
    void updatePlanetWithError() {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto();

        PlanetEntity updatedEntity = new PlanetEntity(
                planetUpdateDto.getName(),
                planetUpdateDto.getClimate(),
                planetUpdateDto.getTerrain()
        );

        updatedEntity.setId(id.toString());

        assertThrows(ServiceException.class, () -> this.planetService.update(id.toString(), planetUpdateDto));
    }

    @Test
    @DisplayName("Update existing planet with success")
    void updateExistingPlanetWithSuccess() {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto();

        PlanetEntity updatedEntity = new PlanetEntity(
                planetUpdateDto.getName(),
                planetUpdateDto.getClimate(),
                planetUpdateDto.getTerrain()
        );

        updatedEntity.setId(id.toString());

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(updatedEntity);
        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.of(updatedEntity));

        PlanetEntity saved = assertDoesNotThrow(() -> this.planetService.update(id.toString(), planetUpdateDto));
        assertNotNull(saved);
        assertEquals(saved, updatedEntity);
    }

    @Test
    @DisplayName("Update existing planet using entity with success")
    void updateExistingPlanetEntitySuccess() {
        PlanetEntity updatedEntity = PlanetTestHelper.createNewPlanetWithId();

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(updatedEntity);

        PlanetEntity saved = assertDoesNotThrow(() -> this.planetService.update(updatedEntity));
        assertNotNull(saved);
        assertEquals(saved, updatedEntity);

    }

    @Test
    @DisplayName("Update existing planet name with rest error")
    void updateExistingPlanetEntityWithError() {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto();

        PlanetEntity updatedEntity = new PlanetEntity(
                planetUpdateDto.getName(),
                planetUpdateDto.getClimate(),
                planetUpdateDto.getTerrain()
        );

        updatedEntity.setId(id.toString());

        when(this.planetRepository.save(any(PlanetEntity.class))).thenReturn(updatedEntity);
        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.of(updatedEntity));
        when(this.starWarsService.getMovieAppearancesCount(any(String.class))).thenThrow(new RestClientException("Error when getting response"));

        PlanetEntity saved = assertDoesNotThrow(() -> this.planetService.update(id.toString(), planetUpdateDto));
        assertNotNull(saved);
        assertEquals(saved, updatedEntity);
    }

    @Test
    @DisplayName("Find inexistend entity")
    void findEntityByIdNotExistent() {
        ObjectId id = ObjectId.get();
        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.empty());

        Optional<PlanetEntity> optionalPlanetEntity = this.planetService.findById(id.toString());
        assertTrue(optionalPlanetEntity.isEmpty());
    }

    @Test
    @DisplayName("Find existent entity")
    void findEntityById() {
        ObjectId id = ObjectId.get();
        PlanetEntity updatedEntity = PlanetTestHelper.createNewPlanetWithId(id);

        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.of(updatedEntity));

        Optional<PlanetEntity> optionalPlanetEntity = this.planetService.findById(id.toString());
        assertTrue(optionalPlanetEntity.isPresent());
    }

    @Test
    @DisplayName("Find all with page")
    void findAllWithPage() {
        Pageable pageRequest = Pageable.unpaged();
        Page<PlanetEntity> planetsPage = PlanetTestHelper.createPageOfPlanets();

        when(this.planetRepository.findAll(any(Pageable.class))).thenReturn(planetsPage);

        Page<PlanetEntity> planetsResult = this.planetService.findAll(pageRequest);

        assertNotNull(planetsResult);
        assertNotNull(planetsResult.getContent());
        assertFalse(planetsResult.getContent().isEmpty());
        assertEquals(planetsResult.getSize(), planetsPage.getSize());

    }

    @Test
    @DisplayName("Find all with page and name empty")
    void findAllWithPageAndNameEmpty() {
        Pageable pageRequest = Pageable.unpaged();
        Page<PlanetEntity> planetsPage = PlanetTestHelper.createPageOfPlanets();

        when(this.planetRepository.findAll(any(Pageable.class))).thenReturn(planetsPage);

        Page<PlanetEntity> planetsResult = this.planetService.findAll("", pageRequest);

        assertNotNull(planetsResult);
        assertNotNull(planetsResult.getContent());
        assertFalse(planetsResult.getContent().isEmpty());
        assertEquals(planetsResult.getSize(), planetsPage.getSize());
    }

    @Test
    @DisplayName("Find all with page and name")
    void findAllWithPageAndName() {
        Pageable pageRequest = Pageable.unpaged();
        Page<PlanetEntity> planetsPage = PlanetTestHelper.createPageOfPlanets();

        when(this.planetRepository.findByNameLike(any(String.class), any(Pageable.class))).thenReturn(planetsPage);

        Page<PlanetEntity> planetsResult = this.planetService.findAll("Tatooine", pageRequest);

        assertNotNull(planetsResult);
        assertNotNull(planetsResult.getContent());
        assertFalse(planetsResult.getContent().isEmpty());
        assertEquals(planetsResult.getSize(), planetsPage.getSize());
    }

    @Test
    @DisplayName("Delete existing planet success")
    void deleteExistingPlanet() {
        ObjectId id = ObjectId.get();

        PlanetEntity updatedEntity = PlanetTestHelper.createNewPlanetWithId(id);

        updatedEntity.setId(id.toString());

        doNothing().when(this.planetRepository).delete(any(PlanetEntity.class));
        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.of(updatedEntity));

        assertDoesNotThrow(() -> this.planetService.deleteById(id.toString()));
    }

    @Test
    @DisplayName("Delete not existing planet error")
    void deleteNotExistingPlanet() {
        ObjectId id = ObjectId.get();

        when(this.planetRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> this.planetService.deleteById(id.toString()));
    }

}