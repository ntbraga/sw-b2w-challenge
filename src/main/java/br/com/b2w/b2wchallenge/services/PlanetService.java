package br.com.b2w.b2wchallenge.services;

import br.com.b2w.b2wchallenge.config.ServiceException;
import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlanetService {

    PlanetEntity create(PlanetCreateDto planetDto);
    PlanetEntity save(PlanetEntity entity);
    PlanetEntity update(String id, PlanetUpdateDto planetUpdateDto) throws ServiceException;
    PlanetEntity update(PlanetEntity entity) throws ServiceException;
    Optional<PlanetEntity> findById(String id);
    Page<PlanetEntity> findAll(Pageable pageable);
    Page<PlanetEntity> findAll(String name, Pageable pageable);
    void deleteById(String id) throws ServiceException;

}
