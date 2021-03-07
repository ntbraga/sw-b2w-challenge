package br.com.b2w.b2wchallenge.controllers;

import br.com.b2w.b2wchallenge.config.ServiceException;
import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import br.com.b2w.b2wchallenge.services.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService planetService;

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<PlanetEntity> createPlanet(
            @RequestBody @Valid PlanetCreateDto planetCreateDto
    ) throws ServiceException {
        PlanetEntity entity = this.planetService.create(planetCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanetEntity> updatePlanet(
            @PathVariable("id") String id,
            @RequestBody @Valid PlanetUpdateDto planetUpdateDto
    ) throws ServiceException {
        PlanetEntity entity = this.planetService.update(id, planetUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @GetMapping
    public ResponseEntity<Page<PlanetEntity>> findAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            Pageable pageable
    ) throws ServiceException {

        Page<PlanetEntity> entities = this.planetService.findAll(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanetEntity> findById(
            @PathVariable("id") String id
    ) {
        Optional<PlanetEntity> opt = this.planetService.findById(id);
        return opt
                .map(entity -> ResponseEntity.status(HttpStatus.OK).body(entity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("id") String id
    ) throws ServiceException {
        this.planetService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
