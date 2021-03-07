package br.com.b2w.b2wchallenge.controllers;

import br.com.b2w.b2wchallenge.config.RestConfig;
import br.com.b2w.b2wchallenge.config.ServiceException;
import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import br.com.b2w.b2wchallenge.data.repositories.PlanetRepository;
import br.com.b2w.b2wchallenge.helpers.PlanetTestHelper;
import br.com.b2w.b2wchallenge.services.impl.PlanetServiceImpl;
import br.com.b2w.b2wchallenge.services.impl.StarWarsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

@WebMvcTest(controllers = PlanetController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Import({RestConfig.class, StarWarsServiceImpl.class})
class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PlanetRepository planetRepository;

    @MockBean
    private PlanetServiceImpl planetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create planet with success")
    void createPlanet() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto();
        ObjectId id = new ObjectId();
        PlanetEntity returnPlanet = PlanetTestHelper.createNewPlanetWithId(id);
        when(this.planetService.create(any(PlanetCreateDto.class))).thenReturn(returnPlanet);

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath(("$.name"), is(returnPlanet.getName())))
                .andExpect(jsonPath(("$.climate"), is(returnPlanet.getClimate())))
                .andExpect(jsonPath(("$.terrain"), is(returnPlanet.getTerrain())))
                .andExpect(jsonPath(("$.appearances"), is(returnPlanet.getAppearances())));

    }

    @Test
    @DisplayName("Create planet where name is null error")
    void createPlanetWithNameNull() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto(null, "Árido", "Deserto");

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].rejected", is(nullValue())))
                .andExpect(jsonPath("$.errors[0].message", is("Name is required")));
    }

    @Test
    @DisplayName("Create planet where name is empty error")
    void createPlanetWithNameEmpty() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto("", "Árido", "Deserto");

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Name is required")));
    }

    @Test
    @DisplayName("Create planet where climate is null error")
    void createPlanetWithClimateNull() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto("Tatooine", null, "Deserto");

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("climate")))
                .andExpect(jsonPath("$.errors[0].rejected", is(nullValue())))
                .andExpect(jsonPath("$.errors[0].message", is("Climate is required")));
    }

    @Test
    @DisplayName("Create planet where climate is empty error")
    void createPlanetWithClimateEmpty() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto("Tatooine", "", "Deserto");

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("climate")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Climate is required")));
    }


    @Test
    @DisplayName("Create planet where terrain is null error")
    void createPlanetWithTerrainNull() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto("Tatooine", "Árido", null);

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("terrain")))
                .andExpect(jsonPath("$.errors[0].rejected", is(nullValue())))
                .andExpect(jsonPath("$.errors[0].message", is("Terrain is required")));
    }

    @Test
    @DisplayName("Create planet where terrain is empty error")
    void createPlanetWithTerrainEmpty() throws Exception {
        PlanetCreateDto planetCreateDto = PlanetTestHelper.createNewPlanetDto("Tatooine", "Árido", "");

        this.mockMvc.perform(post("/planets").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("terrain")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Terrain is required")));
    }

    @Test
    @DisplayName("Update planet with all properties success")
    void updatePlanetWithAllPropertiesSuccess() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto();

        PlanetEntity updatedEntity = new PlanetEntity(
                planetUpdateDto.getName(),
                planetUpdateDto.getClimate(),
                planetUpdateDto.getTerrain()
        );

        updatedEntity.setId(id.toString());

        when(this.planetService.update(any(String.class), any(PlanetUpdateDto.class))).thenReturn(updatedEntity);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath(("$.name"), is(updatedEntity.getName())))
                .andExpect(jsonPath(("$.climate"), is(updatedEntity.getClimate())))
                .andExpect(jsonPath(("$.terrain"), is(updatedEntity.getTerrain())))
                .andExpect(jsonPath(("$.appearances"), is(updatedEntity.getAppearances())));
    }

    @Test
    @DisplayName("Update planet only with name success")
    void updatePlanetOnlyWithNameSuccess() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto("Alderaan", null, null);

        PlanetEntity updatedEntity = new PlanetEntity(
                planetUpdateDto.getName(),
                "Árido", "Deserto"
        );

        updatedEntity.setId(id.toString());

        when(this.planetService.update(any(String.class), any(PlanetUpdateDto.class))).thenReturn(updatedEntity);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath(("$.name"), is(updatedEntity.getName())))
                .andExpect(jsonPath(("$.climate"), is(updatedEntity.getClimate())))
                .andExpect(jsonPath(("$.terrain"), is(updatedEntity.getTerrain())))
                .andExpect(jsonPath(("$.appearances"), is(updatedEntity.getAppearances())));
    }

    @Test
    @DisplayName("Update planet only with climate success")
    void updatePlanetOnlyWithClimateSuccess() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto(null, "Temperado", null);

        PlanetEntity updatedEntity = new PlanetEntity(
                "Tatooine",
                planetUpdateDto.getClimate(), "Deserto"
        );

        updatedEntity.setId(id.toString());

        when(this.planetService.update(any(String.class), any(PlanetUpdateDto.class))).thenReturn(updatedEntity);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath(("$.name"), is(updatedEntity.getName())))
                .andExpect(jsonPath(("$.climate"), is(updatedEntity.getClimate())))
                .andExpect(jsonPath(("$.terrain"), is(updatedEntity.getTerrain())))
                .andExpect(jsonPath(("$.appearances"), is(updatedEntity.getAppearances())));
    }

    @Test
    @DisplayName("Update planet only with terrain success")
    void updatePlanetOnlyWithTerrainSuccess() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto(null, null, "Pastagens, montanhas");

        PlanetEntity updatedEntity = new PlanetEntity(
                "Tatooine", "Árido", planetUpdateDto.getTerrain()
        );

        updatedEntity.setId(id.toString());

        when(this.planetService.update(any(String.class), any(PlanetUpdateDto.class))).thenReturn(updatedEntity);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath(("$.name"), is(updatedEntity.getName())))
                .andExpect(jsonPath(("$.climate"), is(updatedEntity.getClimate())))
                .andExpect(jsonPath(("$.terrain"), is(updatedEntity.getTerrain())))
                .andExpect(jsonPath(("$.appearances"), is(updatedEntity.getAppearances())));
    }

    @Test
    @DisplayName("Update planet with name empty error")
    void updatePlanetWithNameEmpty() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto("", null, null);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Name must not be empty.")));
    }

    @Test
    @DisplayName("Update planet with climate empty error")
    void updatePlanetWithClimateEmpty() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto(null, "", null);

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("climate")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Climate must not be empty.")));
    }

    @Test
    @DisplayName("Update planet with terrain empty error")
    void updatePlanetWithTerrainEmpty() throws Exception {
        ObjectId id = ObjectId.get();

        PlanetUpdateDto planetUpdateDto = PlanetTestHelper.createUpdatePlanetDto(null, null, "");

        this.mockMvc.perform(put("/planets/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("terrain")))
                .andExpect(jsonPath("$.errors[0].rejected", is("")))
                .andExpect(jsonPath("$.errors[0].message", is("Terrain must not be empty.")));
    }

    @Test
    @DisplayName("Find all planets")
    void findAll() throws Exception {
        Page<PlanetEntity> planetEntityPage = PlanetTestHelper.createPageOfPlanets();

        when(this.planetService.findAll(any(String.class), any(Pageable.class))).thenReturn(planetEntityPage);

        List<PlanetEntity> content = planetEntityPage.getContent();

        this.mockMvc.perform(get("/planets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(planetEntityPage.getSize())))
                .andExpect(jsonPath("$.empty", is(planetEntityPage.isEmpty())))
                .andExpect(jsonPath("$.first", is(planetEntityPage.isFirst())))
                .andExpect(jsonPath("$.content[0].id", is(content.get(0).getId())))
                .andExpect(jsonPath("$.content[0].name", is(content.get(0).getName())))
                .andExpect(jsonPath("$.content[0].climate", is(content.get(0).getClimate())))
                .andExpect(jsonPath("$.content[0].terrain", is(content.get(0).getTerrain())))
                .andExpect(jsonPath("$.content[0].appearances", is(content.get(0).getAppearances())))

                .andExpect(jsonPath("$.content[1].id", is(content.get(1).getId())))
                .andExpect(jsonPath("$.content[1].name", is(content.get(1).getName())))
                .andExpect(jsonPath("$.content[1].climate", is(content.get(1).getClimate())))
                .andExpect(jsonPath("$.content[1].terrain", is(content.get(1).getTerrain())))
                .andExpect(jsonPath("$.content[1].appearances", is(content.get(1).getAppearances())))

                .andExpect(jsonPath("$.content[2].id", is(content.get(2).getId())))
                .andExpect(jsonPath("$.content[2].name", is(content.get(2).getName())))
                .andExpect(jsonPath("$.content[2].climate", is(content.get(2).getClimate())))
                .andExpect(jsonPath("$.content[2].terrain", is(content.get(2).getTerrain())))
                .andExpect(jsonPath("$.content[2].appearances", is(content.get(2).getAppearances())))

                .andExpect(jsonPath("$.content[3].id", is(content.get(3).getId())))
                .andExpect(jsonPath("$.content[3].name", is(content.get(3).getName())))
                .andExpect(jsonPath("$.content[3].climate", is(content.get(3).getClimate())))
                .andExpect(jsonPath("$.content[3].terrain", is(content.get(3).getTerrain())))
                .andExpect(jsonPath("$.content[3].appearances", is(content.get(3).getAppearances())));
    }

    @Test
    @DisplayName("Find by existent id success")
    void findByIdSuccess() throws Exception {
        ObjectId id = ObjectId.get();
        PlanetEntity entity = PlanetTestHelper.createNewPlanetWithId(id);

        when(this.planetService.findById(any(String.class))).thenReturn(Optional.of(entity));

        this.mockMvc.perform(get("/planets/{id}", id.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(entity.getId())))
                .andExpect(jsonPath("$.name", is(entity.getName())))
                .andExpect(jsonPath("$.climate", is(entity.getClimate())))
                .andExpect(jsonPath("$.terrain", is(entity.getTerrain())))
                .andExpect(jsonPath("$.appearances", is(entity.getAppearances())));
    }

    @Test
    @DisplayName("Find by not existent by id error")
    void findById() throws Exception {
        ObjectId id = ObjectId.get();

        when(this.planetService.findById(any(String.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/planets/{id}", id.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete existing planet by id")
    void deleteExistingPlanetById() throws Exception{
        ObjectId id = ObjectId.get();

        PlanetEntity updatedEntity = PlanetTestHelper.createNewPlanetWithId(id);

        updatedEntity.setId(id.toString());

        doNothing().when(this.planetService).deleteById(any(String.class));

        this.mockMvc.perform(delete("/planets/{id}", id.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Delete not existing planet by id")
    void deleteNotExistingPlanetById() throws Exception{
        ObjectId id = ObjectId.get();

        PlanetEntity updatedEntity = PlanetTestHelper.createNewPlanetWithId(id);

        updatedEntity.setId(id.toString());

        ServiceException ex = new ServiceException("Planet", id.toString());
        doThrow(ex).when(this.planetService).deleteById(any(String.class));

        this.mockMvc.perform(delete("/planets/{id}", id.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is(ex.getMessage())))
                .andExpect(jsonPath("rootCause", is(ExceptionUtils.getRootCauseMessage(ex))));
    }

}