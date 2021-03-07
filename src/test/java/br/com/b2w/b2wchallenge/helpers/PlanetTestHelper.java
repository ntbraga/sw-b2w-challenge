package br.com.b2w.b2wchallenge.helpers;

import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

public class PlanetTestHelper {

    public static PlanetEntity createNewPlanet(String name, String climate, String terrain) {
        return new PlanetEntity(name, climate, terrain);
    }

    public static PlanetEntity createNewPlanet() {
        return PlanetTestHelper.createNewPlanet("Tatooine", "Árido", "Deserto");
    }

    public static PlanetEntity createNewPlanet(PlanetCreateDto planetCreateDto) {
        return PlanetTestHelper.createNewPlanet(planetCreateDto.getName(), planetCreateDto.getClimate(), planetCreateDto.getTerrain());
    }

    public static PlanetEntity createNewPlanetWithId() {
        PlanetEntity planetEntity = PlanetTestHelper.createNewPlanet();
        planetEntity.setId(new ObjectId().toString());
        return planetEntity;
    }

    public static PlanetEntity createNewPlanetWithId(String name, String climate, String terrain) {
        PlanetEntity planetEntity = PlanetTestHelper.createNewPlanet(name, climate, terrain);
        planetEntity.setId(new ObjectId().toString());
        return planetEntity;
    }

    public static PlanetEntity createNewPlanetWithId(ObjectId id) {
        PlanetEntity planetEntity = PlanetTestHelper.createNewPlanet();
        planetEntity.setId(id.toString());
        return planetEntity;
    }

    public static PlanetCreateDto createNewPlanetDto(String name, String climate, String terrain) {
        return new PlanetCreateDto(name, climate, terrain);
    }

    public static PlanetCreateDto createNewPlanetDto() {
        return PlanetTestHelper.createNewPlanetDto("Tatooine", "Árido", "Deserto");
    }


    public static PlanetUpdateDto createUpdatePlanetDto(String name, String climate, String terrain) {
        return new PlanetUpdateDto(name, climate, terrain);
    }

    public static PlanetUpdateDto createUpdatePlanetDto() {
        return PlanetTestHelper.createUpdatePlanetDto("Alderaan", "Temperado", "Pastagens, montanhas");
    }

    public static List<PlanetEntity> createListOfPlanets() {
        return Arrays.asList(
                PlanetTestHelper.createNewPlanetWithId("Tatooine", "Árido", "Deserto"),
                PlanetTestHelper.createNewPlanetWithId("Alderaan", "Temperado", "Pastagens, montanhas"),
                PlanetTestHelper.createNewPlanetWithId("Yavin IV", "Teperado, tropical", "Selvas, florestas tropicais"),
                PlanetTestHelper.createNewPlanetWithId("Hoth", "Congelado", "Tundra, cavernas congeladas, cordilheiras")
        );
    }

    public static Page<PlanetEntity> createPageOfPlanets() {
        List<PlanetEntity> planets = PlanetTestHelper.createListOfPlanets();
        return new PageImpl<>(planets);
    }

}
