package br.com.b2w.b2wchallenge.data.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "planet")
@Data
public class PlanetEntity {

    @Id
    private String id;

    private String name;

    private String climate;

    private String terrain;

    private Integer appearances = 0;

    public PlanetEntity(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }


}
