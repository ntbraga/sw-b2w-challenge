package br.com.b2w.b2wchallenge.services.impl;

import br.com.b2w.b2wchallenge.config.ServiceException;
import br.com.b2w.b2wchallenge.data.dto.PlanetCreateDto;
import br.com.b2w.b2wchallenge.data.dto.PlanetUpdateDto;
import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import br.com.b2w.b2wchallenge.data.repositories.PlanetRepository;
import br.com.b2w.b2wchallenge.services.PlanetService;
import br.com.b2w.b2wchallenge.services.StarWarsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;
    private final StarWarsService starWarsService;

    private static final Logger logger = LoggerFactory.getLogger(PlanetServiceImpl.class);

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository, StarWarsService starWarsService) {
        this.planetRepository = planetRepository;
        this.starWarsService = starWarsService;
    }

    @Override
    public PlanetEntity create(PlanetCreateDto planetDto) {
        PlanetEntity entity = new PlanetEntity(planetDto.getName(), planetDto.getClimate(), planetDto.getTerrain());

        try {
            Integer count = this.starWarsService.getMovieAppearancesCount(entity.getName());
            entity.setAppearances(count);
        } catch (RestClientException ex) {
            logger.error(String.format("Failed to get planet '%s' movie appearances", planetDto.getName()), ex);
        }

        return this.save(entity);
    }

    @Override
    public PlanetEntity save(PlanetEntity entity) {
        return this.planetRepository.save((entity));
    }

    @Override
    public PlanetEntity update(String id, PlanetUpdateDto planetUpdateDto) throws ServiceException {
        PlanetEntity entity = this.planetRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Planet", id));

        if(StringUtils.isNotEmpty(planetUpdateDto.getName())) {
            entity.setName(planetUpdateDto.getName());

            try {
                Integer count = this.starWarsService.getMovieAppearancesCount(entity.getName());
                entity.setAppearances(count);
            } catch (RestClientException ex) {
                logger.error(String.format("Failed to get planet '%s' movie appearances", planetUpdateDto.getName()), ex);
            }

        }

        if(StringUtils.isNotEmpty(planetUpdateDto.getClimate())) {
            entity.setClimate(planetUpdateDto.getClimate());
        }

        if(StringUtils.isNotEmpty(planetUpdateDto.getTerrain())) {
            entity.setTerrain(planetUpdateDto.getTerrain());
        }

        return this.update(entity);
    }

    @Override
    public PlanetEntity update(PlanetEntity entity) throws ServiceException {
        return this.save(entity);
    }

    @Override
    public Optional<PlanetEntity> findById(String id) {
        return this.planetRepository.findById(id);
    }

    @Override
    public Page<PlanetEntity> findAll(Pageable pageable) {
        return this.planetRepository.findAll(pageable);
    }

    @Override
    public Page<PlanetEntity> findAll(String name, Pageable pageable) {
        if(StringUtils.isNotEmpty(name)) {
            return this.planetRepository.findByNameLike(name, pageable);
        }
        return this.findAll(pageable);
    }

    @Override
    public void deleteById(String id) throws ServiceException{
        PlanetEntity entity = this.planetRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Planet", id));

        this.planetRepository.delete(entity);
    }
}
