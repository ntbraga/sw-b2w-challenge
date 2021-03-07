package br.com.b2w.b2wchallenge.data.dto;

import br.com.b2w.b2wchallenge.utils.NullOrNotBlank;
import lombok.Data;

@Data
public class PlanetUpdateDto {

    @NullOrNotBlank(message = "Name must not be empty.")
    private final String name;
    @NullOrNotBlank(message = "Climate must not be empty.")
    private final String climate;
    @NullOrNotBlank(message = "Terrain must not be empty.")
    private final String terrain;

}
