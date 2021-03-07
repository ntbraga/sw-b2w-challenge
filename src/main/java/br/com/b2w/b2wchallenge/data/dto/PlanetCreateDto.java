package br.com.b2w.b2wchallenge.data.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PlanetCreateDto {

    @NotEmpty(message = "Name is required")
    private final String name;
    @NotEmpty(message = "Climate is required")
    private final String climate;
    @NotEmpty(message = "Terrain is required")
    private final String terrain;

}
